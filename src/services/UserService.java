package services;

import java.util.ArrayList;

import model.Korisnik;
import model.Organizacija;

public class UserService {
	
	private static ArrayList<Korisnik> users;
	
	public static void initialize() {
		users = new ArrayList<Korisnik>();
		
		Korisnik user = new Korisnik("roza44", "pass", "Jovan", "Bodroza",
						new Organizacija("Organizacija", "Probna organizacija"), "admin");
		
		users.add(user);
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
	
	

}
