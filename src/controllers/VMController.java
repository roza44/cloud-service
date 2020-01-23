package controllers;

import com.google.gson.Gson;

import services.VMService;
import spark.Route;

public class VMController {
	
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		return g.toJson(VMService.getVirtualMachines());
	};
	

}
