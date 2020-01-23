package services;

import java.util.ArrayList;

import model.Korisnik;
import model.Organizacija;

public class UserService {
	
	private static ArrayList<Korisnik> users;
	
	public static void initialize() {
		users = new ArrayList<Korisnik>();
		
		users.add(new Korisnik("roza44", "pass", "Joca", "Boca", OrganizacijaService.getOrganizacija("Lambda kod"), "admin"));
		users.add(new Korisnik("stegnuti", "pass", "Stefko", "Stegko", null, "superadmin"));
	}
	
	public static boolean checkLogin(String username, String password) {
		for(Korisnik u : users)
			if(u.getEmail().equals(username) && u.getLozinka().equals(password))
				return true;
		
		return false;
	}
	
	public static Korisnik getUser(String username) {
		for(Korisnik k : users) {
			if (k.getEmail().equals(username)) {
				return k;
			}
		}
		
		return null;
	}
	
	public static ArrayList<Korisnik> getUsers() {
		return users;
	}
	
	

}
