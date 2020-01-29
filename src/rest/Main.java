package rest;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;

import controllers.CatController;
import controllers.DiskController;
import controllers.OrganizacijaController;
import controllers.UserController;
import controllers.VMController;
import services.CatService;
import services.DiskService;
import services.OrganizacijaService;
import services.UserService;
import services.VMService;

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
	
	public static void main(String[] args) throws IOException {
		port(8080);
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		// Load all data and initialize
		initDatabase(false);
		
		// USERS
		post("/rest/Users/login", UserController.verifyLogin);
		get("/rest/Users/info", UserController.getInfo);
		get("/rest/Users/getAll", UserController.getAll);
		get("/rest/Users/logout", UserController.logout);
		get("/rest/Users/getUser/:username", UserController.getUser);
		post("/rest/Users/addUser/:organization", UserController.addUser);
		post("/rest/Users/changeUser/:organization", UserController.changeUser);
		post("/rest/Users/deleteUser", UserController.deleteUser);
		post("/rest/Users/updateProfile", UserController.updateProfile);
		
		// ORGANIZATIONS
		get("rest/Organizations/getIds", OrganizacijaController.getIds);
		get("rest/Organizations", OrganizacijaController.getAll);
		get("rest/Organizations/:ime", OrganizacijaController.getOne);
		get("rest/Organizations/vms/:ime", OrganizacijaController.getVMs);
		post("rest/Organizations/", OrganizacijaController.insertOne);
		post("rest/Organizations/:ime", OrganizacijaController.updateOne);
		post("rest/setOrgImage/:ime/:fileName", OrganizacijaController.setImage);
		
		// DISKS
		get("rest/Disks/getIds/:organization", DiskController.getIds);
		get("rest/Disks/", DiskController.getAll);
		post("rest/Disks/", DiskController.insertOne);
		post("rest/Disks/:ime", DiskController.updateOne);
		delete("rest/Disks/:ime", DiskController.deleteOne);
		
		// VIRTUAL MACHINES
		get("/rest/VirualMachines/getAll", VMController.getAll);
		get("/rest/VirualMachines/getDiscsIds/:vm", VMController.getDiscsIds);
		post("/rest/VirualMachines/addVM", VMController.addVM);
		post("/rest/VirualMachines/changeVM", VMController.changeVM);
		delete("/rest/VirualMachines/deleteVM/:vm", VMController.deleteVM);
		
		// CATEGORIES
		get("/rest/Categories/getIds", CatController.getIds);
		get("/rest/Categories/getAll", CatController.getAll);
		post("/rest/Categories/addCat", CatController.addCat);
		post("/rest/Categories/changeCat", CatController.changeCat);
		post("/rest/Categories/deleteCat", CatController.deleteCat);
	}

}
