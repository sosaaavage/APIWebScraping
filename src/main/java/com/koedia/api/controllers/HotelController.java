package com.koedia.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import java.io.IOException;

import javax.xml.ws.Response;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.koedia.api.model.Hotel;
import com.koedia.api.services.HotelServices;

/**
 *Hotel Controller 
 */
@RestController
@Api(value="hotel Controller", description="Operations for hotels")
public class HotelController {
	
	private final String customMessageCode500 = "We are sorry, the request has not been successful.\nIf this is the first time for this query, please try again again : the API does not always hit the first shot, or review the name parameter\nIf the error is still there, please contact the server administrator";
	
	/** Config Param path*/
	private final String listPlatforms = "trivago";
	
	@Autowired
	HotelServices hotelServices;
	
	@ApiOperation(
			value = "Get address and offers in a platform for a hotel by his name",
			response = Hotel.class)
	@ApiResponses(
			value = {@ApiResponse(code = 500, message = customMessageCode500)})
	@RequestMapping(
			value = "/getOffers/{a_platform}/{a_name}/{a_dateBegin}/{a_dateEnd}",
			method = RequestMethod.GET,produces = {"application/json"})
	
    public ResponseEntity<Hotel> getHotel(
    		@PathVariable("a_platform") @ApiParam(allowableValues = listPlatforms,required = true) String aPlatform ,
    		@PathVariable("a_name")@ApiParam(required = true) String aName,
    		@PathVariable("a_dateBegin")@ApiParam(required = true) String aDateBegin,
    		@PathVariable("a_dateEnd")@ApiParam(required = true) String aDateEnd) 
    		throws IOException, JSONException, InterruptedException {
		
		return ResponseEntity.ok().body(hotelServices.getOffers(aPlatform,aName,aDateBegin,aDateEnd));
    }
	
	@ApiOperation(value = "Get ID in a platform for a hotel by his name")
	@ApiResponses(value = {@ApiResponse(code = 500, message = customMessageCode500)})
	@RequestMapping(value = "/getID/{a_platform}/{a_name}", method = RequestMethod.GET,produces = {"application/json"})
	
    public int getID(@PathVariable("a_platform") @ApiParam(allowableValues = listPlatforms,required = true) String aPlatform ,@PathVariable("a_name")@ApiParam(required = true) String aName) throws IOException, JSONException, InterruptedException {
        return hotelServices.getID(aPlatform,aName);
    }
	
}
