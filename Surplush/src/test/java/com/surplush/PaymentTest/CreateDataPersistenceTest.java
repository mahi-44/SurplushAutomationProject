package com.surplush.PaymentTest;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.surplush.objectrepositoryutility.BasketPage;
import com.surplush.objectrepositoryutility.HomePage;
import com.surplush.objectrepositoryutility.LoginPage;
import com.surplush.objectrepositoryutility.ProductPage;
import com.surplush.objectrepositoryutility.ShopPage;

import BaseClass.Baseclass;

public class CreateDataPersistenceTest extends Baseclass {

    @Test
    public void testBasketPersistenceAfterLogoutLogin() throws Exception {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String expectedProduct = "10 Litre Ice Cream Container Natural";
        String USERNAME = "mallisettymahesh10@gmail.com";
        String PASSWORD = "Mahesh44@Mahi";

        HomePage home = new HomePage(driver);
        ShopPage shop = new ShopPage(driver);
        ProductPage product = new ProductPage(driver);
        BasketPage basket = new BasketPage(driver);
        LoginPage login = new LoginPage(driver);

        // 1. Login with valid credentials - Already done in Baseclass
        System.out.println("1. Login done with valid credentials: " + USERNAME);

        // 2. Add a product (10 Litre Ice Cream Container Natural) to basket
        home.navigateToShop();
        shop.searchAndSelectProduct(expectedProduct);

        String priceBefore = product.getProductPrice();
        String qtyBefore = product.getQuantity();
        System.out.println("Product Price Before Logout: " + priceBefore);

        product.addToBasket();
        System.out.println("2. Added Product to basket: " + expectedProduct);
        Thread.sleep(3000);

        // Save basket details before logout
        basket.openBasket();
        String basketProductName = basket.getProductName();
        String basketProductPrice = basket.getProductPrice();
        String basketQty = basket.getProductQuantity();
        System.out.println("Basket Before Logout - Product: " + basketProductName + " | Qty: " + basketQty + " | Price: " + basketProductPrice);

        Assert.assertTrue(basketProductName != null && basketProductName.toLowerCase().contains("ice cream"),
            "Product was not confirmed in basket before logout - cannot proceed with persistence check");

        // 3. Logout of the account (JS-based — avoids relying on an unverified HomePage.logout() method)
        try {
            driver.get("https://surplush.co.uk/logout");
            Thread.sleep(3000);
        } catch (Exception e) {
            try {
                js.executeScript(
                    "let els=document.querySelectorAll('button, a, img'); " +
                    "for(let b of els){ let t=(b.innerText||'').toLowerCase(); " +
                    "if(t.includes('logout') || t.includes('log out')){ b.click(); break; } }"
                );
                Thread.sleep(3000);
            } catch (Exception ex) {
                driver.manage().deleteAllCookies();
                driver.get("https://surplush.co.uk/login");
                Thread.sleep(3000);
            }
        }
        System.out.println("3. Logged out of the account");

        // 4. Login again with the same credentials (JS-based — avoids relying on an
        //    unverified LoginPage.loginWithValidCredentials(String) method)
        driver.get("https://surplush.co.uk/login");
        Thread.sleep(4000);

        WebElement emailBox = (WebElement) js.executeScript("return document.querySelector('input[type=\"email\"]')");
        emailBox.clear();
        emailBox.sendKeys(USERNAME);
        Thread.sleep(800);

        WebElement passBox = (WebElement) js.executeScript("return document.querySelector('input[type=\"password\"]')");
        passBox.clear();
        passBox.sendKeys(PASSWORD);
        Thread.sleep(800);

        js.executeScript(
            "let btns=document.querySelectorAll('button'); for(let b of btns){ " +
            "let t=b.innerText.toLowerCase(); if(t.includes('log in')||t.includes('sign in')){ b.click(); break; } }"
        );
        Thread.sleep(6000);
        System.out.println("4. Login again with same credentials: " + USERNAME);

        // 5. Open the basket
        home.navigateToShop(); // to get header
        basket.openBasket();
        System.out.println("5. Opened Basket after Re-Login");
        Thread.sleep(3000);

        // 6. Verify the previously added product, quantity and pricing are still present and correct
        String productAfter = basket.getProductName();
        String priceAfter = basket.getProductPrice();
        String qtyAfter = basket.getProductQuantity();

        System.out.println("==============================================");
        System.out.println("VERIFICATION AFTER RE-LOGIN");
        System.out.println("Product: " + productAfter);
        System.out.println("Quantity: " + qtyAfter);
        System.out.println("Pricing: " + priceAfter);
        System.out.println("==============================================");

        Assert.assertTrue(productAfter.contains("10 Litre") && productAfter.contains("Ice Cream"), "Product name mismatch after re-login!");
        Assert.assertEquals(qtyAfter, qtyBefore, "Quantity not persisted after re-login!");
        Assert.assertEquals(priceAfter, basketProductPrice, "Pricing not persisted after re-login!");

        System.out.println("PASS: Basket Persistence Verified - Product, Quantity and Pricing are still present and correct");
        System.out.println("JOB DONE FOR THIS TEST SCRIPT");
    }
}