package controllers;

import com.google.gson.Gson;

import gson_classes.LoginInfo;
import model.Korisnik;
import services.UserService;

import spark.Route;

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
			return "Invalid username or password!";
		}
	};
	
	public static Route getInfo = (req,res) -> {
		res.type("application/json");
		String role = req.session(false).attribute("role");
		return role;
	};
}
