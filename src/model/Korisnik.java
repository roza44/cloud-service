package model;

enum Uloge {
	SUPER_ADMIN, ADMIN, KORISNIK;
}

public class Korisnik {
	
	private String email;
	private String lozinka;
	private String ime;
	private String prezime;
	private Organizacija organizacija;
	private Uloge uloga;
	
	public Korisnik(String email, String lozinka, String ime, String prezime, Organizacija organizacija, Uloge uloga) {
		super();
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = organizacija;
		this.uloga = uloga;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public Organizacija getOrganizacija() {
		return organizacija;
	}
	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}
	public Uloge getUloga() {
		return uloga;
	}
	public void setUloga(Uloge uloga) {
		this.uloga = uloga;
	}
	
	
	
	

}
