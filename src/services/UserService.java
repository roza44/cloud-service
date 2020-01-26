package services;

import java.util.ArrayList;

import model.Korisnik;
import model.Organizacija;

public class UserService {
	
	private static ArrayList<Korisnik> users;
	
	private static void simulate() {
		// Simulate
		users = new ArrayList<Korisnik>();
		
		users.add(new Korisnik("roza44", "pass", "Joca", "Boca", null, "admin"));
		users.add(new Korisnik("stegnuti", "pass", "Stefko", "Stegko", null, "superadmin"));
		
		helpers.FileHandler.saveUsers(users);
		///
	}
	
	public static void initialize() {
		users = helpers.FileHandler.loadUsers();
	}
	
	public static void addUser(Korisnik k) {
		users.add(k);
	}
	
	public static boolean containsUser(Korisnik k) {
		return users.contains(k);
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
	
	public static Korisnik updateUser(String email, Korisnik newUser) {
		Korisnik k = getUser(email);
		k.setIme(newUser.getIme());
		k.setPrezime(newUser.getPrezime());
		k.setLozinka(newUser.getLozinka());
		return k;
	}
	
	public static Korisnik deleteUser(Korisnik delK) {
		Korisnik retVal = new Korisnik();
		for(Korisnik k : users)
			if(k.equals(delK)) {
				retVal = k;
				users.remove(k);
				break;
			}
		
		return retVal;
	}

}
