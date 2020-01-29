package controllers;

import com.google.gson.Gson;

import model.Disk;
import services.DiskService;
import services.VMService;
import spark.Route;

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
			if (d.getVm() != null) {
				d.setVm(VMService.getVirtualMachine(d.getVm().getIme()));
			}
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
			if (d.getVm() != null) {
				d.setVm(VMService.getVirtualMachine(d.getVm().getIme()));
			}
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
}
