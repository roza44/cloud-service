package model;

import java.util.ArrayList;

public class VirtualnaMasina {
	
	private String ime;
	private KategorijaVM kategorija;
	private ArrayList<Disk> diskovi;
	
	public VirtualnaMasina(String ime, KategorijaVM kategorija) {
		this.ime = ime;
		this.kategorija = kategorija;
		this.diskovi = new ArrayList<Disk>();
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
	

}
