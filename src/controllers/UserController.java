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
		return "Succssesfull logout!";
	};
}
