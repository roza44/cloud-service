package model;

import java.util.ArrayList;

public class Organizacija {
	
	private String ime;
	private String opis;
	private ArrayList<Korisnik> korisnici;
	private ArrayList<VirtualnaMasina> resursi;
	
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}
	public void dodajKorisnika(Korisnik korisnik) {
		this.korisnici.add(korisnik);
	}
	public ArrayList<VirtualnaMasina> getResursi() {
		return resursi;
	}
	public void dodajResurs(VirtualnaMasina vm) {
		this.resursi.add(vm);
	}
	
	

}
