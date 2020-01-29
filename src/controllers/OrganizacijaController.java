package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import model.Korisnik;
import model.Organizacija;
import services.OrganizacijaService;
import services.UserService;
import spark.Route;

public class OrganizacijaController {
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		if (req.session().attribute("role").equals("superadmin")) {
			// Smes sve da vidis
			return g.toJson(OrganizacijaService.getOrganizacije());
		} else {
			// Smes samo svoju da vidis
			return g.toJson(OrganizacijaService.getOrganizacije(req.session().attribute("username")));
		}
	};
	
	public static Route getOne = (req, res) -> {
		res.type("application/json");
		Organizacija org;
		
		if (req.session().attribute("role").equals("superadmin")) {
			// Smes koju god da vidis
			org = OrganizacijaService.getOrganizacija(req.params(":ime"));
		} else {			
			// Smes samo svoju da vidis
			org = OrganizacijaService.getOrganizacije(req.session().attribute("username")).get(0);
		}
		
		
		if (org == null) {
			res.status(404);
			return "Organization not found";
		} else {
			return g.toJson(org);
		}
	};
	
	public static Route getVMs = (req, res) -> {
		res.type("application/json");
		String role = req.session().attribute("role");
		
		// Ako nisi superadmin ili admin, zabranjeno
		if (!(role.equals("superadmin") || role.equals("admin"))) {
			res.status(403);
			return "Zabranjen pristup spisku virtualnih masina";
		}
		
		Organizacija org = OrganizacijaService.getOrganizacija(req.params(":ime"));
		
		// Ako si admin, smes da vidis samo od svoje organizacije
		Korisnik k = UserService.getUser(req.session().attribute("username"));
		if (org != k.getOrganizacija()) {
			res.status(403);
			return "Zabranjen pristup spisku virtualnih masina za tudju organizaciju";
		}
		
		if (org == null) {
			res.status(404);
			return "Organization not found";
		} else {
			return g.toJson(org.getResursi());
		}
	};
	
	private static String validate(Organizacija org) {
		if (org.getIme() == null) return "Ime je obavezno polje kod organizacije!";
		return null;
	}
	
	public static Route insertOne = (req, res) -> {
		res.type("application/json");
		
		if (!req.session().attribute("role").equals("superadmin")) {
			res.status(403);
			return "Organizacije sme da dodaje samo superadmin";
		}
		
		Organizacija org = g.fromJson(req.body(), Organizacija.class);
		
		// Validacija
		String validateResult = validate(org);
		if (validateResult != null) {
			// Nesto ne valja
			res.status(400);
			return validateResult;
		}
		
		try {
			OrganizacijaService.add(org);
		} catch (exceptions.RecordIDAlreadyTaken e) {
			res.status(400);
			return "Organizacija s prosledjenim imenom vec postoji!";
		}
		
		return g.toJson(OrganizacijaService.getOrganizacija(org.getIme()));
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		
		Organizacija org = g.fromJson(req.body(), Organizacija.class);
		
		String role = req.session().attribute("role");
		// Samo superadmin ili admin te organizacije
		if (!role.equals("superadmin") && !role.equals("admin")) {
			res.status(403);
			return "Organizaciju sme da menja samo admin ili superadmin";
		}
		
		if (role.equals("admin")) {			
			Korisnik k = UserService.getUser(req.session().attribute("username"));
			if (!k.getOrganizacija().getIme().equals(org.getIme())) {
				res.status(403);
				return "Admin organizacije sme samo nju da menja";
			}
		}
		
		// Validacija
		String validateResult = validate(org);
		if (validateResult != null) {
			// Nesto ne valja
			res.status(400);
			return validateResult;
		}
		
		try {
			OrganizacijaService.update(org);
		} catch (exceptions.RecordNotFound e) {
			res.status(404);
			return "Organization not found!";
		}
		
		return g.toJson(OrganizacijaService.getOrganizacija(org.getIme()));
	};
	
	public static Route setImage = (req, res) -> {
		res.type("application/json");
		String fileName = req.params("fileName");
		String extension = fileName.split("\\.")[1];
		String orgName = req.params("ime");
		
		String role = req.session().attribute("role");
		// Samo superadmin ili admin te organizacije
		if (!role.equals("superadmin") && !role.equals("admin")) {
			res.status(403);
			return "Sliku organizacije sme da menja samo admin ili superadmin";
		}
		
		if (role.equals("admin")) {			
			Korisnik k = UserService.getUser(req.session().attribute("username"));
			if (!k.getOrganizacija().getIme().equals(orgName)) {
				res.status(403);
				return "Admin organizacije sme samo da menja sliku te organizacije";
			}
		}
		
		byte[] data = req.bodyAsBytes();
		
		try {			
			OrganizacijaService.setImage(orgName, extension, data);
		} catch (exceptions.RecordNotFound e) {			
			res.status(404);
			return "Organization not found!";
		}

		return g.toJson(OrganizacijaService.getOrganizacija(orgName));
	};
	
	public static Route getIds = (req, res) -> {
		res.type("application/json");
		
		if (req.session().attribute("role").equals("superadmin")) {
			// Smes sve da vidis
			return g.toJson(OrganizacijaService.getIds());
		} else {
			// Smes samo svoju da vidis
			return g.toJson(OrganizacijaService.getIdsForUsername(req.session().attribute("username")));
		}
		
		
	};
	
}
