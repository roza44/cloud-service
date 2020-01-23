package services;

import java.util.ArrayList;

import model.KategorijaVM;
import model.VirtualnaMasina;

public class VMService {
	
	private static ArrayList<VirtualnaMasina> virtualMachines;
	
	
	public static void initialize()
	{
		virtualMachines = new ArrayList<>();
		
		VirtualnaMasina vm1 = new VirtualnaMasina("VM1", new KategorijaVM("Linux", 2, 2500, 10));
		VirtualnaMasina vm2 = new VirtualnaMasina("VM2", new KategorijaVM("Windows", 4, 3000, 20));
		
		virtualMachines.add(vm1);
		virtualMachines.add(vm2);		
	}
	
	public static ArrayList<VirtualnaMasina> getVirtualMachines() {
		return virtualMachines;
	}

}
