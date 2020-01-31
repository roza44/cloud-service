package model;

import java.util.Date;

public class VMActivity {
	public Date timestamp;
	public boolean turnedOn;
	
	public VMActivity(boolean turnedOn) {
		this.timestamp = new Date();
		this.turnedOn = turnedOn;
	}
	
	public VMActivity(boolean turnedOn, Date timestamp) {
		this.timestamp = timestamp;
		this.turnedOn = turnedOn;
	}
}
