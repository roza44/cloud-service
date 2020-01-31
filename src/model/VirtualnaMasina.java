package model;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Date;

public class VirtualnaMasina {
	
	private String ime;
	private KategorijaVM kategorija;
	private Organizacija organizacija;
	
	private transient ArrayList<Disk> diskovi;
	private ArrayList<VMActivity> aktivnosti;
	
	public Organizacija getOrganizacija() {
		return organizacija;
	}
	
	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}
	
	private VirtualnaMasina() {
		this.diskovi = new ArrayList<Disk>();
		this.aktivnosti = new ArrayList<VMActivity>();
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (obj == this) { 
	            return true; 
	     }  
	     
		 if (!(obj instanceof Korisnik)) { 
	         return false; 
	     }  
	        
		 VirtualnaMasina vm = (VirtualnaMasina) obj; 
	            
	     return this.ime.equals(vm.ime);
	}
	
	public void detachDiscs() {
		for(Disk d : this.diskovi) {
			d.setVm(null);
		}
		
		this.diskovi = new ArrayList<Disk>();
	}
	
	public ArrayList<VMActivity> getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(ArrayList<VMActivity> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	public VirtualnaMasina(String ime, KategorijaVM kategorija) {
		this();
		this.ime = ime;
		this.kategorija = kategorija;
		
	}
	
	public void addActivity(boolean turnedOn) {
		this.aktivnosti.add(new VMActivity(turnedOn));
	}
	
	public void addActivity(boolean turnedOn, Date timestamp) {
		this.aktivnosti.add(new VMActivity(turnedOn, timestamp));
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
	
	public void obrisiDisk(Disk disk) {
		this.diskovi.remove(disk);
	}
	
	public boolean getActivity() {
		int last = this.aktivnosti.size() - 1;
		if(last == -1) return false;
		
		return this.aktivnosti.get(last).turnedOn;
	}
	
	private int getActivityBefore(Date timestamp) {
		VMActivity a;
		for (int i=0; i < aktivnosti.size(); i++) {
			a = aktivnosti.get(i);
			if (a.timestamp.getTime() > timestamp.getTime()) {
				// Rewind by one
				i--;
				if (i < 0) return -1; // Timestamp is earlier than any activity
				return i;
			} else if (a.timestamp.getTime() == timestamp.getTime()) {
				// Return this one
				return i;
			}
		}
		
		return aktivnosti.size() - 1; // Timestamp is later than any activity
	}
	
	public double getActiveHours(Date from, Date to) {
		if (aktivnosti.size() == 0) {
			return 0;
		}
		
		// LEGEND:
		// Offline region - time from 'false' to 'true' activity
		// Online region - time from 'true' to 'false' activity
		// --> We want to exclude all offline regions between 'from' and 'to'
		double totalHours = to.getTime() - from.getTime(); // milliseconds
		totalHours /= 1000.0 * 3600.0; // hours
		
		double excludeMs = 0;
		
		int from_act_index = getActivityBefore(from);
		int to_act_index = getActivityBefore(to);
		
		VMActivity activity, nextActivity;
		if (from_act_index == -1) {
			// Timestamp is earlier than any activity
			if (to_act_index == -1) {
				// Both points are before any activity
				return 0;
			}
			excludeMs += aktivnosti.get(0).timestamp.getTime() - from.getTime();
		} else {
			if (!aktivnosti.get(from_act_index).turnedOn) {
				if (from_act_index < to_act_index) {
					nextActivity = aktivnosti.get(from_act_index+1);
					excludeMs += nextActivity.timestamp.getTime() - from.getTime();
				} else {
					// It's completely in an offline region
					return 0;
				}
			}
		}
		int startingIndex = from_act_index+1; // Start from the next activity (or first, in case of -1)
		
		for (int i = startingIndex; i <= to_act_index; i++) {
			activity = aktivnosti.get(i);
			if (activity.turnedOn) continue; // We care only about offline regions
			if (i+1 >= aktivnosti.size()) {
				excludeMs += to.getTime() - activity.timestamp.getTime();
			} else {
				nextActivity = aktivnosti.get(i+1);
				if (nextActivity.timestamp.getTime() > to.getTime()) {
					excludeMs += to.getTime() - activity.timestamp.getTime();
				} else {					
					excludeMs += nextActivity.timestamp.getTime() - activity.timestamp.getTime();
				}
			}
		}
		
		double excludeHours = excludeMs / (1000.0 * 3600.0);
		return totalHours - excludeHours;
	}

}
