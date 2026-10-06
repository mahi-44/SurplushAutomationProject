package com.surplush.objectrepositoryutility;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class ShopPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public ShopPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
    }

    // This is the method from your screenshot Line 20
    public void selectCategoryAndProduct() throws InterruptedException {
        System.out.println("== ShopPage: selectCategoryAndProduct() Started ==");

        // 1. Accept Cookies - from your Line 23 in old image
        try{
            WebElement acceptBtn = driver.findElement(By.xpath("//button[text()='Accept All']"));
            if(acceptBtn.isDisplayed()){
                js.executeScript("arguments[0].click();", acceptBtn);
                System.out.println("Cookie Accepted");
                Thread.sleep(1000);
            }
        }catch(Exception e){
            System.out.println("No cookie popup");
        }

        // 2. Wait for Shop products to load - "Showing 12 of 609 products"
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//img[contains(@src,'/_next/image')] | //div[contains(text(),'Showing')]")));
        Thread.sleep(3000);
        System.out.println("Shop Page Loaded: " + driver.getCurrentUrl());

        // 3. Select Category - Cleaning Products / Chemicals (if visible)
        // If you want specific category, uncomment this
        try{
            WebElement category = driver.findElement(By.xpath("//span[contains(text(),'Cleaning Products') or contains(text(),'Chemicals')]/ancestor::button[1] | //a[contains(text(),'Cleaning Products')]"));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", category);
            js.executeScript("arguments[0].click();", category);
            System.out.println("Category Selected");
            Thread.sleep(3000);
        }catch(Exception e){
            System.out.println("Using default category - All Products");
        }

        // 4. Click on Product - Goes to Product Detail Page
        // This is for your Line 24 in old image: img click
        try{
            // Click first product image to go to detail page
            WebElement firstProductImg = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//img[contains(@src,'/_next/image')])[1] | (//a[contains(@href,'/product/')])[1] | (//div[contains(@class,'product')]//img)[1]")));

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstProductImg);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", firstProductImg);
            Thread.sleep(5000);
            System.out.println("Clicked Product - Navigated to: " + driver.getCurrentUrl());
        }catch(Exception e){
            System.out.println("Product click failed: " + e.getMessage());
            // Fallback - click text link
            List<WebElement> products = driver.findElements(By.xpath("//a[contains(@href,'/product/')]"));
            if(products.size()>0){
                js.executeScript("arguments[0].click();", products.get(0));
                try{Thread.sleep(5000);}catch(Exception ex){}
            }
        }
    }

    
    public void addProductDirectlyFromListing() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(.,'Add to Basket')]")));

        WebElement addBtn5 = driver.findElement(By.xpath("(//button[contains(.,'Add to Basket')])[5]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", addBtn5);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", addBtn5);
        System.out.println("Clicked Add to Basket [5] from Shop Listing");
        Thread.sleep(3000);
    }
    public void searchAndSelectProduct(String productName) throws InterruptedException {
        System.out.println("Searching: " + productName);
        try{
            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='search' or contains(@placeholder,'Search') or @name='search']")));
            search.click();
            search.sendKeys(Keys.CONTROL + "a");
            search.sendKeys(Keys.DELETE);
            search.sendKeys(productName);
            search.sendKeys(Keys.ENTER);
            Thread.sleep(4000);
        }catch(Exception e){ System.out.println("Search skipped: " + e.getMessage()); }

        // Select Product - 10 Litre Ice Cream Container Natural
        WebElement prod = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(),'10 Litre') or contains(text(),'"+productName+"')][1] | //div[contains(@class,'product')]//h3[1]")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", prod);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", prod);
        System.out.println("Selected Product: " + productName);
    }

    public void selectProductByName(String productName) throws InterruptedException {
        searchAndSelectProduct(productName);
    }
}