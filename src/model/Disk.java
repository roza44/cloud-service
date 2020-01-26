package model;

import com.google.gson.annotations.Expose;

public class Disk {
	private String ime, tip;
	private double kapacitet;
	
	private transient VirtualnaMasina vm;
	
	public Disk(String ime, String tip, double kapacitet, VirtualnaMasina vm) {
		super();
		this.ime = ime;
		this.tip = tip;
		this.kapacitet = kapacitet;
		this.vm = vm;
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
