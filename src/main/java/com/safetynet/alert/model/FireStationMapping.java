package com.safetynet.alert.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Represent a fire station mapping in database
 * @author Mathias Lauer
 * 6 févr. 2021
 */
public class FireStationMapping {
	
	@NotNull @Min(5)
	private String address;
	
	@NotNull @Min(1)
	private int station;
	
	
	public FireStationMapping(@NotNull @Min(5) String address, @NotNull @Min(1) int station) {
		this.address = address;
		this.station = station;
	}
	
	public FireStationMapping() {
		
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getStation() {
		return station;
	}
	public void setStation(int station) {
		this.station = station;
	}

}