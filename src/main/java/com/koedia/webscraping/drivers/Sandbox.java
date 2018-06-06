package com.koedia.webscraping.drivers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Sandbox {
	
	private static int difMonth(LocalDate firstDate, LocalDate secondDate ){
		
		int difMonth = 0;
		int monthFirstDate = firstDate.getMonthValue();
		int monthSecondDate = secondDate.getMonthValue();
		
		difMonth = monthSecondDate - monthFirstDate;
		
		if(difMonth < 0){
			difMonth = monthSecondDate - monthFirstDate +12;
		}
		
		return difMonth;

	}
	
	public static void main(String[] args) {
		
		String FORMAT = "yyyy-MM-dd";
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern(FORMAT);
		
		String dateBegin = "2018-11-22";
		String dateEnd = "2018-01-30";
		
        LocalDate DateBegin = LocalDate.parse(dateBegin, DATEFORMATTER);
        LocalDate DateEnd = LocalDate.parse(dateEnd, DATEFORMATTER);
        
        int difMonth = difMonth(DateBegin,DateEnd);
        
        System.out.println(difMonth);
		
	}
		

}
