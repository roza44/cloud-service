package controllers;

import com.google.gson.Gson;

import gson_classes.LoginInfo;
import model.Korisnik;
import model.Organizacija;
import services.OrganizacijaService;
import services.UserService;
import spark.Route;

public class OrganizacijaController {
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		return g.toJson(OrganizacijaService.getOrganizacije());
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		String nameToFind = req.params("ime");
		Organizacija newObj = g.fromJson(req.body(), Organizacija.class);
		OrganizacijaService.updateOrInsert(nameToFind, newObj);
		
		res.status(200);
		return "OK";
	};
	
}
