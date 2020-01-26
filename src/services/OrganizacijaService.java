package services;
import java.io.InputStream;
import java.util.ArrayList;

import helpers.FileHandler;
import model.KategorijaVM;
import model.Korisnik;
import model.Organizacija;
import model.VirtualnaMasina;
public class OrganizacijaService {
	private static ArrayList<Organizacija> organizacije;
	
	private static void simulate() {
		// Simulate
		organizacije = new ArrayList<Organizacija>();
		
		Organizacija org1 = new Organizacija("Org1", "opis1");
		org1.dodajKorisnika(new Korisnik("roza44", "pass", "Joca", "Boca", null, "admin"));
		org1.dodajResurs(new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32)));
		organizacije.add(org1);
		
		Organizacija org2 = new Organizacija("Org2", "opis2");
		org2.dodajKorisnika(new Korisnik("stegnuti", "pass", "Stefko", "Stegko", null, "superadmin"));
		org2.dodajResurs(new VirtualnaMasina("VM2", new KategorijaVM("Kat2", 8, 20, 128)));
		organizacije.add(org2);
		
		helpers.FileHandler.saveOrgs(organizacije);
		///
	}
	
	public static void initialize() {
		organizacije = FileHandler.loadOrgs();
		
		ArrayList<Korisnik> users;
		ArrayList<VirtualnaMasina> vms;
		
		for (Organizacija org : organizacije) {
			users = org.getKorisnici();
			
			for (int i=0; i < users.size(); i++) {
				users.set(i, UserService.getUser(users.get(i).getEmail()));
				users.get(i).setOrganizacija(org);
			}
			
			vms = org.getResursi();
			
			for (int i=0; i < vms.size(); i++) {
				vms.set(i, VMService.getVirtualMachine(vms.get(i).getIme()));
			}
		}
	}
	
	public static ArrayList<Organizacija> getOrganizacije() {
		return organizacije;
	}
	
	public static Organizacija getOrganizacija(String ime) {
		for (Organizacija org : organizacije) {
			if (org.getIme().equals(ime)) {
				return org;
			}
		}
		
		return null;
	}
	
	public static boolean setImage(String ime, String extension, byte[] data) {
		Organizacija org = getOrganizacija(ime);
		if (org == null) {
			return false;
		} else {
			org.setSlikaPutanja(org.getSlikaIme() + "." + extension);
			helpers.FileHandler.saveImage(org.getSlikaIme(), extension, data);
			return true;
		}
	}
	public static void add(Organizacija org) {
		organizacije.add(org);
		
		// Save
		helpers.FileHandler.saveOrgs(organizacije);
	}
	
	public static boolean update(Organizacija newObj) {
		Organizacija org = getOrganizacija(newObj.getIme());
		if (org == null) {
			return false;
		} else {
			org.setIme(newObj.getIme());
			org.setOpis(newObj.getOpis());

			// Save
			helpers.FileHandler.saveOrgs(organizacije);
			return true;
		}

	}
}
