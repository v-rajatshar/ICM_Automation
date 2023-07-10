package test1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class TestClass {
	static WebDriver driver;
//	static Voice voice;
//    static VoiceManager voiceManager;
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, JavaLayerException {
        initialSetup();
        portalLink();
        checkNewIncidents();
        
    }
    
    public static void voiceMod(String name) throws FileNotFoundException, JavaLayerException {
    	
    	FileInputStream fis = new FileInputStream(name);
    	Player player = new Player(fis);
    	player.play();

    }
    
    public static void initialSetup() {
    	System.setProperty("webdriver.edge.driver", "D:\\Rajat\\selenium\\Drivers\\edgedriver_win64\\msedgedriver.exe");
    	driver = new EdgeDriver();
        driver.manage().window().maximize();
        
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
    
    

    
    public static void checkNewIncidents() throws InterruptedException, FileNotFoundException, JavaLayerException {
    	
    	WebDriverWait wait = new WebDriverWait(driver, 40); // Set the maximum wait time as needed

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("MSIT-ADFS-Federation")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"bySelection\"]/div[2]/div/span")));
        element.click();

        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"tilesHolder\"]/div[1]/div/div")));
        element.click();
        System.out.println("Started");
     
     
        

     // Wait until the table element containing the rows is visible
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table")));

        List<WebElement> rows = tableElement.findElements(By.xpath(".//tr"));
        int rsize = rows.size();
        
     // Wait for the radio buttons to become clickable
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.btn-group-toggle")));

        // Find the "OFF" button label element and check if it is clicked
        WebElement offButtonLabel = driver.findElement(By.cssSelector("span.btn-group-toggle label.btn:nth-of-type(1)"));
        boolean isOffButtonClicked = offButtonLabel.getAttribute("class").contains("btn active");
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

//        List<WebElement> cols = rows.get(0).findElements(By.xpath(".//td"));
//        int colsize = cols.size();
      
        String owningTeam = "";
        String sev, title, owner;
        int idCol,sevCol,ownTeamCol,titleCol, ownCol;
        idCol = DynamicColumns.checkNewIncidents("ID", driver);
        String incidentID =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(idCol)+"]")).getText();
        ownTeamCol = DynamicColumns.checkNewIncidents("Owning Team", driver);
        owningTeam = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(ownTeamCol)+"]")).getText();
        sevCol = DynamicColumns.checkNewIncidents("Severity", driver);
        sev = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(sevCol)+"]")).getText();
        titleCol = DynamicColumns.checkNewIncidents("Title", driver);
        title = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(titleCol)+"]")).getText();
        ownCol = DynamicColumns.checkNewIncidents("Owner", driver);
        owner = driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(ownCol)+"]")).getText();
        System.out.println(title);
        System.out.println(owner);
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
                    	if(!owner.contains("v-")) {
                    		if(!newIncident.equals(tillHere)) {
                        		latest = newIncident;
                        		
                        		driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td[1]")).click();
                        		driver.findElement(By.xpath("//*[@id=\"skip-to-main\"]/ui-view/ui-view/icm-collapsible-panels/main-panel/searchresults/ul/li[6]/div/command-buttons-addnl/delayload/incident-actionbuttons/div/div/div[2]/acknowledgeincident/button")).click();
                        		System.out.println("New Incident came: "+newIncident);
                        		System.out.println("Owning Team: "+owningTeam);
                        		System.out.println("Severity: "+sev);
                        		if(title.contains("[CatchPoint]")) {
                        			speak("catchpoint.mp3");
                        			speak("acknow.mp3");
                        		}
                        		else {
                        			speak("new_Arrived.mp3");
                        			speak("acknow.mp3");
    							}
//                        		System.out.println("Acknowledged");
                        	}
                    		else {
                        		flag = false;
                        	}
                    	}
                    	else {
                    		speak("new_Arrived.mp3");
                    		speak("alreadyAck.mp3");
                    	}
                    	
                	}
                	else {
                		speak("notLive.mp3");
                	}
                	
                }
            }
            
        	Thread.sleep(4000);
        	
        }

    }
    
    public static void speak(String fName) throws FileNotFoundException, JavaLayerException {
    	
    	String fileName = fName;
    	URL resource = ICMAckScript.class.getResource(fileName);
    	if (resource == null) {
    	    System.err.println("File not found: " + fileName);
    	    return;
    	}

    	String filePath = resource.getFile();
    	voiceMod(filePath);
    	
    }
}





