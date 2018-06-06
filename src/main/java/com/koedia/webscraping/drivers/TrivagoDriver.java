package com.koedia.webscraping.drivers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.koedia.api.model.Hotel;
import com.koedia.api.model.Offer;
import com.koedia.webscraping.Core;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;



public class TrivagoDriver {
	
	/** Setup driver Trivago*/
	private static final String baseURL = "https://www.trivago.fr/";
	public static WebDriver driverTrivago;
	private static final int waitTime = 10; // wait JS DOM exec
	private static final String serverAddress = "http://localhost";
	private static final String port = ":8080";
	private static final String screenshotDirectory = "/screenshots/";
	private static final String screenshotAbsolutePath = "src/main/resources"+screenshotDirectory;
	private static final String screenshotWebPath = serverAddress+port+screenshotDirectory;
	
	/** Setup xPaths */
	private static final String containerId = "//*[contains(@class,\"item-order__list-item\")]";
	private static final String formName = "sQuery";
	private static final String buttonName = "//*[@dir=\"ltr\"][1]";
	private static final String tabListOffers = "//*[@class=\"item__slideout\"]/div/div[1]/div/ul/li[4]/button";
	private static final String labelOffers = "//*[contains(@class,\"js_co_deal\") and contains(@class,\"sl-deal\")]";
	private static final String labelPlaform = "//*[@class=\"sl-deal__logo-img\"]";
	private static final String labelDesc = ".//*[contains(@class,\"sl-deal__text-desc\")]";
	private static final String labelBreakfast = ".//*[contains(@class,\"sl-deal__rate-attr\")]";
	private static final String labelPrice = ".//*[contains(@class,\"sl-deal__btn-lbl\")]";
	private static final String buttonRoom ="//*[@ref=\"roomsButton\"]";
	private static final String labelCalendar = "//*[@class=\"df_label\"]";
	private static final String panelMonthSimpleCalendar = "//*[@id=\"cal-heading-month\"]";
	private static final String panelFirstMonthDoubleCalendar = "//*[@id=\"cal-heading-month-first\"]";
	private static final String panelSecondMonthDoubleCalendar = "//*[@id=\"cal-heading-month-second\"]";
	private static final String buttonPrevCalendar = "//*[@class=\"cal-btn-prev\"]";
	private static final String buttonNextCalendar = "//*[@class=\"cal-btn-next\"]";
	private static final String buttonSeeMore = "//*[contains(@class,\"sl-box__expand-btn\")]";
	private static final String messageCookies = "//*[contains(@class,\"btn--tertiary\")][1]";
	
	/** Setup textuals variables displayed on website */
	
	private static final String textFreeBreakFast = "Petit-déjeuner gratuit";
	private static final String textNoOffer = "Aucune offre actuellement disponible sur ce site";
	private static final String textMultipleOffers= "Plusieurs offres disponibles pour votre recherche";
	private static final String textBeforePrice = "dès";
	private static final String textBtnSeeMore = "Voir plus";
	
	
	/** Setup attributs */
	private static final String idAttr = "data-item";
	private static final String platformAttr = "alt";
	private static final String textSimpleCalendar = "Choisissez";
	
	/** Setup webservice Trivago JSON*/
	private final static  String jsonURLBegin = "https://www.trivago.fr/api/v1/_cache/accommodation/";
	private final static String jsonURLEnd = "/complete-info.json";
	
	/** Setup date */
	
	private static final String FORMAT = "yyyy-MM-dd";
	private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern(FORMAT);
	
	private static final HashMap<Integer, String> mapMonth = new HashMap<Integer, String>();
    static {
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
    }
    private static final HashMap<String, Integer> mapMonthReverse = new HashMap<String, Integer>();
    static {
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
    }
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
    
	/** Constructeur privé */
	private TrivagoDriver(){}
	
	/** Instance unique pré-initialisée */
	private static TrivagoDriver INSTANCE = new TrivagoDriver();
	
	/** Point d'accès pour l'instance unique du singleton */
	public static TrivagoDriver getInstance(){
		return INSTANCE;
	}
	
	public static WebDriver submitForm(String aQuery,String aDateBegin, String aDateEnd){
		
		WebDriver driver = Core.open(baseURL);
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		driver.manage().deleteAllCookies();
		wait.until(ExpectedConditions.elementToBeClickable((By.name(formName))));
        WebElement element = driver.findElement(By.name(formName));
        element.sendKeys(aQuery);
        element.submit();
        
        //select dates
        
        LocalDate DateBegin = LocalDate.parse(aDateBegin, DATEFORMATTER);
        LocalDate DateEnd = LocalDate.parse(aDateEnd, DATEFORMATTER);
        LocalDate DateNow = LocalDate.now();
        
        // verification date
        if(DateBegin.isAfter(DateEnd) || (DateBegin.equals(DateEnd))){
        	// TODO gérer erreur date
        }
        
        if(DateBegin.isBefore(DateNow) || DateEnd.isBefore(DateNow)){
        	// TODO gérer erreur date
        }
        
        if(difMonth(DateBegin,DateEnd)>=2){
        	// TODO gérer erreur date : écart de + de 2 mois
        }
        
        // define calendarMode
        
        boolean isSimpleCalendar = false;
        String simpleCalendar = textSimpleCalendar;
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(labelCalendar)));
        element = driver.findElement(By.xpath(labelCalendar));
        String df_label = element.getText();
        
        if(df_label.contentEquals(simpleCalendar)){
        	isSimpleCalendar = true;
        }
        
        // calendar mode selection date
        
        String currentMonth = "";
        String nextMonth = "";
        
        if(isSimpleCalendar){
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(panelMonthSimpleCalendar)));
            element = driver.findElement(By.xpath(panelMonthSimpleCalendar));
            currentMonth = element.getText();
            currentMonth = currentMonth.substring(0,currentMonth.length()-5); // delete year
        }
        
        else {
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(panelFirstMonthDoubleCalendar)));
            element = driver.findElement(By.xpath(panelFirstMonthDoubleCalendar));
            currentMonth = element.getText();
            currentMonth = currentMonth.substring(0,currentMonth.length()-5);
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(panelSecondMonthDoubleCalendar)));
            element = driver.findElement(By.xpath(panelSecondMonthDoubleCalendar));
            nextMonth = element.getText();
            nextMonth = nextMonth.substring(0,nextMonth.length()-5);
        }
        
        //init calendar
        
        if(!currentMonth.contentEquals(mapMonth.get(DateNow.getMonthValue()))){
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonPrevCalendar)));
            element = driver.findElement(By.xpath(buttonPrevCalendar));
            element.click();
            
            currentMonth = mapMonth.get(DateNow.getMonthValue());
        }
        
        //input dates in calendars
        
        //simple calendar
        
        if(isSimpleCalendar){
        	
        	// if month begin date isn't in current month
        	if(!mapMonth.get(DateBegin.getMonthValue()).contentEquals(currentMonth)){
        		
        		//go to the good month
        		int nbClickNextMonth = difMonth(mapMonthReverse.get(currentMonth),DateBegin);
	        	for(int i = 0; i< nbClickNextMonth;i++){
	        		
		        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonNextCalendar)));
		            element = driver.findElement(By.xpath(buttonNextCalendar));
		            element.click();
	        	}
	        	
	        }
        	
        	// input begin date
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+aDateBegin+"']")));
     	    element = driver.findElement(By.xpath("//*[@datetime ='"+aDateBegin+"']"));
     	    element.click();
     	    
     	    // update current month
     	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(panelMonthSimpleCalendar)));
     	    element = driver.findElement(By.xpath(panelMonthSimpleCalendar));
     	    currentMonth = element.getText();
     	    currentMonth = currentMonth.substring(0,currentMonth.length()-5); // delete year
     	    
     	    // if month end date isn't in current month
     	    if (!mapMonth.get(DateEnd.getMonthValue()).contentEquals(currentMonth)){
     	    	
     	    	//go to the good month
     	    	int nbClickNextMonth = difMonth(DateBegin, DateEnd);
	        	for(int i = 0; i< nbClickNextMonth;i++){
		        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonNextCalendar)));
		            element = driver.findElement(By.xpath(buttonNextCalendar));
		            element.click();
	        	}
     	    	
     	    }
     	    
     	    // input end date
     	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+aDateEnd+"']")));
    	    element = driver.findElement(By.xpath("//*[@datetime ='"+aDateEnd+"']"));
    	    element.click();
    	    
        }
        
        //double calendar
        
        if(!isSimpleCalendar){
        	if((!mapMonth.get(DateBegin.getMonthValue()).contentEquals(currentMonth)) && (!mapMonth.get(DateBegin.getMonthValue()).contentEquals(nextMonth))){
        		
        		int nbClickNextMonth = difMonth(mapMonthReverse.get(currentMonth),DateBegin);
            	
            	for(int i = 0; i< nbClickNextMonth;i++){
    	        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonNextCalendar)));
    	            element = driver.findElement(By.xpath(buttonNextCalendar));
    	            element.click();
            	}	
        	}
        	
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+aDateBegin+"']")));
     	    element = driver.findElement(By.xpath("//*[@datetime ='"+aDateBegin+"']"));
     	    element.click();
        	
        	if((!mapMonth.get(DateEnd.getMonthValue()).contentEquals(currentMonth)) && (!mapMonth.get(DateEnd.getMonthValue()).contentEquals(nextMonth))){
        		
        		currentMonth = mapMonth.get(DateBegin.getMonthValue());
            	int nbClickNextMonth = difMonth(DateBegin, DateEnd);
            	
            	for(int i = 0; i< nbClickNextMonth;i++){
            		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonNextCalendar)));
            		element = driver.findElement(By.xpath(buttonNextCalendar));
            		element.click();
            	}
        	}
        
        	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@datetime ='"+aDateEnd+"']")));
        	element = driver.findElement(By.xpath("//*[@datetime ='"+aDateEnd+"']"));
        	element.click();
        }

        //select room
        driver.switchTo().defaultContent(); //focus to main page
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonRoom)));
        element = driver.findElement(By.xpath(buttonRoom));
        element.click();
        
        return driver;
	}
	
	public static WebDriver submitForm(String aQuery){
		
		WebDriver driver = Core.open(baseURL);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable((By.name(formName))));
        WebElement element = driver.findElement(By.name(formName));
        element.sendKeys(aQuery);
        element.submit();
        
        // return
        
        return driver;
	}

	public static int getId(String aName){
	
		WebDriver driver = TrivagoDriver.submitForm(aName);
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.elementToBeClickable((By.xpath(TrivagoDriver.containerId)))).getAttribute(idAttr);
		int id = Integer.valueOf(driver.findElement(By.xpath(TrivagoDriver.containerId)).getAttribute(idAttr));
		TrivagoDriver.driverTrivago = driver;
		return id;
	}

	
	public static int getId(String aName, String aDateBegin, String aDateEnd){
		
		WebDriver driver = TrivagoDriver.submitForm(aName,aDateBegin,aDateEnd);
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.elementToBeClickable((By.xpath(TrivagoDriver.containerId)))).getAttribute(idAttr);
		int id = Integer.valueOf(driver.findElement(By.xpath(TrivagoDriver.containerId)).getAttribute(idAttr));
		TrivagoDriver.driverTrivago = driver;
		return id;
	}
	
	public static Hotel getHotel(String aName,String aDateBegin, String aDateEnd) throws IOException, JSONException, InterruptedException{
		
		// get id
		
		int id = TrivagoDriver.getId(aName, aDateBegin, aDateEnd);
		
		// read json
		
		URL URLJson = new URL(jsonURLBegin+id+jsonURLEnd);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(URLJson.openStream()));
		String strJSON = "",str = "";
		while (null != (str = buffer.readLine())) {
			strJSON = str;
		}
		JSONObject JSONDatas = new JSONObject(strJSON);
		
		// build propreties of object
		
		//JSON datas
		
		String name;
		JSONObject accommodation = JSONDatas.getJSONObject("accommodation");
		name = accommodation.getString("name");
		
		JSONObject address = accommodation.getJSONObject("address");
		String street = address.getString("street");
		String locality = address.getString("locality");
		String postalCode = address.getString("postalCode");
		String country = address.getString("country");
		
		JSONObject hotelStarRating = accommodation.getJSONObject("hotelStarRating");
		int rate = hotelStarRating.getInt("starCount");
		
		//WebScraping datas init objects
		
		WebDriverWait wait = new WebDriverWait(driverTrivago, waitTime);
		JavascriptExecutor jse = ((JavascriptExecutor)driverTrivago); // javascript script for bypass loader UI
		
		// cookies accept
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(messageCookies)));
		WebElement element = driverTrivago.findElement(By.xpath(messageCookies));
		jse.executeScript("arguments[0].click();", element);
		
		//  offers
		
		//name click
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonName)));
        element = driverTrivago.findElement(By.xpath(buttonName));
        jse.executeScript("arguments[0].click();", element);
        
        //tab offer click
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(tabListOffers)));
        element = driverTrivago.findElement(By.xpath(tabListOffers));
        jse.executeScript("arguments[0].click();", element);
        
        // offers
        
        // display all offers
        
        //text recovery
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonSeeMore)));
	    element = driverTrivago.findElement(By.xpath(buttonSeeMore));
	    String txtButtonExpand = element.getText();
	    
	    while(txtButtonExpand.contentEquals(textBtnSeeMore)){
	    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonSeeMore)));
	    	jse.executeScript("arguments[0].click();", element);
	    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonSeeMore)));
	 	    element = driverTrivago.findElement(By.xpath(buttonSeeMore));
	 	    txtButtonExpand = element.getText();
	    }
        
	    
	    //Screenshot
	    
	    //generate unique name pic
	    
	    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    String UUID = String.valueOf(timestamp.getTime());
	    UUID = DigestUtils.md5Hex(UUID);
	    
	    //capture screenshot
	    
	    WebElement ElementScreen = driverTrivago.findElement(By.xpath("//article[@id=\"js_item_"+id+"\"]")) ;
	    Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingStrategy(500)).takeScreenshot(driverTrivago,ElementScreen);
        BufferedImage image = screenshot.getImage();
        String nameFile = UUID+".png";
        String linkScreenshot = screenshotAbsolutePath+nameFile; 
        ImageIO.write(image, "PNG", new File(linkScreenshot));
        String linkWebScreenshot = screenshotWebPath+nameFile;
        
	    // for all offers
	    
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(labelOffers)));
        List<WebElement> elements = driverTrivago.findElements(By.xpath(labelOffers));
        
        List<Offer> listOffers = new ArrayList<Offer>();
        
        for(WebElement aOffer : elements){
        	
	        int indexOffer = elements.indexOf(aOffer);
	        
	        String platform = "";
	        String breakfast = "";
	        boolean breakfastIsInclued = false;
	        double price = 0;
	        String desc = "";
	        int indexBF = 0;
	        
	        // platform
	        
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelPlaform)));
			element = driverTrivago.findElements(By.xpath(labelPlaform)).get(indexOffer);
			platform = element.getAttribute(platformAttr);
			
			// desc
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelDesc)));
		    element = driverTrivago.findElements(By.xpath(labelDesc)).get(indexOffer*2); // 1 on 2
		    desc = element.getText();
		    
		    if(desc.contentEquals(textNoOffer)){break;} // optimization when there are no more offers
		    
		    if((platform != null) && (desc != null)){
			
				if((!desc.contentEquals(textNoOffer))) {
					
					if(!desc.contentEquals(textMultipleOffers)){
						
						// breakfast
					
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelBreakfast)));
						element = driverTrivago.findElements(By.xpath(labelBreakfast)).get(indexBF);
						breakfast = element.getText();
						breakfastIsInclued = false;
						
						if(breakfast.contentEquals(textFreeBreakFast)){
							breakfastIsInclued = true;
						}
						
						indexBF = indexBF + 1;
					}
					// price
					
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(labelPrice)));
					element = driverTrivago.findElements(By.xpath(labelPrice)).get(indexOffer);
					String priceStr = element.getText();
					priceStr = priceStr.replace(" ","");
					priceStr = priceStr.substring(0,priceStr.length()-1);
					
					if(priceStr.startsWith(textBeforePrice)){
				    	 priceStr = priceStr.substring(3);
				     }
					
					price = Double.valueOf(priceStr);
				
				}
				     
				
				Offer aOfferObject = new Offer(platform,desc,breakfastIsInclued,price);
				aOfferObject.display();
				listOffers.add(aOfferObject);
			    }
        }
        
		// build hotel object
		
		Hotel hotelBuilt = new Hotel(name,street,locality,postalCode,country,rate,id,linkWebScreenshot,listOffers);
		
		return hotelBuilt;
		
		}
	}
