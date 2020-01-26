package model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Organizacija {
	
	private String ime;
	private String opis;
	private String slikaPutanja;
	
	private ArrayList<Korisnik> korisnici;
	private ArrayList<VirtualnaMasina> resursi;
	
	
	public Organizacija(String ime, String opis) {
		super();
		this.ime = ime;
		this.opis = opis;
		this.korisnici = new ArrayList<Korisnik>();
		this.resursi = new ArrayList<VirtualnaMasina>();
	}
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
	public String getSlikaPutanja() {
		return slikaPutanja;
	}
	public String getSlikaIme() {
		return ime.replaceAll(" ", "_");
	}
	public void setSlikaPutanja(String slikaPutanja) {
		this.slikaPutanja = slikaPutanja;
	}
	
	

}
