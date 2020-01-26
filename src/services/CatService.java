package services;

import java.util.ArrayList;

import model.KategorijaVM;

public class CatService {
	
	private static ArrayList<KategorijaVM> categories;
	
	private static void simulate() {
		// Simulate
		categories = new ArrayList<KategorijaVM>();
		
		categories.add(new KategorijaVM("Kat1", 4, 8.5, 32));
		categories.add(new KategorijaVM("Kat2", 8, 20, 128));
		helpers.FileHandler.saveCats(categories);
		///
	}
	
	public static void initialize() {
		
		
		categories = helpers.FileHandler.loadCats();
		
	}
	
	
	public static ArrayList<KategorijaVM> getAll() {
		return categories;
	}
	
	public static boolean contains(KategorijaVM k) {
		return categories.contains(k);
	}
	
	public static void addCat(KategorijaVM k) {
		categories.add(k);
	}
	
	public static KategorijaVM getCat(String name) {
		for(KategorijaVM c : categories)
			if(c.getIme().equals(name))
				return c;
		
		return null;
	}
	
	public static KategorijaVM updateCat(String name, KategorijaVM k) {
		KategorijaVM realK = getCat(name);
		realK.setBrojJezgara(k.getBrojJezgara());
		realK.setGpuJezgra(k.getGpuJezgra());
		realK.setRam(k.getRam());
		return realK;
	}
	
	public static KategorijaVM deleteCat(KategorijaVM k) {
		KategorijaVM retVal = new KategorijaVM();
		for(KategorijaVM realK : categories)
			if(realK.equals(k)) {
				retVal = realK;
				categories.remove(realK);
				break;
			}
		
		return retVal;		
	}

}
