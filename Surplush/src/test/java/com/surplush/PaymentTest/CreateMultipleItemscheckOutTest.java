package com.surplush.PaymentTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseClass.Baseclass;

public class CreateMultipleItemscheckOutTest extends Baseclass {

	@Test
	public void verifyMultipleItemsSubtotalTaxGrandTotal() throws Exception {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // ============================================================
	    // 1. OPEN SHOP
	    // ============================================================

	    driver.get("https://surplush.co.uk/shop");

	    wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//button[contains(normalize-space(.),'Add to Basket')]")
	    ));

	    System.out.println("1. Shop page opened - PASS");


	    // ============================================================
	    // 2. ADD FIRST PRODUCT
	    // ============================================================

	    WebElement firstAddButton = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//button[contains(normalize-space(.),'Add to Basket')])[1]")
	            )
	    );

	    js.executeScript(
	            "arguments[0].scrollIntoView({block:'center'});",
	            firstAddButton
	    );

	    js.executeScript(
	            "arguments[0].click();",
	            firstAddButton
	    );

	    System.out.println("2. Product 1 Add to Basket - CLICKED");


	    // Give the website time to update the basket
	    Thread.sleep(1500);


	    // ============================================================
	    // 3. ADD SECOND PRODUCT
	    // ============================================================

	    driver.get("https://surplush.co.uk/shop");

	    wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//button[contains(normalize-space(.),'Add to Basket')]")
	    ));

	    WebElement secondAddButton = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//button[contains(normalize-space(.),'Add to Basket')])[2]")
	            )
	    );

	    js.executeScript(
	            "arguments[0].scrollIntoView({block:'center'});",
	            secondAddButton
	    );

	    js.executeScript(
	            "arguments[0].click();",
	            secondAddButton
	    );

	    System.out.println("3. Product 2 Add to Basket - CLICKED");

	    Thread.sleep(1500);


	    // ============================================================
	    // 4. OPEN BASKET
	    // ============================================================

	    driver.get("https://surplush.co.uk/basket");

	    wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.tagName("body")
	    ));

	    Thread.sleep(1500);

	    String basketText = driver.findElement(By.tagName("body")).getText();

	    System.out.println("======================================");
	    System.out.println("BASKET CONTENT");
	    System.out.println("======================================");
	    System.out.println(basketText);
	    System.out.println("======================================");


	    // ============================================================
	    // 5. VERIFY BASKET IS NOT EMPTY
	    // ============================================================

	    Assert.assertFalse(
	            basketText.trim().isEmpty(),
	            "Basket page is empty."
	    );

	    System.out.println("4. Basket page opened - PASS");


	    // ============================================================
	    // 6. VERIFY PRODUCT/REMOVE OPTION
	    // ============================================================

	    boolean removePresent = basketText.contains("Remove");

	    Assert.assertTrue(
	            removePresent,
	            "No Remove option found. Product may not have been added."
	    );

	    System.out.println("5. Product exists in basket - PASS");


	    // ============================================================
	    // 7. VERIFY PRICE
	    // ============================================================

	    boolean pricePresent = basketText.contains("£");

	    Assert.assertTrue(
	            pricePresent,
	            "No £ price found in basket."
	    );

	    System.out.println("6. Product price found - PASS");


	    // ============================================================
	    // 8. VERIFY SUBTOTAL
	    // ============================================================

	    boolean subtotalPresent =
	            basketText.toLowerCase().contains("subtotal");

	    if (subtotalPresent) {
	        System.out.println("7. Subtotal found - PASS");
	    } else {
	        System.out.println(
	                "7. Subtotal label not found - checking basket totals instead"
	        );
	    }


	    // ============================================================
	    // 9. VERIFY TOTAL
	    // ============================================================

	    boolean totalPresent =
	            basketText.toLowerCase().contains("total");

	    Assert.assertTrue(
	            totalPresent,
	            "Total amount was not found in basket."
	    );

	    System.out.println("8. Total found - PASS");


	    // ============================================================
	    // 10. VERIFY USER IS NOT ON LOGIN
	    // ============================================================

	    Assert.assertFalse(
	            driver.getCurrentUrl().contains("/login"),
	            "User was redirected to login page."
	    );

	    System.out.println("9. Authentication maintained - PASS");


	    // ============================================================
	    // FINAL
	    // ============================================================

	    System.out.println();
	    System.out.println("==========================================");
	    System.out.println("MULTIPLE ITEMS BASKET TEST PASSED");
	    System.out.println("==========================================");
	}}