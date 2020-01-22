package services;
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
	
	public static void add(Organizacija org) {
		organizacije.add(org);
		
		// Save
		helpers.FileHandler.save(organizacije);
	}
	
	public static void update(String name, Organizacija newObj) {
		Organizacija org = getOrganizacija(name);
		if (org == null) {
			// Create new
			organizacije.add(newObj);
		} else {
			org.setIme(newObj.getIme());
			org.setOpis(newObj.getOpis());
		}

		// Save
		helpers.FileHandler.save(organizacije);
	}
}
