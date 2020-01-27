package controllers;

import com.google.gson.Gson;

import model.Disk;
import services.DiskService;
import spark.Route;

public class DiskController {
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		return g.toJson(DiskService.getDisks());
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
		} catch (exceptions.RecordIsReferenced e) {
			res.status(400);
			return e.getMessage();
		}
	};
}
