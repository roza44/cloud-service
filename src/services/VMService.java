package services;

import java.util.ArrayList;

import model.Disk;
import model.KategorijaVM;
import model.VirtualnaMasina;

public class VMService {
	
	private static ArrayList<VirtualnaMasina> virtualMachines;
	
	private static void simulate() {
		// Simulate
		virtualMachines = new ArrayList<VirtualnaMasina>();

		VirtualnaMasina vm1 = new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32));
		vm1.addDisk(new Disk("Disk1", "ssd", 1.2, null));
		virtualMachines.add(vm1);
		
		VirtualnaMasina vm2 = new VirtualnaMasina("VM2", new KategorijaVM("Kat2", 8, 20, 128));
		vm2.addDisk(new Disk("Disk2", "hdd", 700.45, null));
		virtualMachines.add(vm2);
		///
	}
	
	public static void initialize()
	{
		virtualMachines = helpers.FileHandler.loadVMs();
		
		for (VirtualnaMasina vm : virtualMachines) {
			// Set refs to before loaded real objects
			vm.setKategorija(CatService.getCat(vm.getKategorija().getIme()));
			
			ArrayList<Disk> vmDisks = vm.getDiskovi();
			for (int i=0; i < vmDisks.size(); i++) {
				vmDisks.set(i, DiskService.getDisk(vmDisks.get(i).getIme()));
				vmDisks.get(i).setVm(vm);
			}
		}
		
		helpers.FileHandler.saveVMs(virtualMachines);
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

}
