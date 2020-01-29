package services;

import java.util.ArrayList;

import exceptions.RecordIDAlreadyTaken;
import exceptions.RecordIsReferenced;
import exceptions.RecordNotFound;
import model.Disk;
import model.KategorijaVM;
import model.Korisnik;
import model.Organizacija;
import model.VirtualnaMasina;

public class DiskService {
	private static ArrayList<Disk> disks;
	
	private static void simulate() {
		// Simulate
		disks = new ArrayList<Disk>();
		
		disks.add(new Disk("Disk1", "ssd", 1.2, new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32)), new Organizacija("Org1", "opis1")));
		disks.add(new Disk("Disk2", "hdd", 700.45, new VirtualnaMasina("VM2", new KategorijaVM("Kat2", 8, 20, 128)), new Organizacija("Org2", "opis2")));
		helpers.FileHandler.saveDisks(disks);
		///
	}
	
	public static void initialize(boolean testMode) {
		if (testMode) simulate();
		
		disks = helpers.FileHandler.loadDisks();
	}
	
	public static ArrayList<Disk> getDisks() {
		return disks;
	}
	
	public static ArrayList<Disk> getDisks(String username) {
		Korisnik k = UserService.getUser(username);
		Organizacija org = OrganizacijaService.getOrganizacija(k.getOrganizacija().getIme());
		
		ArrayList<Disk> ret = new ArrayList<Disk>();
		for (Disk d : disks) {
			if (d.getOrganizacija() == org) {
				ret.add(d);
			}
		}
		return ret;
	}
	
	public static void add(Disk d) throws RecordIDAlreadyTaken {
		Disk sameName = getDisk(d.getIme());
		if (sameName != null) throw new exceptions.RecordIDAlreadyTaken("Disk sa unetim imenom vec postoji");
		
		if (d.getVm() != null) {
			d.setVm(VMService.getVirtualMachine(d.getVm().getIme()));
		}
		d.setOrganizacija(OrganizacijaService.getOrganizacija(d.getOrganizacija().getIme()));
		
		disks.add(d);
		
		helpers.FileHandler.saveDisks(disks);
	}
	
	public static void update(Disk newDisk) throws exceptions.RecordNotFound {
		Disk d = getDisk(newDisk.getIme());
		if (d == null) throw new exceptions.RecordNotFound("Disk sa unetim imenom ne postoji");
		
		d.setKapacitet(newDisk.getKapacitet());
		d.setTip(newDisk.getTip());
		d.setVm(newDisk.getVm());
		
		helpers.FileHandler.saveDisks(disks);
	}
	
	public static void delete(String diskIme) throws RecordNotFound, RecordIsReferenced {
		Disk d = getDisk(diskIme);
		if (d == null) throw new exceptions.RecordNotFound("Disk sa unetim imenom ne postoji");
		
		OrganizacijaService.getOrganizacija(d.getOrganizacija().getIme()).obrisiDisk(d);
		disks.remove(d);
		
		helpers.FileHandler.saveDisks(disks);
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
