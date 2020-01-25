package controllers;

import com.google.gson.Gson;

import gson_classes.EnsureInfo;
import gson_classes.LoginInfo;
import model.Korisnik;
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
		return g.toJson(UserService.getUsers());
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
		
		res.status(200);
		UserService.addUser(k);
		return g.toJson(k);
	};
	
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
