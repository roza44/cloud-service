package controllers;

import javax.swing.undo.CannotUndoException;

import com.google.gson.Gson;

import model.KategorijaVM;
import services.CatService;
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
		return g.toJson(k);
	};
	
	public static Route changeCat = (req, res) -> {
		res.type("application/json");
		KategorijaVM k = g.fromJson(req.body(), KategorijaVM.class);
		KategorijaVM uk = CatService.updateCat(k.getIme(), k);
		return g.toJson(uk);
	};
	
	public static Route deleteCat = (req,res) -> {
		res.type("application/json");
		KategorijaVM k = g.fromJson(req.body(), KategorijaVM.class);
		return g.toJson(CatService.deleteCat(k));
	};

}
