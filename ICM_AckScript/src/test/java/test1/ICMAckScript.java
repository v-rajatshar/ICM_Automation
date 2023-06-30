package test1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ICMAckScript {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.edge.driver", "D:\\Rajat\\selenium\\Drivers\\edgedriver_win64\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://portal.microsofticm.com/imp/v3/incidents/search/advanced?sl=0pkusngqfou");

        WebDriverWait wait = new WebDriverWait(driver, 15); // Set the maximum wait time as needed

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
      
        String incidentID =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(colsize-10)+"]")).getText();
    	System.out.println(incidentID);

    	String latest = incidentID;
    	
        while(true) {
        	System.out.println("Inside Loop");
        	System.out.println("Entered and Started");
        	driver.navigate().refresh();
        	System.out.println("refreshed");
        	Thread.sleep(15000);
        	List<WebElement>  rws = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr"));
            rsize = rws.size();
            System.out.println(rsize);
            List<WebElement> columns = driver.findElements(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td"));
            colsize = columns.size();
            System.out.println(colsize);
           
            String newIncident =  driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr["+(rsize-99)+"]/td["+(colsize-10)+"]")).getText();
        	System.out.println(newIncident);
        	if(!newIncident.equals(latest)) {
        		System.out.println("New Incident came");
        		latest = newIncident;
        		driver.findElement(By.xpath("//div[@class='k-grid-content k-auto-scrollable']/table//tr[1]/td[1]")).click();
        		driver.findElement(By.xpath("//*[@id=\"skip-to-main\"]/ui-view/ui-view/icm-collapsible-panels/main-panel/searchresults/ul/li[6]/div/command-buttons-addnl/delayload/incident-actionbuttons/div/div/div[2]/acknowledgeincident/button")).click();
        		System.out.println("Acknowledged!");
        	}
        	else {
        		System.out.println("No New Incident");
        	}
        	
        	Thread.sleep(10000);
        	
        }
    }
}





