package rest;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controllers.CatController;
import controllers.DiskController;
import controllers.OrganizacijaController;
import controllers.UserController;
import controllers.VMController;
import gson_classes.MBRequest;
import model.KategorijaVM;
import model.Organizacija;
import model.VMActivity;
import model.VirtualnaMasina;
import services.CatService;
import services.DiskService;
import services.OrganizacijaService;
import services.UserService;
import services.VMService;
import spark.Filter;
import spark.Route;

public class Main {

	static void initDatabase(boolean testMode) {
		// Load disks
		DiskService.initialize(testMode);
		// Load categories
		CatService.initialize(testMode);
		// Load VMs and connect references to categories and disks
		VMService.initialize(testMode);
		// Load users
		UserService.initialize(testMode);
		// Load organizations and connect refs to users
		OrganizacijaService.initialize(testMode);
	}
	
	static Filter authCheck = (req, res) -> {
		if (req.session(false) == null) {
			halt(401, "Prvo se prijavite");
		}
		
	};
	
	static void monthlyBillTest() {
		VirtualnaMasina vm1 = new VirtualnaMasina("VM1", new KategorijaVM("Kat1", 4, 8.5, 32));
		vm1.setOrganizacija(new Organizacija("Org1", "Opis1"));
		vm1.addActivity(true, new Date(2020, 1, 10, 12, 0));
		vm1.addActivity(false, new Date(2020, 1, 10, 15, 0));
		vm1.addActivity(true, new Date(2020, 1, 10, 16, 0));
		vm1.addActivity(false, new Date(2020, 1, 11, 9, 0));
		vm1.addActivity(true, new Date(2020, 1, 11, 11, 0));
		
		Date from = new Date(2020, 1, 11, 7, 0);
		Date to = new Date(2020, 1, 11, 12, 0);
		System.out.println(vm1.getActiveHours(from, to));
		
	}
	
	
	public static void main(String[] args) throws IOException {
		port(8080);
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		// Load all data and initialize
		initDatabase(false/* Simulation mode flag */);
		
		// USERS
		get("/rest/userInfo", UserController.getInfo);
		post("/rest/login", UserController.verifyLogin);
		
		before("/rest/Users/*", authCheck);
		get("/rest/Users/getAll", UserController.getAll);
		get("/rest/Users/logout", UserController.logout);
		get("/rest/Users/getUser/:username", UserController.getUser);
		post("/rest/Users/addUser/:organization", UserController.addUser);
		post("/rest/Users/changeUser/:organization", UserController.changeUser);
		post("/rest/Users/deleteUser/:email", UserController.deleteUser);
		post("/rest/Users/updateProfile", UserController.updateProfile);
		
		// ORGANIZATIONS
		before("rest/Organizations/*", authCheck);
		get("rest/Organizations/getIds", OrganizacijaController.getIds);
		get("rest/Organizations", OrganizacijaController.getAll);
		get("rest/Organizations/:ime", OrganizacijaController.getOne);
		get("rest/Organizations/vms/:ime", OrganizacijaController.getVMs);
		post("rest/Organizations/monthlyBill", OrganizacijaController.getMonthlyBill);
		post("rest/Organizations/", OrganizacijaController.insertOne);
		post("rest/Organizations/:organization", OrganizacijaController.updateOne);
		post("rest/Organizations/setImage/:ime/:fileName", OrganizacijaController.setImage);
		
		// DISKS
		before("rest/Disks/*", authCheck);
		get("rest/Disks/getIds/:organization", DiskController.getIds);
		get("rest/Disks/", DiskController.getAll);
		post("rest/Disks/", DiskController.insertOne);
		post("rest/Disks/:ime", DiskController.updateOne);
		delete("rest/Disks/:ime", DiskController.deleteOne);
		
		// VIRTUAL MACHINES
		before("/rest/VirualMachines/*", authCheck);
		get("/rest/VirualMachines/getAll", VMController.getAll);
		get("/rest/VirualMachines/getDiscsIds/:vm", VMController.getDiscsIds);
		post("/rest/VirualMachines/addVM", VMController.addVM);
		post("/rest/VirualMachines/changeVM", VMController.changeVM);
		delete("/rest/VirualMachines/deleteVM/:vm", VMController.deleteVM);
		
		// CATEGORIES
		before("/rest/Categories/*", authCheck);
		get("/rest/Categories/getIds", CatController.getIds);
		get("/rest/Categories/getAll", CatController.getAll);
		post("/rest/Categories/addCat", CatController.addCat);
		post("/rest/Categories/changeCat", CatController.changeCat);
		post("/rest/Categories/deleteCat", CatController.deleteCat);
	}

}
