package services;
import java.io.InputStream;
import java.util.ArrayList;

import exceptions.RecordIDAlreadyTaken;
import exceptions.RecordNotFound;
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
}
