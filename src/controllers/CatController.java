package controllers;

import com.google.gson.Gson;

import model.KategorijaVM;
import services.CatService;
import services.VMService;
import spark.Route;

public class CatController {
	
	private static Gson g = new Gson();
	
	public static Route getAll = (req, res) -> {
		res.type("application/json");
		return g.toJson(CatService.getAll());
	};
	
	public static Route addCat = (req, res) -> {
		res.type("application/json");
		KategorijaVM k = g.fromJson(req.body(), KategorijaVM.class);
		if(CatService.contains(k)) {
			res.status(400);
			return g.toJson("Kategorija sa unetim imenom vec postoji");
		}
		
		res.status(200);
		CatService.addCat(k);
		helpers.FileHandler.saveCats(CatService.getAll());
		return g.toJson(k);
	};
	
	public static Route changeCat = (req, res) -> {
		res.type("application/json");
		KategorijaVM k = g.fromJson(req.body(), KategorijaVM.class);
		KategorijaVM uk = CatService.updateCat(k.getIme(), k);
		helpers.FileHandler.saveCats(CatService.getAll());
		return g.toJson(uk);
	};
	
	public static Route deleteCat = (req,res) -> {
		res.type("application/json");
		KategorijaVM k = g.fromJson(req.body(), KategorijaVM.class);
		
		if(VMService.catInUser(k)) {
			res.status(403);
			return g.toJson("Kategorija je u upotrebi!");
		}
		
		KategorijaVM delK = CatService.deleteCat(k);
		helpers.FileHandler.saveCats(CatService.getAll());
		return g.toJson(delK);
	};

}