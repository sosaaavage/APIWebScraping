package com.koedia.webscraping.drivers;

import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.koedia.api.model.Offer;

public class Demo  {
	
	/** Renvoie le nombre de mois de différence entre 2 dates */
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
	
	private static int difMonth(int firstDate, LocalDate secondDate ){
		
		int difMonth = 0;
		int monthSecondDate = secondDate.getMonthValue();
		
		difMonth = monthSecondDate - firstDate;
		
		if(difMonth < 0){
			difMonth = monthSecondDate - firstDate +12;
		}
		
		return difMonth;

	}
	
	//--
	public static void main(String[] args) throws InterruptedException, ParseException {
    	
    	HashMap<Integer, String> mapMonth = new HashMap<Integer, String>();
        mapMonth.put(1, "Janvier");
        mapMonth.put(2, "Février");
        mapMonth.put(3, "Mars");
        mapMonth.put(4, "Avril");
        mapMonth.put(5, "Mai");
        mapMonth.put(6, "Juin");
        mapMonth.put(7, "Juillet");
        mapMonth.put(8, "Août");
        mapMonth.put(9, "Septembre");
        mapMonth.put(10, "Octobre");
        mapMonth.put(11, "Novembre");
        mapMonth.put(12, "Décembre");
        
        HashMap<String, Integer> mapMonthReverse = new HashMap<String, Integer>();
        mapMonthReverse.put("Janvier",1);
        mapMonthReverse.put("Février",2);
        mapMonthReverse.put("Mars",3);
        mapMonthReverse.put("Avril",4);
        mapMonthReverse.put("Mai",5);
        mapMonthReverse.put("Juin",6);
        mapMonthReverse.put("Juillet",7);
        mapMonthReverse.put("Août",8);
        mapMonthReverse.put("Septembre",9);
        mapMonthReverse.put("Octobre",10);
        mapMonthReverse.put("Novembre",11);
        mapMonthReverse.put("Décembre",12);
    	
    	File file = new File("/src/main/java/com/koedia/webscraping/chromedriver");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		
		// init
		
		ChromeOptions chromeOptions = new ChromeOptions();
	    //chromeOptions.addArguments("--help");
		//chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--incognito");
		
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        JavascriptExecutor jse = ((JavascriptExecutor)driver); // javascript script for bypass loader UI
        // Create a new instance of the Chrome driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        
        driver.manage().deleteAllCookies();

        driver.get("https://www.trivago.fr");
        
        driver.manage().deleteAllCookies();
        
        // fill and submit form
        wait.until(ExpectedConditions.elementToBeClickable((By.name("sQuery"))));
        WebElement element = driver.findElement(By.name("sQuery"));
        
        element.sendKeys("Mercure Nice");
        element.submit();
        
        //select date setup
        
        String dateBegin = "2018-06-18";
        String dateEnd = "2018-06-22";
        
        String FORMAT = "yyyy-MM-dd";
        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern(FORMAT);
        LocalDate DateBegin = LocalDate.parse(dateBegin, DATEFORMATTER);
        LocalDate DateEnd = LocalDate.parse(dateEnd, DATEFORMATTER);
        LocalDate DateNow = LocalDate.now();
        
        // verification date
        if(DateBegin.isAfter(DateEnd) || (DateBegin.equals(DateEnd))){
        	// TODO gérer erreur date : date de début supérieur ou égale à la date de fin
        }
        
        if(DateBegin.isBefore(DateNow) || DateEnd.isBefore(DateNow)){
        	// TODO gérer erreur date : date de fin inférieure à la date de début 
        }
        
        if(difMonth(DateBegin,DateEnd)>=2){
        	// TODO gérer erreur date : écart de + de 2 mois
        }
        
        
        // define calendarMode !
        
        boolean isSimpleCalendar = false;
        String simpleCalendar = "Choisissez";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"df_label\"]")));
        element = driver.findElement(By.xpath("//*[@class=\"df_label\"]"));
        String df_label = element.getText();
        
        if(df_label.contentEquals(simpleCalendar)){
        	isSimpleCalendar = true;
        }
        
        // calendar mode selection date
        
        String currentMonth = "";
        String nextMonth = "";
        
        if(isSimpleCalendar){
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cal-heading-month\"]")));
            element = driver.findElement(By.xpath("//*[@id=\"cal-heading-month\"]"));
            currentMonth = element.getText();
            currentMonth = currentMonth.substring(0,currentMonth.length()-5);
        	System.out.println("#mois "+ currentMonth);
        }
        
        else{
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cal-heading-month-first\"]")));
            element = driver.findElement(By.xpath("//*[@id=\"cal-heading-month-first\"]"));
            currentMonth = element.getText();
            currentMonth = currentMonth.substring(0,currentMonth.length()-5);
        	System.out.println("#mois "+ currentMonth);
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"cal-heading-month-second\"]")));
            element = driver.findElement(By.xpath("//*[@id=\"cal-heading-month-second\"]"));
            nextMonth = element.getText();
            nextMonth = nextMonth.substring(0,nextMonth.length()-5);
        	System.out.println("#mois "+ nextMonth);
        }
        
        //init calendar
        
        if(!currentMonth.contentEquals(mapMonth.get(DateNow.getMonthValue()))){
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"cal-btn-prev\"]")));
            element = driver.findElement(By.xpath("//*[@class=\"cal-btn-prev\"]"));
            element.click();
            
            currentMonth = mapMonth.get(DateNow.getMonthValue());
        }
        
        //select date begins
        
        //simple calendar
        
        //new conditions
        
        if(isSimpleCalendar){
        	
        	if(!mapMonth.get(DateBegin.getMonthValue()).contentEquals(currentMonth)){
        		
        		int nbClickNextMonth = difMonth(mapMonthReverse.get(currentMonth),DateBegin);
        		
	        	for(int i = 0; i< nbClickNextMonth;i++){
	        		
		        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"cal-btn-next\"]")));
		            element = driver.findElement(By.xpath("//*[@class=\"cal-btn-next\"]"));
		            element.click();
	        	}
	        	
	        }
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+dateBegin+"']")));
     	    element = driver.findElement(By.xpath("//*[@datetime ='"+dateBegin+"']"));
     	    element.click();
     	    
     	    currentMonth = mapMonth.get(DateBegin.getMonthValue());
     	    
     	    if (!mapMonth.get(DateEnd.getMonthValue()).contentEquals(currentMonth)){
     	    	
     	    	int nbClickNextMonth = difMonth(DateBegin, DateEnd);
	        	for(int i = 0; i< nbClickNextMonth;i++){
		        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"cal-btn-next\"]")));
		            element = driver.findElement(By.xpath("//*[@class=\"cal-btn-next\"]"));
		            element.click();
	        	}
     	    	
     	    }
     	    
     	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+dateEnd+"']")));
    	    element = driver.findElement(By.xpath("//*[@datetime ='"+dateEnd+"']"));
    	    element.click();
    	    
        }
        
        if(!isSimpleCalendar){
        	if((!mapMonth.get(DateBegin.getMonthValue()).contentEquals(currentMonth)) && (!mapMonth.get(DateBegin.getMonthValue()).contentEquals(nextMonth))){
        		
        		int nbClickNextMonth = difMonth(mapMonthReverse.get(currentMonth),DateBegin);
            	
            	for(int i = 0; i< nbClickNextMonth;i++){
    	        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"cal-btn-next\"]")));
    	            element = driver.findElement(By.xpath("//*[@class=\"cal-btn-next\"]"));
    	            element.click();
            	}	
        	}
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+dateBegin+"']")));
     	    element = driver.findElement(By.xpath("//*[@datetime ='"+dateBegin+"']"));
     	    element.click();
        	
        	if((!mapMonth.get(DateEnd.getMonthValue()).contentEquals(currentMonth)) && (!mapMonth.get(DateEnd.getMonthValue()).contentEquals(nextMonth))){
        		
        		currentMonth = mapMonth.get(DateBegin.getMonthValue());
            	int nbClickNextMonth = difMonth(DateBegin, DateEnd);
            	
            	for(int i = 0; i< nbClickNextMonth;i++){
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"cal-btn-next\"]")));
            		element = driver.findElement(By.xpath("//*[@class=\"cal-btn-next\"]"));
            		element.click();
            	}
        	}
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+dateEnd+"']")));
        element = driver.findElement(By.xpath("//*[@datetime ='"+dateEnd+"']"));
        element.click();
        }
        //select room
        driver.switchTo().defaultContent(); //focus to main page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@ref=\"roomsButton\"]")));
        element = driver.findElement(By.xpath("//*[@ref=\"roomsButton\"]"));
        element.click();
        
        // name click
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@dir=\"ltr\"][1]")));
        element = driver.findElement(By.xpath("//*[@dir=\"ltr\"][1]"));
        jse.executeScript("arguments[0].click();", element);
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"item__slideout\"]/div/div[1]/div/ul/li[4]/button")));
        element = driver.findElement(By.xpath("//*[@class=\"item__slideout\"]/div/div[1]/div/ul/li[4]/button"));
        jse.executeScript("arguments[0].click();", element);
        
        // all offers
        
        // /!\ 
        // display all offers
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@class,\"sl-box__expand-btn\")]")));
	    element = driver.findElement(By.xpath(".//*[contains(@class,\"sl-box__expand-btn\")]"));
	    String txtButtonExpand = element.getText();
	    
	    while(txtButtonExpand.contentEquals("Voir plus")){
	    	jse.executeScript("arguments[0].click();", element);
	    	//Thread.sleep(1000);
	    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@class,\"sl-box__expand-btn\")]")));
	 	    element = driver.findElement(By.xpath(".//*[contains(@class,\"sl-box__expand-btn\")]"));
	 	    txtButtonExpand = element.getText();
	    }
	    // /!\ 
        // foreach offers
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@class,\"js_co_deal\") and contains(@class,\"sl-deal\")]")));
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@class,\"js_co_deal\") and contains(@class,\"sl-deal\")]"));
        
        List<Offer> listOffers = new ArrayList<Offer>();
        
        for(WebElement aOffer : elements){
        	
        	int indexOffer = elements.indexOf(aOffer);
        	
        	// platform
        	
        	 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class=\"sl-deal__logo-img\"]")));
		     element = driver.findElements(By.xpath(".//*[@class=\"sl-deal__logo-img\"]")).get(indexOffer);
		     String platform = element.getAttribute("alt");
		     
		     // desc
		     
		     wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,\"sl-deal__text-desc\")]")));
		     element = driver.findElements(By.xpath(".//*[contains(@class,\"sl-deal__text-desc\")]")).get(indexOffer*2);
		     String desc = element.getText();
		     
		     String breakfast = "";
		     boolean breakfastIsInclued = false;
		     double price = 0;
		     
		     if((platform != null) && (desc != null)){
			     if((!platform.contentEquals("null")) && (!desc.contentEquals("null"))){
				     
			    	 if(!desc.contentEquals("Plusieurs offres disponibles pour votre recherche")){
				    	
			    		 // breakfast
				    	 
				    	 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,\"sl-deal__rate-attr\")]")));
					     element = driver.findElement(By.xpath(".//*[contains(@class,\"sl-deal__rate-attr\")]"));
					     breakfast = element.getText();
					     if(breakfast.contentEquals("Petit-déjeuner gratuit")){
					    	 breakfastIsInclued = true;
					     }
			    	 }
			    	 
			     
	
				     // price
				     
				     wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,\"sl-deal__btn-lbl\")]")));
				     element = driver.findElements(By.xpath(".//*[contains(@class,\"sl-deal__btn-lbl\")]")).get(indexOffer);
				     String priceStr = element.getText();
				     priceStr = priceStr.substring(0,priceStr.length()-1).replace(" ","");
				     
				     if(priceStr.startsWith("dès")){
				    	 priceStr = priceStr.substring(3);
				     }
				     
				     price = Double.valueOf(priceStr);
				 
			     }
			     
			     Offer aOfferObject = new Offer(platform,desc,breakfastIsInclued,price);
			     listOffers.add(aOfferObject);
		     }
		     
        }
        
        //driver.quit();
        
        for(Offer offer : listOffers){
        	System.out.println("\n Platform : "+offer.getPlatformName()+" -Description : "+offer.getDesc()+" -BF : "+offer.isBreakfastInclued()+" -Price : "+offer.getPrice());
        }
        
    }
  }
