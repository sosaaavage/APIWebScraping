package com.koedia.api.services.impl;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.koedia.api.model.Hotel;
import com.koedia.api.services.HotelServices;
import com.koedia.webscraping.Core;
import com.koedia.webscraping.drivers.TrivagoDriver;

@Service
public class HotelServicesImpl implements HotelServices {

	@Override
	public Hotel getOffers(String aPlatform,String aName, String aDateBegin, String aDateEnd) throws IOException, JSONException, InterruptedException{
		
		Hotel hotelDisplayed = null;
		String nameUTF8 = new String(aName.getBytes(), "UTF-8");

		
		if(aPlatform.contains("trivago")){
			hotelDisplayed = TrivagoDriver.getHotel(nameUTF8,aDateBegin,aDateEnd);
		}

		//Core.close(TrivagoDriver.driverTrivago);
		
		return hotelDisplayed;
	}
	@Override
	public int getID(String aPlatform,String aName) throws IOException{
		
		int idDisplayed = 0;
		String nameUTF8 = new String(aName.getBytes(), "UTF-8");

		
		if(aPlatform.contains("trivago")){
			idDisplayed = TrivagoDriver.getId(nameUTF8);
		}
		
		Core.close(TrivagoDriver.driverTrivago);

		return idDisplayed;
	}

	
}