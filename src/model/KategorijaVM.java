package model;

public class KategorijaVM {

	private String ime;
	private int brojJezgara;
	private double ram;
	private int gpuJezgra;
	
	public KategorijaVM() {
		brojJezgara = 0;
		ram = 0;
		gpuJezgra = 0;
	}
	
	public KategorijaVM(String ime, int brojJezgara, double ram, int gpuJezgra) {
		this.ime = ime;
		this.brojJezgara = brojJezgara;
		this.ram = ram;
		this.gpuJezgra = gpuJezgra;
	}
	
	@Override
	public boolean equals(Object obj) {   
        if (obj == this) { 
            return true; 
        }  
        if (!(obj instanceof KategorijaVM)) { 
            return false; 
        }  
        KategorijaVM k = (KategorijaVM) obj; 
            
        return this.ime.equals(k.ime);
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
