package services;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import exceptions.RecordIDAlreadyTaken;
import exceptions.RecordNotFound;
import helpers.FileHandler;
import model.Disk;
import model.KategorijaVM;
import model.Korisnik;
import model.Organizacija;
import model.VirtualnaMasina;
public class OrganizacijaService {
	private static ArrayList<Organizacija> organizacije;
	
	private static void simulate() {
		// Simulate
		organizacije = new ArrayList<Organizacija>();
		
		organizacije.add(new Organizacija("Org1", "opis1"));
		organizacije.add(new Organizacija("Org2", "opis2"));
		
		helpers.FileHandler.saveOrgs(organizacije);
		///
	}
	
	public static void initialize(boolean testMode) {
		if (testMode) simulate();
		
		organizacije = FileHandler.loadOrgs();
		
		ArrayList<Korisnik> users = UserService.getUsers();
		ArrayList<VirtualnaMasina> vms = VMService.getVirtualMachines();
		ArrayList<Disk> allDisks = DiskService.getDisks();
		
		for (Organizacija org : organizacije) {
			for (Korisnik k : users) {
				if (k.getOrganizacija().getIme().equals(org.getIme())) {
					org.dodajKorisnika(k);
					k.setOrganizacija(org);
				}
			}
			
			for (VirtualnaMasina v : vms) {
				if (v.getOrganizacija().getIme().equals(org.getIme())) {
					org.dodajResurs(v);
					v.setOrganizacija(org);
				}
			}
			
			for (Disk d : allDisks) {
				if (d.getOrganizacija().getIme().equals(org.getIme())) {					
					org.dodajDisk(d);
					d.setOrganizacija(org);
				}
			}
		}
	}
	
	public static ArrayList<Organizacija> getOrganizacije() {
		return organizacije;
	}
	
	public static ArrayList<Organizacija> getOrganizacije(String username) {
		Korisnik k = UserService.getUser(username);
		ArrayList<Organizacija> ret = new ArrayList<Organizacija>();
		ret.add(k.getOrganizacija());
		return ret;
	}
	
	public static Organizacija getOrganizacija(String ime) {
		for (Organizacija org : organizacije) {
			if (org.getIme().equals(ime)) {
				return org;
			}
		}
		
		return null;
	}
	
	public static void setImage(String ime, String extension, byte[] data) throws RecordNotFound {
		Organizacija org = getOrganizacija(ime);
		if (org == null) {
			throw new RecordNotFound("Organizacija " + ime + " ne postoji");
		} else {
			org.setSlikaPutanja(org.getSlikaIme() + "." + extension);
			helpers.FileHandler.saveImage(org.getSlikaIme(), extension, data);
		}
	}
	
	public static void add(Organizacija org) throws RecordIDAlreadyTaken {
		Organizacija sameName = getOrganizacija(org.getIme());
		if (sameName != null) throw new RecordIDAlreadyTaken("Organizacija " + org.getIme() + " vec postoji");
		organizacije.add(org);
		
		// Save
		helpers.FileHandler.saveOrgs(organizacije);
	}
	
	public static void update(Organizacija newObj) throws RecordNotFound {
		Organizacija org = getOrganizacija(newObj.getIme());
		if (org == null) {
			throw new RecordNotFound("Organizacija " + newObj.getIme() + " ne postoji");
		} else {
			org.setIme(newObj.getIme());
			org.setOpis(newObj.getOpis());

			// Save
			helpers.FileHandler.saveOrgs(organizacije);
		}

	}
	
	public static ArrayList<String> getIds(ArrayList<Organizacija> orgs) {
		ArrayList<String> ids = new ArrayList<String>();
		
		for(Organizacija o : orgs)
			ids.add(o.getIme());
		
		return ids;
	}
	
	public static ArrayList<String> getIdsForUsername(String username) {
		ArrayList<String> ids = new ArrayList<String>();
		Korisnik k = UserService.getUser(username);
		
		for (Organizacija org : organizacije) {
			if (org == k.getOrganizacija()) {
				ids.add(org.getIme());
				break;
			}
		}
		
		return ids;
	}
	
	public static ArrayList<String> getIds() {
		return getIds(organizacije);
	}
	
	// MONTHLY BILL, HOURLY PRICES
	// 720 = number of hours in 30 days
	private static final double HDD_PRICE = 0.1 / 720.0;
	private static final double SSD_PRICE = 0.3 / 720.0;
	
	private static final double CPU_PRICE = 25 / 720.0;
	private static final double RAM_PRICE = 15 / 720.0;
	private static final double GPU_PRICE = 1 / 720.0;
	
	public static HashMap<String, Double> calculateMonthlyBill(Organizacija org, Date from, Date to) throws RecordNotFound {
		// Get number of hours
		double hours = to.getTime() - from.getTime(); // milliseconds
		hours = hours / 1000.0; // seconds
		hours = hours / 3600.0; // hours
		
		HashMap<String, Double> bill = new HashMap<String, Double>();
		
		// Calculate disk prices
		double price;
		for (Disk d : org.getDiskovi()) {
			if (d.getTip().equalsIgnoreCase("hdd")) {
				price = d.getKapacitet() * HDD_PRICE * hours;
			} else {
				price = d.getKapacitet() * SSD_PRICE * hours;
			}
			
			bill.put("Disk_" + d.getIme(), Math.round(price * 100.0) / 100.0); // Rounding to 2 decimal places
		}
		
		// Calculate vm prices
		KategorijaVM kat;
		for (VirtualnaMasina vm : org.getResursi()) {
			hours = vm.getActiveHours(from, to);
			kat = vm.getKategorija();
			price = hours * (CPU_PRICE * kat.getBrojJezgara() + GPU_PRICE * kat.getGpuJezgra() + RAM_PRICE * kat.getRam());
			
			bill.put("VM_" + vm.getIme(), Math.round(price * 100.0) / 100.0);
		}

		return bill;
	}
}
