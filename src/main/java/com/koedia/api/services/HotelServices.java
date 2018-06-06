package com.koedia.api.services;

import java.io.IOException;

import org.json.JSONException;

import com.koedia.api.model.Hotel;

/**
 * Hotel Services
 */
public interface HotelServices  {
	
	Hotel getOffers(String aPlatform, String aName, String aDateBegin, String aDateEnd) throws IOException, JSONException, InterruptedException;
	int getID(String aPlatform, String aName) throws IOException;
}
