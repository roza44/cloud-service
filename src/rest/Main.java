package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		port(8080);
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		post("/rest/login", (req, res) -> {return "radi";});

	}

}
