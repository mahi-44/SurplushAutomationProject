package com.surplush.objectrepositoryutility;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class HomePage {
	WebDriver driver;
	JavascriptExecutor js;
	WebDriverWait wait;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.js = (JavascriptExecutor) driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		PageFactory.initElements(driver, this);
	}

	// Surplush header has Account icon - TOP RIGHT
	@FindBy(xpath = "//a[contains(@href,'/login') or contains(@href,'/account')] | //header//a[contains(@href,'login')] | //button[contains(@class,'account')] | //a[contains(text(),'Account')] | //*[contains(@class,'user')]//ancestor::a | //header//button[contains(@class,'user')]")
	private WebElement profileIcon;

	@FindBy(xpath = "//button[contains(text(),'Accept') or contains(text(),'Got it') or contains(@id,'cookie')]")
	private WebElement cookieBtn;

	// LOGIN PAGE ELEMENTS - More generic
	@FindBy(xpath = "//input[@type='email']")
	private WebElement emailInput;

	@FindBy(xpath = "//input[@type='password']")
	private WebElement pwdInput;

	@FindBy(xpath = "//button[@type='submit'] | //form//button[contains(.,'Log') or contains(.,'Sign')] | //button[contains(@class,'login')]")
	private WebElement loginBtn;

	public void clickProfile() {
		try {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(cookieBtn));
				js.executeScript("arguments[0].click();", cookieBtn);
			} catch (Exception e) {
			}

			js.executeScript("window.scrollTo(0,0);");
			Thread.sleep(1500);

			// If profile icon not found, go direct
			try {
				wait.until(ExpectedConditions.visibilityOf(profileIcon));
				System.out.println("Found profile: " + profileIcon.getAttribute("outerHTML").substring(0, 100));
				js.executeScript("arguments[0].scrollIntoView(true);", profileIcon);
				Thread.sleep(500);
				js.executeScript("arguments[0].click();", profileIcon);
				Thread.sleep(4000);
				System.out.println("After click URL: " + driver.getCurrentUrl());
			} catch (Exception e) {
				System.out.println("Profile icon not found, going direct to /login");
				driver.get("https://www.surplush.co.uk/login");
				Thread.sleep(4000);
			}

			// If still not on login page, force navigate
			if (!driver.getCurrentUrl().contains("login") && !driver.getCurrentUrl().contains("account")) {
				System.out.println("Forcing login page - Current URL: " + driver.getCurrentUrl());
				driver.get("https://www.surplush.co.uk/login");
				Thread.sleep(4000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(String email, String password) {
		try {
			wait.until(ExpectedConditions.visibilityOf(emailInput));
			emailInput.clear();
			emailInput.sendKeys(email);
			System.out.println("Email entered");

			pwdInput.clear();
			pwdInput.sendKeys(password);
			System.out.println("Password entered");

			js.executeScript("arguments[0].scrollIntoView({block:'center'});", loginBtn);
			js.executeScript("arguments[0].click();", loginBtn);
			Thread.sleep(6000);
			System.out.println("Login clicked, now URL: " + driver.getCurrentUrl());

		} catch (Exception e) {
			System.out.println("Login failed final: " + e.getMessage());
			// Debug - print page source snippet
			System.out.println(driver.getPageSource().substring(0, 500));
		}
	}

	public void navigateToShop() {
		driver.get("https://www.surplush.co.uk/shop");
		try {
			Thread.sleep(4000);
		} catch (Exception e) {
		}
	}

	public void logout() throws Exception {
	    System.out.println("Logging out...");
	    try {
	        // Direct Shopify logout URL - this always signs out
	        driver.get("https://www.surplush.co.uk/account/logout");
	        Thread.sleep(5000);
	        System.out.println("After logout URL: " + driver.getCurrentUrl());

	        // If still logged in, try second URL
	        if(driver.getCurrentUrl().contains("/account")) {
	            driver.get("https://www.surplush.co.uk/");
	            Thread.sleep(2000);
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("document.querySelectorAll('iframe').forEach(e=>e.remove());");
	            Thread.sleep(1000);
	            // Click logout link if present
	            try {
	                WebElement logoutLink = driver.findElement(By.xpath("//a[contains(@href,'logout') or contains(text(),'Log out')]"));
	                js.executeScript("arguments[0].click();", logoutLink);
	                Thread.sleep(4000);
	            } catch(Exception e) {
	                System.out.println("Logout link not found, already logged out via URL");
	            }
	        }
	    } catch(Exception e) {
	        System.out.println("Logout error: " + e.getMessage());
	        driver.get("https://www.surplush.co.uk/account/logout");
	        Thread.sleep(5000);
	    }
	    System.out.println("Logged out successfully - Current URL: " + driver.getCurrentUrl());
	}

	public void openHome() {
		try {
			driver.get("https://surplush.co.uk");
			wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
			System.out.println("Successfully opened Home Page.");
		} catch (Exception e) {
			System.err.println("Failed to open Home Page:");
			e.printStackTrace();
			throw e;
		}
	}

	public void clickLoginIcon() throws Exception {
		try {
			// Locator for the login icon/button
			By loginIcon = By.xpath("//a[@class='login']");
			// Adjust the XPath above to match your actual application’s login element

			// Perform the click
			driver.findElement(loginIcon).click();
			System.out.println("Login icon clicked successfully.");
		} catch (Exception e) {
			System.err.println("Failed to click Login Icon:");
			e.printStackTrace();
			throw e; // rethrow to fail the test if login icon cannot be clicked
		}
	}

}
