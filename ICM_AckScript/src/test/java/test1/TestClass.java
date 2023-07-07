package test1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


public class TestClass {
	static WebDriver driver;
	static Voice voice;
    static VoiceManager voiceManager;
    public static void main(String[] args) throws InterruptedException {
        initialSetup();
        portalLink();
        checkNewIncidents();
        
    }
    
    public static void initialSetup() {
    	System.setProperty("webdriver.edge.driver", "D:\\Rajat\\selenium\\Drivers\\edgedriver_win64\\msedgedriver.exe");
    	driver = new EdgeDriver();
        driver.manage().window().maximize();
        
     // Set the freetts.voices system property
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        
        // Initialize the VoiceManager
        voiceManager = VoiceManager.getInstance();
        
        // Get the desired voice
        voice = voiceManager.getVoice("kevin16");
        if (voice == null) {
            System.err.println("Requested voice not available");
            return;
        }
        
        // Set the pitch and speed
        voice.setPitch(100); // Set the pitch value (0-100)
        voice.setRate(120); // Set the speed value (80-450)
        
        // Allocate the voice
        voice.allocate();
        
     // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (driver != null) {
                    driver.quit();
                }
            }
        });
    }
    
    public static void portalLink() throws InterruptedException {
    	
    	driver.get("https://portal.microsofticm.com/imp/v3/incidents/search/advanced?sl=0pkusngqfou");
    	
        
    }
    
    

    
    public static void checkNewIncidents() throws InterruptedException {
    	
    	WebDriverWait wait = new WebDriverWait(driver, 40); // Set the maximum wait time as needed

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("MSIT-ADFS-Federation")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"bySelection\"]/div[2]/div/span")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tilesHolder\"]/div[1]/div/div")));
        element.click();
        System.out.println("Started");
     
     // Wait for the radio buttons to become clickable
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.btn-group-toggle")));

        // Find the "OFF" button label element and check if it is clicked
        WebElement offButtonLabel = driver.findElement(By.cssSelector("span.btn-group-toggle label.btn:nth-of-type(1)"));
        boolean isOffButtonClicked = offButtonLabel.getAttribute("class").contains("active");
        System.out.println(isOffButtonClicked);

        // If the "OFF" button is not clicked, perform the necessary actions
        if (!isOffButtonClicked) {
            System.out.println("OFF button is not clicked. Performing actions...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
        	js.executeScript("arguments[0].click();", offButtonLabel);
            
            // Add your code to perform actions when the "OFF" button is not clicked
        } else {
            System.out.println("OFF button is already clicked.");
            // Add your code for when the "OFF" button is already clicked
        }
        

     // Wait until the table element containing the rows is visible
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table")));

        List<WebElement> rows = tableElement.findElements(By.xpath(".//tr"));
        int rsize = rows.size();

//        List<WebElement> cols = rows.get(0).findElements(By.xpath(".//td"));
//        int colsize = cols.size();
      
        String owningTeam = "";
        String sev, title;
        int idCol,sevCol,ownTeamCol,titleCol;
        idCol = DynamicColumns.checkNewIncidents("ID", driver);
        String incidentID =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(idCol)+"]")).getText();
        ownTeamCol = DynamicColumns.checkNewIncidents("Owning Team", driver);
        owningTeam = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(ownTeamCol)+"]")).getText();
        sevCol = DynamicColumns.checkNewIncidents("Severity", driver);
        sev = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(sevCol)+"]")).getText();
        titleCol = DynamicColumns.checkNewIncidents("Title", driver);
        title = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(titleCol)+"]")).getText();
        System.out.println(title);
        
    	String latest = incidentID;
    	/*Following block has been modified 
    	 *Our Program can now acknowledge more than 1 tickets */
    	
    	while(true) {
    		Boolean flag = true;
    		
    		// Click on the "Run" button
    		WebElement runButton = driver.findElement(By.cssSelector("button[data-test-id='runQuery']"));
    		runButton.click();
    		
            String tillHere = latest;
            WebElement newinci = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table")));
            String newIncident =  newinci.findElement(By.xpath(".//tr["+(rsize-99)+"]/td["+(idCol)+"]")).getText();
            if(!newIncident.equals(latest)) {
            	for(int i = 99; flag!= false; --i) {
                	newIncident =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(idCol)+"]")).getText();
                	owningTeam = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(ownTeamCol)+"]")).getText();
                	sev = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(sevCol)+"]")).getText();
                	title = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(titleCol)+"]")).getText();
//                	System.out.println("Owning Team: "+owningTeam);
                	if(owningTeam.equals("C+AI Learn Eng Live Site")) {
                		owningTeam = "C + AI Learn Engineering Live Site";
//                		System.out.println(newIncident);
                    	if(!newIncident.equals(tillHere)) {
                    		latest = newIncident;
                    		
                    		driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td[1]")).click();
                    		driver.findElement(By.xpath("//*[@id=\"skip-to-main\"]/ui-view/ui-view/icm-collapsible-panels/main-panel/searchresults/ul/li[6]/div/command-buttons-addnl/delayload/incident-actionbuttons/div/div/div[2]/acknowledgeincident/button")).click();
                    		System.out.println("New Incident came: "+newIncident);
                    		System.out.println("Owning Team: "+owningTeam);
                    		System.out.println("Severity: "+sev);
                    		voice.speak("New Incident has Arrived. Owning Team is "+ owningTeam + " and Severity is "+sev); // Speak the text
                    		voice.speak("Acknowledged");
//                    		System.out.println("Acknowledged");
                    	}
                    	else {
                    		flag = false;
                    	}
                	}
                	else {
//                		System.out.println("Incident is not for Live Site.");
                		voice.speak("Incident is not for Live Site.");
                	}
                	
                }
            }
            else {
//            	System.out.println();
//            	System.out.println("No New Incident!!");
//            	System.out.println("Incident ID: "+latest);
//            	System.out.println("Owning Team: "+owningTeam);
//            	System.out.println("Severity: "+sev);
//            	System.out.println();
//            	voice.speak("No Incident Found.");
            }
            
        	
        	Thread.sleep(4000);
        	
        }

    }
}





