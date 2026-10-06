package surplushutility;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class webdriverutility {
    public void maximizeWindow(WebDriver driver){ driver.manage().window().maximize(); }
    public void waitForPageLoad(WebDriver driver){ driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); }
    public void waitForElementClickable(WebDriver driver, WebElement ele){
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.elementToBeClickable(ele));
    }
    public void jsClick(WebDriver driver, WebElement ele){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
    }
    public void handleCookiePopup(WebDriver driver){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement accept = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Accept All')]")));
            jsClick(driver, accept);
        }catch(Exception e){}
    }
    public void waitAndClick(WebDriver driver, WebElement ele){
        try{
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(ele)).click();
        }catch(Exception e){ jsClick(driver, ele); }
    }
}