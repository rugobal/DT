package com.rugobal.dt.business.model;

import java.util.Date;

public class DayTrade {
	
	private Date date;
	
	private int t1PossitiveOperations;
	private double t1PossitivePoints;
	private int t1NegativeOperations;
	private double t1NegativePoints;
	
	private int t2PossitiveOperations;
	private double t2PossitivePoints;
	private int t2NegativeOperations;
	private double t2NegativePoints;
	
	private int t3PossitiveOperations;
	private double t3PossitivePoints;
	private int t3NegativeOperations;
	private double t3NegativePoints;
	
	private int totalPositiveOperations;
	private int totalNegativeOperations;
	private double totalProfitLoss;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getT1PossitiveOperations() {
		return t1PossitiveOperations;
	}
	
	public void setT1PossitiveOperations(int t1PossitiveOperations) {
		this.t1PossitiveOperations = t1PossitiveOperations;
	}
	
	public double getT1PossitivePoints() {
		return t1PossitivePoints;
	}
	
	public void setT1PossitivePoints(double t1PossitivePoints) {
		this.t1PossitivePoints = t1PossitivePoints;
	}
	
	public int getT1NegativeOperations() {
		return t1NegativeOperations;
	}
	
	public void setT1NegativeOperations(int t1NegativeOperations) {
		this.t1NegativeOperations = t1NegativeOperations;
	}
	
	public double getT1NegativePoints() {
		return t1NegativePoints;
	}
	
	public void setT1NegativePoints(double t1NegativePoints) {
		this.t1NegativePoints = t1NegativePoints;
	}
	
	public int getT2PossitiveOperations() {
		return t2PossitiveOperations;
	}
	
	public void setT2PossitiveOperations(int t2PossitiveOperations) {
		this.t2PossitiveOperations = t2PossitiveOperations;
	}
	
	public double getT2PossitivePoints() {
		return t2PossitivePoints;
	}
	public void setT2PossitivePoints(double t2PossitivePoints) {
		this.t2PossitivePoints = t2PossitivePoints;
	}
	
	public int getT2NegativeOperations() {
		return t2NegativeOperations;
	}
	
	public void setT2NegativeOperations(int t2NegativeOperations) {
		this.t2NegativeOperations = t2NegativeOperations;
	}
	public double getT2NegativePoints() {
		return t2NegativePoints;
	}
	
	public void setT2NegativePoints(double t2NegativePoints) {
		this.t2NegativePoints = t2NegativePoints;
	}
	
	public int getT3PossitiveOperations() {
		return t3PossitiveOperations;
	}
	public void setT3PossitiveOperations(int t3PossitiveOperations) {
		this.t3PossitiveOperations = t3PossitiveOperations;
	}
	
	public double getT3PossitivePoints() {
		return t3PossitivePoints;
	}
	
	public void setT3PossitivePoints(double t3PossitivePoints) {
		this.t3PossitivePoints = t3PossitivePoints;
	}
	
	public int getT3NegativeOperations() {
		return t3NegativeOperations;
	}
	
	public void setT3NegativeOperations(int t3NegativeOperations) {
		this.t3NegativeOperations = t3NegativeOperations;
	}
	
	public double getT3NegativePoints() {
		return t3NegativePoints;
	}
	
	public void setT3NegativePoints(double t3NegativePoints) {
		this.t3NegativePoints = t3NegativePoints;
	}

	public int getTotalPositiveOperations() {
		return totalPositiveOperations;
	}

	public void setTotalPositiveOperations(int totalPositiveOperations) {
		this.totalPositiveOperations = totalPositiveOperations;
	}

	public int getTotalNegativeOperations() {
		return totalNegativeOperations;
	}

	public void setTotalNegativeOperations(int totalNegativeOperations) {
		this.totalNegativeOperations = totalNegativeOperations;
	}

	public double getTotalProfitLoss() {
		return totalProfitLoss;
	}

	public void setTotalProfitLoss(double totalProfitLoss) {
		this.totalProfitLoss = totalProfitLoss;
	}
	

}
