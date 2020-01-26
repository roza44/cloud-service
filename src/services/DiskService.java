package services;

import java.util.ArrayList;

import model.Disk;
import model.KategorijaVM;
import model.VirtualnaMasina;

public class DiskService {
	private static ArrayList<Disk> disks;
	
	private static void simulate() {
		// Simulate
		disks = new ArrayList<Disk>();
		
		disks.add(new Disk("Disk1", "ssd", 1.2, new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32))));
		disks.add(new Disk("Disk2", "hdd", 700.45, new VirtualnaMasina("VM2", new KategorijaVM("Kat2", 8, 20, 128))));
		helpers.FileHandler.saveDisks(disks);
		///
	}
	
	public static void initialize() {
		//simulate();
		
		disks = helpers.FileHandler.loadDisks();
		
	}
	
	public static ArrayList<Disk> getDisks() {
		return disks;
	}
	
	public static Disk getDisk(String ime) {
		for (Disk d : disks) {
			if (d.getIme().equals(ime)) {
				return d;
			}
		}
		return null;
	}
}
