package test1;

import java.util.List;

import org.openqa.selenium.By;
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
    
    public static void portalLink() {
    	
    	driver.get("https://portal.microsofticm.com/imp/v3/incidents/search/advanced?sl=0pkusngqfou");
    }
    
    

    
    public static void checkNewIncidents() throws InterruptedException {
    	
    	WebDriverWait wait = new WebDriverWait(driver, 30); // Set the maximum wait time as needed

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("MSIT-ADFS-Federation")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"bySelection\"]/div[2]/div/span")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tilesHolder\"]/div[1]/div/div")));
        element.click();
        System.out.println("Started");
        Thread.sleep(20000);
        
        List<WebElement> rows = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr"));
        int rsize = rows.size();
        System.out.println(rows.size());
        List<WebElement> cols = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td"));
        int colsize = cols.size();
        System.out.println(cols.size());
      
        String owningTeam = "";
        String sev;
        int idCol,sevCol,ownTeamCol;
        idCol = DynamicColumns.checkNewIncidents("ID", driver);
        String incidentID =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(idCol)+"]")).getText();
        ownTeamCol = DynamicColumns.checkNewIncidents("Owning Team", driver);
        owningTeam = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(ownTeamCol)+"]")).getText();
        sevCol = DynamicColumns.checkNewIncidents("Severity", driver);
        sev = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(sevCol)+"]")).getText();
    	System.out.println("Current Incident: "+incidentID);
    	System.out.println("Owning Team: "+ owningTeam);
    	System.out.println("Severity: "+sev);

    	String latest = incidentID;
    	/*Following block has been modified 
    	 *Our Program can now acknowledge more than 1 tickets */
    	
    	while(true) {
    		Boolean flag = true;
//    		Boolean flag2 = true;
        	System.out.println("Inside Loop");
        	System.out.println("Entered and Started");
        	driver.navigate().refresh();
        	System.out.println("refreshed");
        	Thread.sleep(17000);
        	List<WebElement>  rws = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr"));
            rsize = rws.size();
            System.out.println(rsize);
            List<WebElement> columns = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td"));
            colsize = columns.size();
            System.out.println(colsize);
            String tillHere = latest;
            String newIncident =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(idCol)+"]")).getText();
            if(!newIncident.equals(latest)) {
            	for(int i = 99; flag!= false; --i) {
                	newIncident =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(idCol)+"]")).getText();
                	owningTeam = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(ownTeamCol)+"]")).getText();
                	sev = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-i)+"]/td["+(sevCol)+"]")).getText();
                	System.out.println("Owning Team: "+owningTeam);
                	if(owningTeam.equals("C+AI Learn Eng Live Site")) {
                		owningTeam = "C + AI Learn Engineering Live Site";
                		System.out.println(newIncident);
                    	if(!newIncident.equals(tillHere)) {
                    		System.out.println("New Incident came: "+newIncident);
                    		System.out.println("Owning Team: "+owningTeam);
                    		System.out.println("Severity: "+sev);
                    		voice.speak("New Incident has Arrived. Owning Team is "+ owningTeam + " and Severity is "+sev); // Speak the text
                    		latest = newIncident;
                    		driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td[1]")).click();
                    		driver.findElement(By.xpath("//*[@id=\"skip-to-main\"]/ui-view/ui-view/icm-collapsible-panels/main-panel/searchresults/ul/li[6]/div/command-buttons-addnl/delayload/incident-actionbuttons/div/div/div[2]/acknowledgeincident/button")).click();
                    		voice.speak("Acknowledgement Completed");
                    		System.out.println("Acknowledged");
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
            	System.out.println();
            	System.out.println("No New Incident!!");
            	System.out.println("Incident ID: "+latest);
            	System.out.println("Owning Team: "+owningTeam);
            	System.out.println("Severity: "+sev);
            	System.out.println();
//            	voice.speak("No Incident Found.");
            }
            
        	
//        	Thread.sleep(7000);
        	
        }
    }
}





