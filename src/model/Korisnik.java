package model;

public class Korisnik {
	
	private String email;
	private String lozinka;
	private String ime;
	private String prezime;
	private Organizacija organizacija;
	
	private String uloga;
	// admin, superadmin i user
	
	public Korisnik() {
	}

	public Korisnik(String email, String lozinka, String ime, String prezime, Organizacija organizacija, String uloga) {
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
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	
	
	
	

}
