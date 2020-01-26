package services;

import java.util.ArrayList;

import model.Disk;

public class DiskService {
	private static ArrayList<Disk> disks;
	
	private static void simulate() {
		// Simulate
		disks = new ArrayList<Disk>();
		
		disks.add(new Disk("Disk1", "ssd", 1.2, null));
		disks.add(new Disk("Disk2", "hdd", 700.45, null));
		helpers.FileHandler.saveDisks(disks);
		///
	}
	
	public static void initialize() {
		
		
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
