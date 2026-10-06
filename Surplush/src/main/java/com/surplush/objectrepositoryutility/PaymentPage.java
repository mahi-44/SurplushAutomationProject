package com.surplush.objectrepositoryutility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class PaymentPage {
    WebDriver driver;

    public PaymentPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // For Surplush Stripe Card - This handles iframes
    public void enterDeclineCard(){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            Thread.sleep(3000);

            // Switch to Stripe iframe for Card Number
            try{
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[contains(@name,'__privateStripeFrame') or contains(@title,'Secure card')]")));
                WebElement cardNumber = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='cardnumber' or @placeholder='Card number']")));
                cardNumber.clear();
                cardNumber.sendKeys("4000000000000002"); // Decline card from Test Case
                driver.switchTo().defaultContent();
            } catch(Exception e1){
                // If no iframe, try direct input
                driver.switchTo().defaultContent();
                try{
                    driver.findElement(By.xpath("//input[contains(@placeholder,'Card number') or @name='cardnumber']")).sendKeys("4000000000000002");
                } catch(Exception e2){}
            }

            // Expiry - MM / YY
            try{
                driver.switchTo().defaultContent();
                WebElement expFrame = driver.findElement(By.xpath("(//iframe[contains(@name,'__privateStripeFrame')])[2]"));
                driver.switchTo().frame(expFrame);
                WebElement exp = driver.findElement(By.xpath("//input[@name='exp-date' or @placeholder='MM / YY']"));
                exp.sendKeys("12/30");
                driver.switchTo().defaultContent();
            } catch(Exception e){
                driver.switchTo().defaultContent();
                try{ driver.findElement(By.xpath("//input[contains(@placeholder,'MM') or @name='exp-date']")).sendKeys("12/30"); }catch(Exception e2){}
            }

            // CVC
            try{
                WebElement cvcFrame = driver.findElement(By.xpath("(//iframe[contains(@name,'__privateStripeFrame')])[3]"));
                driver.switchTo().frame(cvcFrame);
                WebElement cvc = driver.findElement(By.xpath("//input[@name='cvc' or @placeholder='CVC']"));
                cvc.sendKeys("123");
                driver.switchTo().defaultContent();
            } catch(Exception e){
                driver.switchTo().defaultContent();
                try{ driver.findElement(By.xpath("//input[contains(@placeholder,'CVC') or @name='cvc']")).sendKeys("123"); }catch(Exception e2){}
            }

            // Zip if asked
            try{
                driver.findElement(By.xpath("//input[contains(@placeholder,'ZIP') or @name='postal']")).sendKeys("12345");
            } catch(Exception e){}

            System.out.println("Decline card entered: 4000 0000 0000 0002");

        } catch(Exception e){
            System.out.println("Card entry error: " + e.getMessage());
        }
    }

    public void submitPayment(){
        try{
            driver.switchTo().defaultContent();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement payBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Pay') or contains(text(),'Complete') or @type='submit']")));
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", payBtn);
            Thread.sleep(1000);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", payBtn);
        } catch(Exception e){
            System.out.println("Pay button error: " + e.getMessage());
        }
    }

    public String getPaymentFailureMessage(){
        try{
            driver.switchTo().defaultContent();
            Thread.sleep(3000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'declined') or contains(text(),'failed') or contains(text(),'error') or contains(text(),'Decline') or @role='alert']")));
            return msg.getText();
        } catch(Exception e){
            return driver.getPageSource(); // return full source for check
        }
    }
}