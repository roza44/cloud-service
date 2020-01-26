package model;

import java.util.ArrayList;

public class VirtualnaMasina {
	
	private String ime;
	private KategorijaVM kategorija;
	private Organizacija organizacija;
	
	public Organizacija getOrganizacija() {
		return organizacija;
	}

	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}
	private transient ArrayList<Disk> diskovi;
	private ArrayList<VMActivity> aktivnosti;
	
	private VirtualnaMasina() {
		this.diskovi = new ArrayList<Disk>();
		this.aktivnosti = new ArrayList<VMActivity>();
	}
	
	public VirtualnaMasina(String ime, KategorijaVM kategorija) {
		this();
		this.ime = ime;
		this.kategorija = kategorija;
	}

	public void addDisk(Disk d) {
		diskovi.add(d);
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
	

}
