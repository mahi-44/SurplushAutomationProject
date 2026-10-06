package com.surplush.objectrepositoryutility;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import surplushutility.Fileutility;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void doLogin(String email, String password) throws Exception {
        driver.get("https://www.surplush.co.uk/account/login");
        Thread.sleep(5000);
        js.executeScript("document.querySelectorAll('iframe').forEach(e=>e.remove());");

        System.out.println("On Login Page: " + driver.getCurrentUrl());

        // Email
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[type='email']")));
            emailField.click();
            emailField.clear();
            emailField.sendKeys(email);
            Thread.sleep(1000);
        // Password
        WebElement passField = driver.findElement(By.xpath("//input[@type='password']"));
        passField.clear();
        passField.sendKeys(password);
        Thread.sleep(1000);

        // Sign In button
        WebElement signInBtn = driver.findElement(By.xpath("//button[text()='Sign In' or contains(text(),'Sign In')]"));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", signInBtn);
        Thread.sleep(1000);
        js.executeScript("arguments[0].click();", signInBtn);
        System.out.println("Clicked Sign In");

        Thread.sleep(8000); // Wait for login
        System.out.println("After Login URL: " + driver.getCurrentUrl());
    
		
	}
    public void loginWithValidCredentials(String email) throws Exception {
        String password = "Mahesh44@Mahi";
        System.out.println("Re-login with same credentials - Email: " + email);
        doLogin(email, password);
    }
    
		
}
