package services;

import java.util.ArrayList;

import model.KategorijaVM;

public class CatService {
	
	private static ArrayList<KategorijaVM> categories;
	
	public static void initialize() {
		categories = new ArrayList<KategorijaVM>();
		
		KategorijaVM kvm1 = new KategorijaVM("Linux", 2, 2500, 10);
		KategorijaVM kvm2 = new KategorijaVM("Windows", 4, 3000, 20);
		
		categories.add(kvm1);
		categories.add(kvm2);
	}
	
	public static ArrayList<KategorijaVM> getCategories() {
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
