package com.example.aifirstprojectalgorithms;

import java.util.ArrayList;


import javafx.scene.shape.Circle;

public class Country {

	private double coordinateX;
	private double coordinateY;
	
	private double latitude;  
	private double longitude;
	private String name;
	private Circle circle;
	

	private double gValue;
	private double hValue;
	private double fValue =0;
	private ArrayList<Country> adjacents = new ArrayList<>();
	public Country(double coordinateX, double coordinateY, double latitude, double longitude, String name, Circle circle) {
		super();
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.latitude=latitude;
		this.longitude=longitude;
		this.name = name;
		this.circle = circle;
	}

	public double getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(double coordinateX) {
		this.coordinateX = coordinateX;
	}

	public double getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(double coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public ArrayList<Country> getAdjacent() {
		return adjacents;
	}

	public void setAdjacent(ArrayList<Country> adjacent) {
		this.adjacents = adjacent;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	public double getgValue() {
		return gValue;
	}

	public void setgValue(double gValue) {
		this.gValue = gValue;
	}

	public double gethValue() {
		return hValue;
	}

	public void sethValue(double hValue) {
		this.hValue = hValue;
	}

	public double getfValue() {
		return fValue;
	}

	public void setfValue(double fValue) {
		this.fValue = fValue;
	}

	@Override
	public String toString() {
		return name + " -- (X : " + coordinateX + " * Y : " + coordinateY + ")";
	}

	public String fullToString() {
		return "City [coordinateX=" + coordinateX + ", coordinateY=" + coordinateY + ", name=" + name + ", circle="
				+ circle + ", adjacent=" + adjacents + "]";

	}
}
