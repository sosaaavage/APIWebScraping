package com.koedia.api.model;

public class Offer {
	
	private String platformName;
	private double price;
	private String desc;
	private boolean breakfastIsInclued;
	
	
	public Offer(String aPlatformName,String aDesc,boolean aBreakfastIsInclued, double aPrice){
		
		this.platformName = aPlatformName;
		this.desc = aDesc;
		this.price = aPrice;
		this.breakfastIsInclued = aBreakfastIsInclued;
	}
	
	
	public String getPlatformName() {
		
		return platformName;
	}
	
	public void setPlatformName(String aPlatformName) {
		
		this.platformName = aPlatformName;
	}
	public double getPrice() {
		
		return price;
	}
	
	public void setPrice(double aPrice) {
		
		this.price = aPrice;
	}
	
	public boolean isBreakfastInclued() {
		
		return breakfastIsInclued;
	}
	
	public void setBreakfastInclued(boolean aBreakfastIsInclued) {
		
		this.breakfastIsInclued = aBreakfastIsInclued;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
	public void setDesc(String aDesc){
		this.desc = aDesc;
	}
	
	public void display(){
		System.out.println("[INFO][Query offer] Platform : "+this.platformName+" | Description : "+this.desc+" | Breakfast : "+ this.breakfastIsInclued+" | Price :"+this.price);
	}

}
