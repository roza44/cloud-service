package controllers;

import com.google.gson.Gson;

import model.KategorijaVM;
import services.CatService;
import services.VMService;
import spark.Request;
import spark.Route;

public class CatController {
	
	private static Gson g = new Gson();
	
	private static boolean roleNotSuperadmin(Request req) {
		return !req.session().attribute("role").equals("superadmin");
	}
	
	private static String validate(KategorijaVM k) {
		if (k.getIme() == null) return "Ime je obavezno polje";
		if (k.getIme().equals("")) return "Ime je obavezno polje";
		return null;
	}

	public static Route getAll = (req, res) -> {
		res.type("application/json");
		
		if (roleNotSuperadmin(req)) {
			res.status(403);
			return "Samo superadmin sme da radi sa kategorijama";
		}
		
		return g.toJson(CatService.getAll());
	};
	
	
	public static Route addCat = (req, res) -> {
		res.type("application/json");
		
		if (roleNotSuperadmin(req)) {
			res.status(403);
			return "Samo superadmin sme da radi sa kategorijama";
		}
		
		KategorijaVM k;
		try {
			k = g.fromJson(req.body(), KategorijaVM.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		if(CatService.contains(k)) {
			res.status(400);
			return "Kategorija sa unetim imenom vec postoji";
		}
		
		CatService.addCat(k);
		helpers.FileHandler.saveCats(CatService.getAll());
		
		return g.toJson(k);
	};
	
	public static Route changeCat = (req, res) -> {
		res.type("application/json");
		
		if (roleNotSuperadmin(req)) {
			res.status(403);
			return "Samo superadmin sme da radi sa kategorijama";
		}
		
		KategorijaVM k;
		try {
			k = g.fromJson(req.body(), KategorijaVM.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		
		KategorijaVM uk = CatService.updateCat(k.getIme(), k);
		helpers.FileHandler.saveCats(CatService.getAll());
		return g.toJson(uk);
	};
	
	public static Route deleteCat = (req,res) -> {
		res.type("application/json");
		
		if (roleNotSuperadmin(req)) {
			res.status(403);
			return "Samo superadmin sme da radi sa kategorijama";
		}
		
		KategorijaVM k;
		try {
			k = g.fromJson(req.body(), KategorijaVM.class);
		} catch (Exception e) {
			res.status(400);
			return "Los format zahteva";
		}
		
		String result = validate(k);
		if (result != null) {
			res.status(400);
			return result;
		}
		
		if(VMService.catInUser(k)) {
			res.status(403);
			return g.toJson("Kategorija je u upotrebi!");
		}
		
		KategorijaVM delK = CatService.deleteCat(k);
		helpers.FileHandler.saveCats(CatService.getAll());
		return g.toJson(delK);
	};
	
	public static Route getIds = (req, res) -> {
		res.type("application/json");
		
		
		return g.toJson(CatService.getIds());
	};

}
