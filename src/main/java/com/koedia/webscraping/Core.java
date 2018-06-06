package com.koedia.webscraping;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/** DOC PhantomJS 
 * https://docs.seleniumhq.org/docs/03_webdriver.jsp
 * */

public class Core {
	
	/** Configuration Core */
	private static final String pathChromeDriver= "src/main/java/com/koedia/webscraping/chromedriver";
	
	/** Constructeur privé */
	private Core(){}
	
	/** Instance unique pré-initialisée */
	private static Core INSTANCE = new Core();
	
	/** Point d'accès pour l'instance unique du singleton */
	public static Core getInstanceCore(){
		return INSTANCE;
	}
	
	public static WebDriver open(String abaseURL){
		
		// set and load ChromeDriver file
		
		File file = new File(Core.pathChromeDriver);
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		// init
		// for list arguments : chromeOptions.addArguments("--help");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("incognito");
		//chromeOptions.addArguments("--headless");
		
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(abaseURL);
        driver.manage().deleteAllCookies();
        
        return driver;
		
	}
	
	public static void close(WebDriver aDriver){
		aDriver.quit();
	}
	
}