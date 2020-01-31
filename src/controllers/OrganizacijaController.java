package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.RecordNotFound;
import gson_classes.MBRequest;
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
	
	public static Route getMonthlyBill = (req, res) -> {
		res.type("application/json");
		
		MBRequest mbr;
		Date from, to;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			mbr = g.fromJson(req.body(), MBRequest.class);
			from = sdf.parse(mbr.from);
			to = sdf.parse(mbr.to);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		if (mbr.from == null) {
			res.status(400);
			return "Početni datum je obavezno polje";
		}
		
		if (mbr.to == null) {
			res.status(400);
			return "Krajnji datum je obavezno polje";
		}
		

		HashMap<String, Double> bill;
		Organizacija org = UserService.getUser(req.session().attribute("username")).getOrganizacija();
		try {			
			bill = OrganizacijaService.calculateMonthlyBill(org, from, to);
		} catch (RecordNotFound e) {
			res.status(400);
			return e.getMessage();
		}
		
		return g.toJson(bill);
	};
	
	public static Route getOne = (req, res) -> {
		res.type("application/json");
		Organizacija org;
		
		if (req.params(":ime") == null) {
			res.status(400);
			return "Query parametar 'ime' nedostaje";
		}
		
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
		
		if (req.params(":ime") == null) {
			res.status(400);
			return "Query parametar 'ime' nedostaje";
		}
		
		String role = req.session().attribute("role");
		
		Organizacija org = OrganizacijaService.getOrganizacija(req.params(":ime"));
		
		if (org == null) {
			res.status(404);
			return "Organization not found";
		} else {
			if (!role.equals("superadmin")) {			
				// Smes da vidis samo od svoje organizacije
				Korisnik k = UserService.getUser(req.session().attribute("username"));
				if (org != k.getOrganizacija()) {
					res.status(403);
					return "Zabranjen pristup spisku virtualnih masina za tudju organizaciju";
				}
			}
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
		Organizacija org;
		try {
			org = g.fromJson(req.body(), Organizacija.class);
		} catch(Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
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
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		return g.toJson(OrganizacijaService.getOrganizacija(org.getIme()));
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		
		Organizacija org;
		try {
			org = g.fromJson(req.body(), Organizacija.class);
		} catch(Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
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
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		return g.toJson(OrganizacijaService.getOrganizacija(org.getIme()));
	};
	
	public static Route setImage = (req, res) -> {
		res.type("application/json");
		
		if (req.params("fileName") == null) {
			res.status(400);
			return "Query parametar 'fileName' nedostaje";
		}
		
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
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
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
