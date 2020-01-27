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
	
	public static Route verifyLogin = (req,res) -> {
		res.type("application/json");
		LoginInfo li = g.fromJson(req.body(), LoginInfo.class);
		if(UserService.checkLogin(li.username, li.password)) {
			Korisnik k = UserService.getUser(li.username);
			req.session(true).attribute("role", k.getUloga());
			req.session(false).attribute("username", k.getEmail());
			return "OK";
		} else {
			res.status(400);
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
	
	public static Route logout = (req,res) -> {
		res.type("application/json");
		req.session(false).invalidate();
		return g.toJson("Succssesfull logout!");
	};
	
	public static Route addUser = (req, res) -> {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
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
		return g.toJson(k);
	};
	
	private static void bindUser(Korisnik k, Organizacija o) {
		k.setOrganizacija(o);
		o.dodajKorisnika(k);
	}
	
	public static Route changeUser = (req, res) -> {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		Korisnik uk = UserService.updateUser(k.getEmail(), k); // updateUser vraca updateovanog usera
		return g.toJson(uk);
	};
	
	public static Route deleteUser = (req, res) -> {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		return g.toJson(UserService.deleteUser(k));
	};
}
