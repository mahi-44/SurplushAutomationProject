package com.srplush.LoginTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseClass.Baseclass;

public class CreateProductDetailsPageTest extends Baseclass {

    String productName = "1 Litre Ice Cream Container Natural";

    @Test
    public void verifyProductDetailsPageOpensCorrectly() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            // 1. Open Surplush URL — already handled by Baseclass @BeforeClass/@BeforeMethod

            // 2. Click on SHOP
            js.executeScript("let links=document.querySelectorAll('a'); for(let l of links){ if(l.innerText.trim().toUpperCase()=='SHOP'){ l.click(); break; } }");
            Thread.sleep(5000);
            System.out.println("2. Click on SHOP - DONE");

            // 3. Browse the product listing
            String listingText = driver.findElement(By.tagName("body")).getText();
            System.out.println("3. Browse the product listing - DONE");
            Assert.assertTrue(listingText.contains("£"), "Product listing does not appear to show any priced products");

            // 4. Select a specific product (e.g. 1 Litre Ice Cream Container Natural)
            Object clicked = js.executeScript(
                "let keyword = arguments[0].toLowerCase();" +
                "let els = document.querySelectorAll('a, div, h2, h3, p');" +
                "for (let el of els) {" +
                "  if (el.offsetParent !== null && el.children.length === 0 && el.innerText && el.innerText.toLowerCase().includes(keyword)) {" +
                "    el.scrollIntoView({block:'center'});" +
                "    el.click();" +
                "    return true;" +
                "  }" +
                "}" +
                "return false;",
                productName
            );
            Thread.sleep(4000);
            System.out.println("4. Select a specific product (" + productName + "): clicked=" + clicked);
            Assert.assertTrue(Boolean.TRUE.equals(clicked), "Could not find/click product '" + productName + "' on the listing page");

            // 5. Verify the correct product details page opens with matching name, image and price
            String detailsPageText = driver.findElement(By.tagName("body")).getText();
            System.out.println("5. Product details page text:\n" + detailsPageText);

            boolean nameMatches = detailsPageText.toLowerCase().contains("ice cream")
                && detailsPageText.toLowerCase().contains("1 litre");
            boolean priceShown = detailsPageText.contains("£");

            int imageCount = ((Long) js.executeScript(
                "return document.querySelectorAll('img').length;"
            )).intValue();
            boolean imagePresent = imageCount > 0;

            System.out.println("5. Name matches: " + nameMatches + " | Price shown: " + priceShown + " | Image(s) present: " + imagePresent + " (" + imageCount + " img tags)");

            Assert.assertTrue(nameMatches, "Product details page does not show the expected product name '" + productName + "'");
            Assert.assertTrue(priceShown, "Product details page does not show any price (£)");
            Assert.assertTrue(imagePresent, "Product details page does not show any product image");

            System.out.println("5. PASS: Correct product details page opened with matching name, image and price");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("ProductDetailsPageTest failed: " + e.getMessage());
        }
    }
}