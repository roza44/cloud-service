package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import model.Disk;
import model.Korisnik;
import model.Organizacija;
import services.DiskService;
import services.VMService;
import services.OrganizacijaService;
import services.UserService;
import spark.Response;
import spark.Route;
import spark.Session;

public class DiskController {
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		if (req.session().attribute("role").equals("superadmin")) {
			return g.toJson(DiskService.getDisks());
		} else {
			return g.toJson(DiskService.getDisks(req.session().attribute("username")));
		}
	};
	
	private static String validate(Disk d) {
		if (d.getIme() == null) return "Ime je obavezno polje";
		if (d.getIme().equals("")) return "Ime je obavezno polje";
		if (d.getKapacitet() == 0) return "Kapacitet je obavezno polje";
		if (d.getTip() == null) return "Tip je obavezno polje";
		if (d.getTip().equals("")) return "Tip je obavezno polje";
		if (d.getOrganizacija() == null) return "Organizacija je obavezno polje";
		if (d.getOrganizacija().getIme() == null || d.getOrganizacija().getOpis() == null) return "Organizacija vezana za disk korumpirana"; 
		return null;
	}
	
	private static String checkEditPermission(String username, Response res, Disk d) {
		Korisnik k = UserService.getUser(username);
		
		// Jedino superadmin i admin smeju da dodaju disk
		if(!k.getUloga().equals("superadmin") && !k.getUloga().equals("admin")) {
			res.status(403);
			return "Samo superadmin i admin smeju da upravljaju diskovima";
		}
		
		// Admin sme samo svojoj
		if (k.getUloga().equals("admin")) {			
			Organizacija org = OrganizacijaService.getOrganizacija(d.getOrganizacija().getIme());
			
			if (k.getOrganizacija() != org) {
				res.status(403);
				return "Admin ima pristup samo sopstvenoj organizaciji";
			}
		}
		
		// Superadmin sme da dodaje bilo kojoj org
		return null;
	}
	
	public static Route insertOne = (req, res) -> {
		res.type("application/json");
		
		Disk d;
		try {
			d = g.fromJson(req.body(), Disk.class);
		} catch(Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		
		try {
			
			// Validacija
			String validateResult = validate(d);
			if (validateResult != null) {
				res.status(400);
				return validateResult;
			}
			
			// Kontrola pristupa
			String permError = checkEditPermission(req.session().attribute("username"), res, d);
			if (permError != null) {
				return permError;
			}
			
			// Izvrsenje operacije
			DiskService.add(d);
			
			return g.toJson(d);
		} catch (exceptions.RecordIDAlreadyTaken e) {
			res.status(400);
			return e.getMessage();
		}
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		
		if (req.params("ime") == null) {
			res.status(400);
			return "Query parametar 'ime' nedostaje";
		}
		
		Disk d;
		try {
			d = g.fromJson(req.body(), Disk.class);
		} catch(Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		try {
			// Validacija
			String validateResult = validate(d);
			if (validateResult != null) {
				res.status(400);
				return validateResult;
			}
			
			// Kontrola pristupa
			String permError = checkEditPermission(req.session().attribute("username"), res, d);
			if (permError != null) {
				return permError;
			}
			
			// Izvrsenje operacije
			DiskService.update(d);
			
			return g.toJson(DiskService.getDisk(d.getIme()));
		} catch (exceptions.RecordNotFound e) {
			res.status(400);
			return e.getMessage();
		}
	};
	
	public static Route deleteOne = (req, res) -> {
		res.type("application/json");
		
		if (req.params("ime") == null) {
			res.status(400);
			return "Query parametar 'ime' nedostaje";
		}
		
		// Samo superadmin sme
		if (!req.session().attribute("role").equals("superadmin")) {
			res.status(403);
			return "Samo superadmin sme da brise diskove";
		}
		
		try {
			DiskService.delete(req.params("ime"));
			return req.params("ime");
		} catch (exceptions.RecordNotFound e) {
			res.status(400);
			return e.getMessage();
		}
	};
	
	public static Route getIds = (req, res) -> {
		res.type("application/json");
		
		if (req.params(":organization") == null) {
			res.status(400);
			return "Query parametar 'organization' nedostaje";
		}
		
		String param = req.params(":organization");
		ArrayList<String> retVal = new ArrayList<String>();
		Session s = req.session(false);
		
		// Kontrola pristupa
		if(s.attribute("role").equals("admin"))
			retVal = extractIds(UserService.getUser(s.attribute("username")).getOrganizacija().getDiskovi());
		else
			retVal = extractIds(OrganizacijaService.getOrganizacija(param).getDiskovi());
		
		return g.toJson(retVal);
	};
	
	private static ArrayList<String> extractIds(ArrayList<Disk> diskovi) {
		ArrayList<String> retVal = new ArrayList<String>();
		
		for(Disk d : diskovi)
			if(d.getVm() == null)
				retVal.add(d.getIme());
		
		return retVal;
		
	}
}
