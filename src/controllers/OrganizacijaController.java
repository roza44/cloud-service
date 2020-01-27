package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import model.Organizacija;
import services.OrganizacijaService;
import spark.Route;

public class OrganizacijaController {
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		return g.toJson(OrganizacijaService.getOrganizacije());
	};
	
	public static Route getOne = (req, res) -> {
		res.type("application/json");
		Organizacija org = OrganizacijaService.getOrganizacija(req.params(":ime"));
		if (org == null) {
			res.status(404);
			return "Organization not found";
		} else {
			return g.toJson(org);
		}
	};
	
	public static Route insertOne = (req, res) -> {
		res.type("application/json");
		Organizacija org = g.fromJson(req.body(), Organizacija.class);
		
		OrganizacijaService.add(org);
		
		return org;
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		
		Organizacija org = g.fromJson(req.body(), Organizacija.class);
	
	    boolean ok = OrganizacijaService.update(org);
		
		if (!ok) {
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
		
		byte[] data = req.bodyAsBytes();
		boolean ok = OrganizacijaService.setImage(orgName, extension, data);
		
		if (!ok) {
			res.status(404);
			return "Organization not found!";
		}
		return g.toJson(OrganizacijaService.getOrganizacija(orgName));
	};
	
	public static Route getIds = (req, res) -> {
		res.type("application/json");
		return g.toJson(OrganizacijaService.getIds());
	};
	
}
