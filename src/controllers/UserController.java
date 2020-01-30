package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import gson_classes.EnsureInfo;
import gson_classes.LoginInfo;
import model.Korisnik;
import model.Organizacija;
import services.OrganizacijaService;
import services.UserService;
import spark.Route;
import spark.Session;

public class UserController {
	
	private static Gson g = new Gson();
	
	private static String validate(Korisnik k) {
		if (k.getEmail() == null) return "E-mail je obavezno polje";
		if (k.getEmail().equals("")) return "E-mail je obavezno polje";
		
		if (k.getIme() == null) return "Ime je obavezno polje";
		if (k.getIme().equals("")) return "Ime je obavezno polje";
		
		if (k.getLozinka() == null) return "Lozinka je obavezno polje";
		if (k.getLozinka().equals("")) return "Lozinka je obavezno polje";
		
		if (k.getPrezime() == null) return "Prezime je obavezno polje";
		if (k.getPrezime().equals("")) return "Prezime je obavezno polje";
		
		if (k.getUloga() == null) return "Uloga je obavezno polje";
		if (!(k.getUloga().equals("user") || k.getUloga().equals("admin") || k.getUloga().equals("superadmin"))) return "Uloga mora biti 'user', 'admin' ili 'superadmin'";
		
		return null;
	}
	
	public static Route verifyLogin = (req,res) -> {
		res.type("application/json");
		
		LoginInfo li;
		try {
			li = g.fromJson(req.body(), LoginInfo.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		if(UserService.checkLogin(li.username, li.password)) {
			Korisnik k = UserService.getUser(li.username);
			req.session(true).attribute("role", k.getUloga());
			req.session(false).attribute("username", k.getEmail());
			req.session(false).attribute("orgName", k.getOrganizacija().getIme());
			
			return "OK";
		} else {
			res.status(403);
			return "Invalid username or password!";
		}
	};
	
	public static Route getInfo = (req,res) -> {
		res.type("application/json");
		
		Session s = req.session(false);
		EnsureInfo ei = new EnsureInfo();
		if(s == null)
			return g.toJson(ei);
		if(s.attribute("role") == null)
			return g.toJson(ei);
		
		ei.isLogedIn = true;
		ei.role = s.attribute("role");
		ei.username = s.attribute("username");
		ei.orgName = s.attribute("orgName");
		return g.toJson(ei);
		
	};
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		Session s = req.session(false);
		ArrayList<Korisnik> users;
		
		if(s.attribute("role").equals("superadmin"))
			users = UserService.getUsers();
		else
			users = UserService.accessibleUsers(s.attribute("username"));
		
		return g.toJson(users);
	};
	
	public static Route getUser = (req, res) -> {
		res.type("application/json");
		if (req.params(":username") == null) {
			res.status(400);
			return "Query parametar 'username' nedostaje";
		}
		
		Korisnik k;
		if (req.session().attribute("role").equals("superadmin")) {
			k = UserService.getUser(req.params(":username"));	
		} else {
			k = UserService.getUser(req.session().attribute("username"));
		}
		return g.toJson(k);
	};
	
	public static Route logout = (req,res) -> {
		res.type("application/json");
		
		req.session(false).invalidate();
		return g.toJson("Succssesfull logout!");
	};
	
	public static Route addUser = (req, res) -> {
		res.type("application/json");
		
		if (req.params(":organization") == null) {
			res.status(400);
			return "Query parametar 'organization' nedostaje";
		}
		
		Korisnik k;
		try {			
			k = g.fromJson(req.body(), Korisnik.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		if(UserService.containsUser(k)) {
			res.status(400);
			return g.toJson("Korisnik sa unetim email-om vec postoji!");
		}
		
		Session s = req.session(false);
		
		//Set the organization of user
		if(s.attribute("role").equals("admin")) {
			Korisnik tempK = UserService.getUser(s.attribute("username"));
			Organizacija o1 = tempK.getOrganizacija();
			bindUser(k, o1);
		}
		else {
			Organizacija o2 = OrganizacijaService.getOrganizacija(req.params(":organization"));
			bindUser(k, o2);
		}
		
		res.status(200);
		UserService.addUser(k);
		
		//Save changes
		helpers.FileHandler.saveUsers(UserService.getUsers());
		
		return g.toJson(k);
	};
	
	private static void bindUser(Korisnik k, Organizacija o) {
		k.setOrganizacija(o);
		o.dodajKorisnika(k);
	}
	
	public static Route changeUser = (req, res) -> {
		res.type("application/json");
		
		if (req.params(":organization") == null) {
			res.status(400);
			return "Query parametar 'organization' nedostaje";
		}
		
		Korisnik k;
		try {			
			k = g.fromJson(req.body(), Korisnik.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		// Validacija
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		Korisnik uk = UserService.updateUser(k.getEmail(), k); // updateUser vraca updateovanog usera
		
		if(req.session(false).attribute("role").equals("superadmin")) {
			Organizacija oldOrg = uk.getOrganizacija();
			oldOrg.removeUser(uk);
			
			Organizacija newOrg = OrganizacijaService.getOrganizacija(req.params(":organization"));
			bindUser(uk, newOrg);
		}
		
		//Save changes
		helpers.FileHandler.saveUsers(UserService.getUsers());
		
		return g.toJson(uk);
	};
	
	public static Route deleteUser = (req, res) -> {
		res.type("application/json");
		
		if (req.params(":email") == null) {
			res.status(400);
			return "Query parametar 'email' nedostaje";
		}
		
		Korisnik k = UserService.getUser(req.params(":email"));
		
		if (k == null) {
			res.status(404);
			return "Korisnik nije pronadjen";
		}
		
		Korisnik realK = UserService.deleteUser(k);
		realK.getOrganizacija().removeUser(realK);
		
		//Save changes
		helpers.FileHandler.saveUsers(UserService.getUsers());
		
		return g.toJson(realK);
	};
	
	public static Route updateProfile = (req, res) -> {
		res.type("application/json");
		Korisnik k;
		try {			
			k = g.fromJson(req.body(), Korisnik.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		Korisnik uk = UserService.updateUser(k.getEmail(), k);
		
		//Save changes
		helpers.FileHandler.saveUsers(UserService.getUsers());
		
		return g.toJson(uk);
	};
}
