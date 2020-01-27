package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;

import com.google.gson.Gson;

import gson_classes.LoginInfo;
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
		
		try {
			OrganizacijaService.add(org);			
		} catch (exceptions.RecordIDAlreadyTaken e) {			
			res.status(400);
			return "Organizacija s prosledjenim imenom vec postoji!";
		}
		
		return org;
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		
		Organizacija org = g.fromJson(req.body(), Organizacija.class);
		
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
		
		byte[] data = req.bodyAsBytes();
		
		try {			
			OrganizacijaService.setImage(orgName, extension, data);
		} catch (exceptions.RecordNotFound e) {			
			res.status(404);
			return "Organization not found!";
		}

		return g.toJson(OrganizacijaService.getOrganizacija(orgName));
	};
	
}
