package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import gson_classes.VmToAdd;
import model.Disk;
import model.Organizacija;
import model.VirtualnaMasina;
import services.CatService;
import services.DiskService;
import services.OrganizacijaService;
import services.UserService;
import services.VMService;
import spark.Route;

public class VMController {
	
	private static Gson g = new Gson();
	
	private static String validate(VmToAdd v) {
		if (v.ime == null) return "Ime je obavezno polje";
		if (v.ime.equals("")) return "Ime je obavezno polje";
		if (v.kategorija == null) return "Organizacija je obavezno polje";
		if (v.organizacija == null) return "Organizacija je obavezno polje";
		if (v.diskovi == null) return "Diskovi su obavezno polje, moze i prazna lista";
		return null;
	}
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		
		String ret = "";
		if(req.session(false).attribute("role").equals("superadmin"))
			ret = g.toJson(VMService.getVirtualMachines()); 
		else
			ret = g.toJson(UserService.accessibleVMs(req.session(false).attribute("username")));
			
		return ret;
	};
	
	public static Route addVM = (req, res) -> {
		res.type("application/json");
		VmToAdd vmInfo;
		try {			
			vmInfo = g.fromJson(req.body(), VmToAdd.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(vmInfo);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		if(VMService.contains(vmInfo.ime)) {
			res.status(400);
			return g.toJson("Virualna masina sa unetim imenom vec postoji!");
		}
		
		VirtualnaMasina vm = new VirtualnaMasina(vmInfo.ime, CatService.getCat(vmInfo.kategorija));
		spark.Session s = req.session(false);
		
		if(s.attribute("role").equals("superadmin"))
			VmOrgBind(vm, OrganizacijaService.getOrganizacija(vmInfo.organizacija));
		else
			VmOrgBind(vm, UserService.getUser(s.attribute("username")).getOrganizacija());
		
		for(String d : vmInfo.diskovi)
			VmDiscBind(vm, DiskService.getDisk(d));
		
		if(vmInfo.aktivnost)
			vm.addActivity(vmInfo.aktivnost);
		
		res.status(200);
		VMService.addVM(vm);
		
		helpers.FileHandler.saveVMs(VMService.getVirtualMachines());
		
		return g.toJson(vm);
	};
	
	private static void VmOrgBind(VirtualnaMasina vm, Organizacija o) {
		vm.setOrganizacija(o);
		o.getResursi().add(vm);
	}
	
	private static void VmDiscBind(VirtualnaMasina vm, Disk d) {
		vm.getDiskovi().add(d);
		d.setVm(vm);
	}
	
	public static Route getDiscsIds = (req,res) -> {
		res.type("application/json");
		
		if (req.params(":vm") == null) {
			res.status(400);
			return "Query parametar 'vm' nedostaje";
		}
		
		ArrayList<String> ids = new ArrayList<String>();
		for(Disk d : VMService.getVirtualMachine(req.params(":vm")).getDiskovi())
			ids.add(d.getIme());
			
		return g.toJson(ids);
	};
	
	public static Route changeVM = (req, res) -> {
		res.type("application/json");
		
		VmToAdd vmInfo;
		try {			
			vmInfo = g.fromJson(req.body(), VmToAdd.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(vmInfo);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		VirtualnaMasina vm = VMService.getVirtualMachine(vmInfo.ime);
		
		vm.setKategorija(CatService.getCat(vmInfo.kategorija));
		vm.detachDiscs();
		
		for(String d : vmInfo.diskovi) {
			Disk disk = DiskService.getDisk(d);
			VmDiscBind(vm, disk);
		}
		
		if(vm.getActivity() != vmInfo.aktivnost)
			vm.addActivity(vmInfo.aktivnost);
		
		helpers.FileHandler.saveVMs(VMService.getVirtualMachines());
		
		return g.toJson(vm);
	};
	
	public static Route deleteVM = (req, res) -> {
		res.type("application/json");
		
		if (req.params(":vm") == null) {
			res.status(400);
			return "Query parametar 'vm' nedostaje";
		}
		
		VirtualnaMasina vm = VMService.getVirtualMachine(req.params(":vm"));
		
		vm.getOrganizacija().getResursi().remove(vm);
		
		for(Disk d : vm.getDiskovi())
			d.setVm(null);
		
		VMService.removeVM(vm);
		
		helpers.FileHandler.saveVMs(VMService.getVirtualMachines());
		
		return g.toJson(vm);
		
	};
	

}
