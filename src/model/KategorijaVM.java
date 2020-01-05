package model;

public class KategorijaVM {

	private String ime;
	private int brojJezgara;
	private double ram;
	private int gpuJezgra;
	
	public KategorijaVM(String ime, int brojJezgara, double ram, int gpuJezgra) {
		super();
		this.ime = ime;
		this.brojJezgara = brojJezgara;
		this.ram = ram;
		this.gpuJezgra = gpuJezgra;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public int getBrojJezgara() {
		return brojJezgara;
	}
	public void setBrojJezgara(int brojJezgara) {
		this.brojJezgara = brojJezgara;
	}
	public double getRam() {
		return ram;
	}
	public void setRam(double ram) {
		this.ram = ram;
	}
	public int getGpuJezgra() {
		return gpuJezgra;
	}
	public void setGpuJezgra(int gpuJezgra) {
		this.gpuJezgra = gpuJezgra;
	}
	
	
	
}
