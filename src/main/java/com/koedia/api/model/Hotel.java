package com.koedia.api.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author anthony.gomez
 * 
 * Model hotel
 * 
 */

public class Hotel {
	
	@ApiModelProperty(notes = "The name of the hotel",example = "Néméa Résidence Green Side", position=1)
	private String name;
	
	@ApiModelProperty(notes = "The number of stars in the hotel", example = "3",position=2)
	private int rate;
	
	@ApiModelProperty(notes = "The street of the address of the hotel", example = "45, Rue Henri Poincaré")
	private String addressStreet;
	
	@ApiModelProperty(notes = "The city of the hotel", example ="Sophia-Antipolis")
	private String addressCity;
	
	@ApiModelProperty(notes = "The postal code of the hotel", example ="06410")
	private String addressPostalCode;
	
	@ApiModelProperty(notes = "The country of the hotel", example ="France")
	private String addressCountry;
	
	@ApiModelProperty(notes = "The id of the hotel for Trivago.com", example ="384236")
	private int id;
	
	@ApiModelProperty(notes = "Screenshot link of hotel offers", example ="http://*****/screenshots/*****.png")
	private String linkScreenshot;
	
	@ApiModelProperty(notes = "Offers for the hotel listing by Trivago.com")
	private List<Offer> listOffers;
	
	public Hotel(
			String aName,
			String aAddressStreet,
			String aAddressCity,
			String aAddressPostalCode,
			String aAddressCountry,
			int aRate,
			int aId,
			String aLink,
			List<Offer> aListOffer
			)
	{
		this.name = aName;
		this.addressStreet = aAddressStreet;
		this.addressCity = aAddressCity;
		this.addressPostalCode = aAddressPostalCode;
		this.addressCountry = aAddressCountry;
		this.rate = aRate;
		this.id = aId;
		this.linkScreenshot = aLink;
		this.listOffers = aListOffer;
		
	}
	
	public List<Offer> getListOffers(){
		return this.listOffers;
	}
	
	/**
	 * Gets name
	 * 
	 * @return value of name
	 */
	
	public String getName(){
		return this.name;
	}
	/**
	 * Gets num of address
	 * 
	 * @return value of id
	 */
	
	public int getId(){
		return this.id;
	}
	
	/**
	 * Gets street of address
	 * 
	 * @return value of street of address
	 */
	
	public String getAddressStreet(){
		return this.addressStreet;
	}
	
	/**
	 * Gets city of address
	 * 
	 * @return value of city of address
	 */
	
	public String getAddressCity(){
		return this.addressCity;
	}
	
	/**
	 * Gets postal code of address
	 * 
	 * @return value of postal code of address
	 */
	
	public String getAddressPostalCode(){
		return this.addressPostalCode;
	}
	
	/**
	 * Gets rate
	 * 
	 * @return value of rate
	 */
	
	public int getRate(){
		return this.rate;
	}
	
	/**
	 * Gets postal code of address
	 * 
	 * @return value of postal code of address
	 */
	
	public String getAddressCountry(){
		return this.addressCountry;
	}
	
	/**
	 * Gets screenshot link of hotel offers
	 * 
	 * @return value of screenshot link
	 */
	public String getLinkScreenshot(){
		return this.linkScreenshot;
	}
	
	
	
}
