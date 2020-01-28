package model;

import com.google.gson.annotations.Expose;

public class Disk {
	private String ime, tip;
	private double kapacitet;
	
	private VirtualnaMasina vm;
	private Organizacija organizacija;
	

	public Disk(String ime, String tip, double kapacitet, VirtualnaMasina vm, Organizacija org) {
		super();
		this.ime = ime;
		this.tip = tip;
		this.kapacitet = kapacitet;
		this.vm = vm;
		this.organizacija = org;
	}
	
	public Organizacija getOrganizacija() {
		return organizacija;
	}
	
	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public double getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(double kapacitet) {
		this.kapacitet = kapacitet;
	}

	public VirtualnaMasina getVm() {
		return vm;
	}

	public void setVm(VirtualnaMasina vm) {
		this.vm = vm;
	}
	
	
	
	
}
