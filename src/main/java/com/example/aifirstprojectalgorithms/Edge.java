package com.example.aifirstprojectalgorithms;

public class Edge {
	private Country Country1;
	private Country Country2;
	private double distance; // cost
	private double heuristicCar; 
	private double heuristicWalking; 
	
	
	
	
	public Edge(Country country1, Country country2,double heuristicCar, double distance,  double heuristicWalking) {
		Country1 = country1;
		Country2 = country2;
		this.distance = distance;
		this.heuristicCar = heuristicCar;
		this.heuristicWalking = heuristicWalking;
	}
	
	
	
	public double getHeuristicCar() {
		return heuristicCar;
	}

	public void setHeuristicCar(double heuristicCar) {
		this.heuristicCar = heuristicCar;
	}

	public double getHeuristicWalking() {
		return heuristicWalking;
	}

	public void setHeuristicWalking(double heuristicWalking) {
		this.heuristicWalking = heuristicWalking;
	}

	

	public Country getSourceCountry() {
		return Country1;
	}

	public void setSourceCountry(Country sourceCountry) {
		this.Country1 = sourceCountry;
	}

	public Country getTargetCountry() {
		return Country2;
	}

	public void setTargetCountry(Country targetCountry) {
		this.Country2 = targetCountry;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Edge [sourceCountry=" + Country1 + ", targetCountry=" + Country2 + ", distance=" + distance + "]";
	}
}
