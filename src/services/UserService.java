package services;

import java.util.ArrayList;

import model.Korisnik;
import model.Organizacija;

public class UserService {
	
	private static ArrayList<Korisnik> users;
	
	public static void initialize() {
		users = new ArrayList<Korisnik>();
		
		Korisnik user = new Korisnik("roza44", "pass", "Jovan", "Bodroza",
						new Organizacija("Organizacija", "Probna organizacija"), Korisnik.Uloge.ADMIN);
		
		users.add(user);
	}
	
	public static boolean exists(String username, String password) {
		for(Korisnik u : users)
			if(u.getEmail().equals(username) && u.getLozinka().equals(password))
				return true;
		
		return false;
	}

}
