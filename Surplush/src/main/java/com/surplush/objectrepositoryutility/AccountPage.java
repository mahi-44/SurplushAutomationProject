package com.surplush.objectrepositoryutility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AccountPage {
	WebDriver driver;

	public AccountPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@type='email']")
	WebElement email;
	@FindBy(xpath = "//input[@type='password']")
	WebElement pwd;
	@FindBy(xpath = "//button[@type='submit']")
	WebElement signIn;

	public void login(String e, String p) {
		sleep(3000);
		try {
			email.clear();
			email.sendKeys(e);
			pwd.clear();
			pwd.sendKeys(p);
			signIn.click();
		} catch (Exception ex) {
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys(e);
			driver.findElement(By.xpath("//input[@type='password']")).sendKeys(p);
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
		sleep(8000);
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
		}
	}

	public void logout() throws Exception {
		try {
			// Locator for the logout button/link
			By logoutButton = By.xpath("//a[@class='logout']");
			// Adjust the XPath above to match your actual application’s logout element

			// Perform the click
			driver.findElement(logoutButton).click();
			System.out.println("Logout performed successfully.");
		} catch (Exception e) {
			System.err.println("Failed to perform logout:");
			e.printStackTrace();
			throw e; // rethrow to fail the test if logout cannot be performed
		}
	}

}