package services;
import java.io.InputStream;
import java.util.ArrayList;

import helpers.FileHandler;
import model.Organizacija;
public class OrganizacijaService {
	private static ArrayList<Organizacija> organizacije;
	
	public static void initialize() {
		organizacije = FileHandler.loadOrgs();
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
		helpers.FileHandler.save(organizacije);
	}
	
	public static boolean update(Organizacija newObj) {
		Organizacija org = getOrganizacija(newObj.getIme());
		if (org == null) {
			return false;
		} else {
			org.setIme(newObj.getIme());
			org.setOpis(newObj.getOpis());

			// Save
			helpers.FileHandler.save(organizacije);
			return true;
		}

	}
}
