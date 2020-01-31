package services;

import java.util.ArrayList;

import model.Disk;
import model.KategorijaVM;
import model.Organizacija;
import model.VirtualnaMasina;

public class VMService {
	
	private static ArrayList<VirtualnaMasina> virtualMachines;
	
	private static void simulate() {
		// Simulate
		virtualMachines = new ArrayList<VirtualnaMasina>();

		VirtualnaMasina vm1 = new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32));
		vm1.setOrganizacija(new Organizacija("Org1", "Opis1"));
		virtualMachines.add(vm1);
		
		VirtualnaMasina vm2 = new VirtualnaMasina("VM2", new KategorijaVM("Kat2", 8, 20, 128));
		vm2.setOrganizacija(new Organizacija("Org2", "Opis1"));
		virtualMachines.add(vm2);
		
		helpers.FileHandler.saveVMs(virtualMachines);
		///
	}
	
	public static void initialize(boolean testMode)
	{
		if (testMode) simulate();
		
		virtualMachines = helpers.FileHandler.loadVMs();
		
		ArrayList<Disk> allDisks = DiskService.getDisks();
		
		for (VirtualnaMasina vm : virtualMachines) {
			// Set refs to before loaded real objects
			vm.setKategorija(CatService.getCat(vm.getKategorija().getIme()));
			
			for (Disk d : allDisks) {
				if (d.getVm() == null) continue; // A disk can exist without being part of a vm
				if (d.getVm().getIme().equals(vm.getIme())) {					
					vm.dodajDisk(d);
					d.setVm(vm);
				}
			}
		}
		
		helpers.FileHandler.saveVMs(virtualMachines);
	}
	
	public static boolean contains(VirtualnaMasina vm) {
		return virtualMachines.contains(vm);
	}
	
	public static boolean contains(String vm) {

		for(VirtualnaMasina m : virtualMachines)
			if(m.getIme().equals(vm))
				return true;
		
		return false;
	}
	
	public static ArrayList<VirtualnaMasina> getVirtualMachines() {
		return virtualMachines;
	}
	
	public static VirtualnaMasina getVirtualMachine(String ime) {
		for (VirtualnaMasina vm : virtualMachines) {
			if (vm.getIme().equals(ime)) {
				return vm;
			}
		}
		return null;
	}
	
	public static boolean catInUser(KategorijaVM kat) {
		
		for(VirtualnaMasina vm : virtualMachines)
			if(vm.getKategorija().equals(kat))
				return true;
		
		return false;
	}
	
	public static void addVM(VirtualnaMasina vm) {
		virtualMachines.add(vm);
	}
	
	public static void removeVM(VirtualnaMasina vm) {
		virtualMachines.remove(vm);
	}

}
