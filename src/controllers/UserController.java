package controllers;

import com.google.gson.Gson;

import model.LoginInfo;
import services.UserService;

import spark.Route;

public class UserController {
	
	private static Gson g = new Gson();
	
	public static Route verifyLogin = (req,res) -> {
		res.type("application/json");
		LoginInfo li = g.fromJson(req.body(), LoginInfo.class);
		if(UserService.exists(li.username, li.password))
			return "OK";
		else
			return "Invalid username or password!";	
	};
}
