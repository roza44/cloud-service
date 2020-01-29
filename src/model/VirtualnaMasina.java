package model;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class VirtualnaMasina {
	
	private String ime;
	private KategorijaVM kategorija;
	private Organizacija organizacija;
	
	private transient ArrayList<Disk> diskovi;
	private ArrayList<VMActivity> aktivnosti;
	
	public Organizacija getOrganizacija() {
		return organizacija;
	}
	
	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}
	
	private VirtualnaMasina() {
		this.diskovi = new ArrayList<Disk>();
		this.aktivnosti = new ArrayList<VMActivity>();
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (obj == this) { 
	            return true; 
	     }  
	     
		 if (!(obj instanceof Korisnik)) { 
	         return false; 
	     }  
	        
		 VirtualnaMasina vm = (VirtualnaMasina) obj; 
	            
	     return this.ime.equals(vm.ime);
	}
	
	public void detachDiscs() {
		for(Disk d : this.diskovi) {
			d.setVm(null);
		}
		
		this.diskovi = new ArrayList<Disk>();
	}
	
	public ArrayList<VMActivity> getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(ArrayList<VMActivity> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	public VirtualnaMasina(String ime, KategorijaVM kategorija) {
		this();
		this.ime = ime;
		this.kategorija = kategorija;
	}
	
	public void addActivity(boolean turnedOn) {
		this.aktivnosti.add(new VMActivity(turnedOn));
	}
	
	public String getIme() {
		return ime;
	}
	
	public void setIme(String ime) {
		this.ime = ime;
	}
	
	public KategorijaVM getKategorija() {
		return kategorija;
	}
	
	public void setKategorija(KategorijaVM kategorija) {
		this.kategorija = kategorija;
	}
	
	public ArrayList<Disk> getDiskovi() {
		return diskovi;
	}
	
	public void dodajDisk(Disk disk) {
		this.diskovi.add(disk);
	}
	
	public void obrisiDisk(Disk disk) {
		this.diskovi.remove(disk);
	}
	
	public boolean getActivity() {
		int last = this.aktivnosti.size() - 1;
		if(last == -1) return false;
		
		return this.aktivnosti.get(last).turnedOn;
	}

}
