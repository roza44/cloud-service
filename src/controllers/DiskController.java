package controllers;

import java.util.ArrayList;

import com.google.gson.Gson;

import model.Disk;
import services.DiskService;
import services.VMService;
import services.OrganizacijaService;
import services.UserService;
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
	
	public static Route insertOne = (req, res) -> {
		res.type("application/json");
		try {
			Disk d = g.fromJson(req.body(), Disk.class);
			
			DiskService.add(d);
			
			return g.toJson(d);
		} catch (exceptions.RecordIDAlreadyTaken e) {
			res.status(400);
			return e.getMessage();
		}
	};
	
	public static Route updateOne = (req, res) -> {
		res.type("application/json");
		try {
			Disk d = g.fromJson(req.body(), Disk.class);
			
			DiskService.update(d);
			return g.toJson(DiskService.getDisk(d.getIme()));
		} catch (exceptions.RecordNotFound e) {
			res.status(400);
			return e.getMessage();
		}
	};
	
	public static Route deleteOne = (req, res) -> {
		res.type("application/json");
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
		
		String param = req.params(":organization");
		ArrayList<String> retVal = new ArrayList<String>();
		Session s = req.session(false);
		
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
