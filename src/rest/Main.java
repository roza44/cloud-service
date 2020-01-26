package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import controllers.CatController;
import controllers.OrganizacijaController;
import controllers.UserController;
import controllers.VMController;
import services.CatService;
import services.DiskService;
import services.OrganizacijaService;
import services.UserService;
import services.VMService;

public class Main {

	static void initDatabase() {
		// Load disks
		DiskService.initialize();
		// Load categories
		CatService.initialize();
		// Load VMs and connect references to categories and disks
		VMService.initialize();
		// Load users
		UserService.initialize();
		// Load organizations and connect refs to users
		OrganizacijaService.initialize();
	}
	
	public static void main(String[] args) throws IOException {
		port(8080);
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		// Load all data and initialize
		initDatabase();
		
		// USERS
		post("/rest/Users/login", UserController.verifyLogin);
		get("/rest/Users/info", UserController.getInfo);
		get("/rest/Users/getAll", UserController.getAll);
		get("/rest/Users/logout", UserController.logout);
		post("/rest/Users/addUser", UserController.addUser);
		post("/rest/Users/changeUser", UserController.changeUser);
		post("/rest/Users/deleteUser", UserController.deleteUser);
		
		// ORGANIZATIONS
		get("rest/Organizations", OrganizacijaController.getAll);
		get("rest/Organizations/:ime", OrganizacijaController.getOne);
		post("rest/Organizations/", OrganizacijaController.insertOne);
		post("rest/Organizations/:ime", OrganizacijaController.updateOne);
		post("rest/setOrgImage/:ime/:fileName", OrganizacijaController.setImage);
		
		// VIRTUAL MACHINES
		get("rest/VirualMachines/getAll", VMController.getAll);
		
		// CATEGORIES
		get("/rest/Categories/getAll", CatController.getAll);
		post("/rest/Categories/addCat", CatController.addCat);
		post("/rest/Categories/changeCat", CatController.changeCat);
		post("/rest/Categories/deleteCat", CatController.deleteCat);

	}

}
