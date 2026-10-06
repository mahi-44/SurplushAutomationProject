package com.surplush.objectrepositoryutility;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    WebDriver driver; WebDriverWait wait; JavascriptExecutor js;

    public ProductPage(WebDriver driver){
        this.driver=driver;
        wait=new WebDriverWait(driver, Duration.ofSeconds(20));
        js=(JavascriptExecutor)driver;
    }

    public void addToBasket() throws InterruptedException {
        // Your exact xpath from previous image
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("(//button[contains(.,'Add to Basket')])[5]")));

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        Thread.sleep(1500);
        js.executeScript("arguments[0].click();", btn);
        System.out.println("Clicked Add to Basket [5]");
        Thread.sleep(4000); // Wait for basket count to update
    }
    public String getProductPrice() {
        try{
            WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'£')][1] | //div[contains(@class,'price')][1] | //p[contains(text(),'£')]")));
            String p = price.getText().trim();
            System.out.println("Product Price: " + p);
            return p;
        }catch(Exception e){ return "£0.00"; }
    }

    public String getQuantity() {
        try{
            WebElement qty = driver.findElement(By.xpath("//input[@type='number' or contains(@class,'qty')] | //span[contains(@class,'quantity')]"));
            String q = qty.getAttribute("value");
            if(q==null || q.isEmpty()) q = qty.getText();
            if(q.isEmpty()) q = "1";
            System.out.println("Quantity: " + q);
            return q;
        }catch(Exception e){ return "1"; }
        
    }

   
}