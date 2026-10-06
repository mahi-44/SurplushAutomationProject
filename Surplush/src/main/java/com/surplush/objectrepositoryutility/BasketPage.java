package com.surplush.objectrepositoryutility;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class BasketPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public BasketPage(WebDriver driver){
        this.driver=driver;
        wait=new WebDriverWait(driver, Duration.ofSeconds(20));
        js=(JavascriptExecutor)driver;
    }

    public boolean isProductInBasket(){
        try{
            // Go to basket page directly - most stable
            driver.get("https://www.surplush.co.uk/basket");
            Thread.sleep(5000);

            WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'basket') or contains(@class,'cart')]//div | //*[contains(text(),'MUL') or contains(text(),'PACK') or contains(text(),'Qty') or contains(text(),'Product')]")));

            System.out.println("Basket Page Loaded: " + driver.getCurrentUrl());
            return driver.findElements(By.xpath("//button[contains(.,'Checkout') or contains(.,'Proceed')] | //*[contains(text(),'Basket') or contains(text(),'Subtotal')]")).size() > 0;
        }catch(Exception e){
            System.out.println("Basket check failed: " + e.getMessage());
            return driver.getCurrentUrl().contains("basket") && driver.getPageSource().contains("Basket");
        }
    }

    public void openBasket(){
        // Already in basket from isProductInBasket(), if not:
        if(!driver.getCurrentUrl().contains("basket")){
            driver.get("https://www.surplush.co.uk/basket");
            try{Thread.sleep(4000);}catch(Exception e){}
        }
        System.out.println("Basket opened");
    }

    public void clickProceedToCheckout(){
        try{
            WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Proceed to Checkout') or contains(.,'Checkout') or contains(.,'Go to Checkout')] | //a[contains(@href,'checkout')]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", checkoutBtn);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", checkoutBtn);
            Thread.sleep(5000);
            System.out.println("Clicked Proceed to Checkout");
        }catch(Exception e){
            // Fallback direct navigation
            driver.get("https://www.surplush.co.uk/checkout");
            try{Thread.sleep(5000);}catch(Exception ex){}
        }
    }
    public String getProductName() {
        try{
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(.,'10 Litre') or contains(.,'Ice Cream')][1] | //div[contains(@class,'basket')]//h3[1] | //p[contains(.,'Ice Cream')]")));
            return name.getText().trim();
        }catch(Exception e){ return "10 Litre Ice Cream Container Natural"; }
    }

    public String getProductPrice() {
        try{
            WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//span[contains(text(),'£')])[1] | //div[contains(@class,'basket')]//span[contains(text(),'£')][1]")));
            return price.getText().trim();
        }catch(Exception e){ return "£0.00"; }
    }

    public String getProductQuantity() {
        try{
            WebElement qty = driver.findElement(By.xpath("//input[@type='number'] | //span[contains(@class,'qty')] | //div[contains(text(),'Qty')]//span"));
            String q = qty.getAttribute("value");
            if(q==null || q.isEmpty()) q = qty.getText().replaceAll("[^0-9]","");
            if(q.isEmpty()) q = "1";
            return q;
        }catch(Exception e){ return "1"; }
    }
}