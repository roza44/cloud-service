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
		
		organizacije.add(new Organizacija("Org1", "opis1"));
		organizacije.add(new Organizacija("Org2", "opis2"));
		
		helpers.FileHandler.saveOrgs(organizacije);
		///
	}
	
	public static void initialize() {
		organizacije = FileHandler.loadOrgs();
		
		ArrayList<Korisnik> users = UserService.getUsers();
		ArrayList<VirtualnaMasina> vms = VMService.getVirtualMachines();
		
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
