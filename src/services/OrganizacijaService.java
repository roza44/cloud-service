package services;
import java.util.ArrayList;

import model.Organizacija;
public class OrganizacijaService {
	private static ArrayList<Organizacija> organizacije;
	
	public static void initialize() {
		organizacije = new ArrayList<Organizacija>();
		
		organizacije.add(new Organizacija("Oblak hosting", "Kod nas nema promenljivog vremena!"));
		organizacije.add(new Organizacija("Lambda kod", "Garantovane implementacije projekata koje koriste iskljucivo lambda funkcije."));
		organizacije.add(new Organizacija("Cvarak pekara", "Prodajemo mnogo peciva i treba nam server da optimizuje testo."));
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
	
	public static void updateOrInsert(String name, Organizacija newObj) {
		Organizacija org = getOrganizacija(name);
		if (org == null) {
			// Create new
			organizacije.add(newObj);
		} else {
			org.setIme(newObj.getIme());
			org.setOpis(newObj.getOpis());
		}
			
	}
}
