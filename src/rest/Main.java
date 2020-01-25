package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import controllers.OrganizacijaController;
import controllers.UserController;
import controllers.VMController;
import services.OrganizacijaService;
import services.UserService;
import services.VMService;

public class Main {

	public static void main(String[] args) throws IOException {
		port(8080);
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		OrganizacijaService.initialize();
		UserService.initialize();
		VMService.initialize();

		
		// USERS
		post("/rest/Users/login", UserController.verifyLogin);
		get("/rest/Users/info", UserController.getInfo);
		get("/rest/Users/getAll", UserController.getAll);
		get("/rest/Users/logout", UserController.logout);
		
		// ORGANIZATIONS
		get("rest/Organizations", OrganizacijaController.getAll);
		get("rest/Organizations/:ime", OrganizacijaController.getOne);
		post("rest/Organizations/", OrganizacijaController.insertOne);
		post("rest/Organizations/:ime", OrganizacijaController.updateOne);
		post("rest/setOrgImage/:ime/:fileName", OrganizacijaController.setImage);
		
		//VIRTUAL MACHINES
		get("rest/VirualMachines/getAll", VMController.getAll);

	}

}
