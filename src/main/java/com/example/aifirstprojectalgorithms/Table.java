package com.example.aifirstprojectalgorithms;

public class Table implements Comparable{
	private Country currentCountry;
	private boolean known;
	private double distance;
	private double distanceWithHeuristic;
	private Country sourceCountry;
	
	public Table(Country currentCountry, boolean known, double distance, Country sourceCountry) {
		super();
		this.currentCountry = currentCountry;
		this.known = known;
		this.distance = distance;
		this.sourceCountry = sourceCountry;
	}

	public Country getCurrentCountry() {
		return currentCountry;
	}

	public void setCurrentCountry(Country currentCountry) {
		this.currentCountry = currentCountry;
	}

	public boolean isKnown() {
		return known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Country getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(Country sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	

	public double getDistanceWithHeuristic() {
		return distanceWithHeuristic;
	}

	public void setDistanceWithHeuristic(double distanceWithHeuristic) {
		this.distanceWithHeuristic = distanceWithHeuristic;
	}

	@Override
	public String toString() {
		return "Table [currentCity=" + currentCountry + ", known=" + known + ", distance=" + distance + ", sourceCity="
				+ sourceCountry + "]";
	}
	
	@Override
	public int compareTo(Object o) {
		Table obj=(Table) o;
		 if (this.distanceWithHeuristic>obj.getDistanceWithHeuristic())
		 	return 1;
		 else
		 	return -1;
	}
}
