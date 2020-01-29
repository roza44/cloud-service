package controllers;

import org.eclipse.jetty.server.session.JDBCSessionManager.Session;

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
		VmToAdd vmInfo = g.fromJson(req.body(), VmToAdd.class);
		
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
	

}
