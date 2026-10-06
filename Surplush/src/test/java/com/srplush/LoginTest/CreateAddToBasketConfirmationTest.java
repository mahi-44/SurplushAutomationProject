package com.srplush.LoginTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseClass.Baseclass;

public class CreateAddToBasketConfirmationTest extends Baseclass {

    @Test
    public void verifyAddToBasketConfirmation() throws Exception {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            // =====================================================
            // 1. Click on SHOP
            // =====================================================

            js.executeScript(
                "let links = document.querySelectorAll('a');" +
                "for(let l of links) {" +
                "   if((l.textContent || '').trim().toUpperCase() == 'SHOP') {" +
                "       l.click();" +
                "       return true;" +
                "   }" +
                "}" +
                "return false;"
            );

            Thread.sleep(5000);

            System.out.println("1. Click on SHOP - DONE");


            // =====================================================
            // 2. Find Add to Basket button
            // =====================================================

            Object productFound = js.executeScript(
                "let buttons = document.querySelectorAll('button');" +
                "for(let b of buttons) {" +
                "   let text = (b.textContent || '').trim().toLowerCase();" +
                "   if(text.includes('add to basket') && b.offsetParent !== null) {" +
                "       return true;" +
                "   }" +
                "}" +
                "return false;"
            );

            System.out.println(
                "2. Add to Basket button found: " + productFound
            );

            Assert.assertTrue(
                Boolean.TRUE.equals(productFound),
                "No Add to Basket button found"
            );


            // =====================================================
            // 3. Click Add to Basket
            // =====================================================

            Object clicked = js.executeScript(
                "let buttons = document.querySelectorAll('button');" +
                "for(let b of buttons) {" +
                "   let text = (b.textContent || '').trim().toLowerCase();" +
                "   if(text.includes('add to basket') && b.offsetParent !== null) {" +
                "       b.scrollIntoView({block:'center'});" +
                "       b.click();" +
                "       return true;" +
                "   }" +
                "}" +
                "return false;"
            );

            System.out.println(
                "3. Add to Basket clicked: " + clicked
            );

            Assert.assertTrue(
                Boolean.TRUE.equals(clicked),
                "Could not click Add to Basket button"
            );

            Thread.sleep(4000);


            // =====================================================
            // 4. Find Basket
            // =====================================================

            Object basketFound = js.executeScript(
                "let elements = document.querySelectorAll('a, button');" +
                "for(let e of elements) {" +
                "   let text = (e.textContent || '').trim().toLowerCase();" +
                "   if(text.includes('basket') && e.offsetParent !== null) {" +
                "       return true;" +
                "   }" +
                "}" +
                "return false;"
            );

            System.out.println(
                "4. Basket element found: " + basketFound
            );

            Assert.assertTrue(
                Boolean.TRUE.equals(basketFound),
                "Basket element was not found"
            );


            // =====================================================
            // 5. Click Basket
            // =====================================================

            Object basketClicked = js.executeScript(
                "let elements = document.querySelectorAll('a, button');" +
                "for(let e of elements) {" +
                "   let text = (e.textContent || '').trim().toLowerCase();" +
                "   if(text.includes('basket') && e.offsetParent !== null) {" +
                "       e.scrollIntoView({block:'center'});" +
                "       e.click();" +
                "       return true;" +
                "   }" +
                "}" +
                "return false;"
            );

            System.out.println(
                "5. Basket clicked: " + basketClicked
            );

            Assert.assertTrue(
                Boolean.TRUE.equals(basketClicked),
                "Could not click Basket"
            );

            Thread.sleep(4000);


            // =====================================================
            // 6. Get complete basket page text
            // =====================================================

            String basketText = driver
                    .findElement(By.tagName("body"))
                    .getText();

            System.out.println(
                "6. Basket page opened - DONE"
            );

            System.out.println(
                "Basket page text:\n" + basketText
            );


            // =====================================================
            // 7. Verify basket is NOT empty
            // =====================================================

            boolean basketIsEmpty =
                    basketText.toLowerCase().contains("your basket is empty")
                    ||
                    basketText.toLowerCase().contains("basket is empty");


            Assert.assertFalse(
                basketIsEmpty,
                "Basket is empty - product was not added"
            );


            // =====================================================
            // 8. Test Passed
            // =====================================================

            System.out.println(
                "7. PASS: Product successfully added to Basket"
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            Assert.fail(
                "CreateAddToBasketConfirmationTest failed: "
                + e.getMessage()
            );
        }
    }
}