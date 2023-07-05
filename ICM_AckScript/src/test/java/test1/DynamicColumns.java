package test1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DynamicColumns {
    static WebDriver driver;

    public static int checkNewIncidents(String search, WebDriver driver){
    	
        
     // Get the visible columns
        List<WebElement> visibleHeadcols = driver.findElements(By.xpath("//*[@id=\"searchresults\"]/div[2]/div/table/thead/tr/th"));
        int visibleColumnsCount = visibleHeadcols.size();
        int returningValue = 0;
        // Store the names of visible columns
        for (int j = 2; j <= visibleColumnsCount; ++j) {
        	String names = driver.findElement(By.xpath("/html/body/div[1]/div/main/div/ui-view/ui-view/icm-collapsible-panels/main-panel/searchresults/icm-collapsible-panels/main-panel/div[1]/div[2]/div/table/thead/tr/th[" + j + "]/a")).getText();
            if(search.equals(names)) {
            	returningValue = j;
            	break;
            }
        }
        
        return returningValue;
       
    }
}
