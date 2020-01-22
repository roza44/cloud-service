package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import controllers.UserController;
import services.UserService;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		port(8080);
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		UserService.initialize();
		
		post("/rest/Users/login", UserController.verifyLogin);
		get("/rest/Users/info", UserController.getInfo);
		get("/rest/Users/logout", UserController.logout);

	}

}
