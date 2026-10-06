package cm.surplush.CreateOrderTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseClass.Baseclass;

public class createBasketFinalOrderTest extends Baseclass {

    JavascriptExecutor js;
    String loginEmail = "mallisettymahesh10@gmail.com";
    String loginPassword = "Mahesh44@Mahi";

    @Test
    public void verifyFullLoginToCheckoutFlowStaysAuthenticated() throws Exception {
        js = (JavascriptExecutor) driver;
        try {
            // 1. Login with valid credentials
            driver.get("https://surplush.co.uk/login");
            Thread.sleep(5000);
            WebElement emailBox = (WebElement) js.executeScript("return document.querySelector('input[type=\"email\"]')");
            emailBox.clear(); emailBox.sendKeys(loginEmail); Thread.sleep(800);
            WebElement passBox = (WebElement) js.executeScript("return document.querySelector('input[type=\"password\"]')");
            passBox.clear(); passBox.sendKeys(loginPassword); Thread.sleep(800);
            js.executeScript("let btns=document.querySelectorAll('button'); for(let b of btns){ let t=b.innerText.toLowerCase(); if(t.includes('log in')||t.includes('sign in')){ b.click(); break; } }");
            Thread.sleep(6000);
            try { driver.switchTo().activeElement().sendKeys(Keys.ESCAPE); } catch (Exception ignored) {}
            System.out.println("1. Login with valid credentials - DONE");

            // 2. Search or browse to a specific product
            driver.get("https://surplush.co.uk/shop");
            Thread.sleep(5000);
            System.out.println("2. Browse to Shop - DONE");

            // 3. Open the product details page
            Object productClicked = js.executeScript(
                "let els = document.querySelectorAll('a, div, h2, h3, p');" +
                "let navWords = ['home','shop','locations','login','sign in','cart','basket','about','contact'];" +
                "for (let el of els) {" +
                "  if (el.offsetParent === null || el.children.length !== 0) continue;" +
                "  let text = el.innerText ? el.innerText.trim() : '';" +
                "  if (text.length <= 3) continue;" +
                "  if (navWords.includes(text.toLowerCase())) continue;" +
                "  " +
                "  let container = el.closest('div');" +
                "  let hasPrice = container && container.parentElement && container.parentElement.innerText.includes('£');" +
                "  if (hasPrice) {" +
                "    el.scrollIntoView({block:'center'});" +
                "    el.click();" +
                "    return text;" +
                "  }" +
                "}" +
                "return null;"
            );
            Thread.sleep(4000);
            System.out.println("3. Open the product details page - clicked element text: " + productClicked);
            Assert.assertNotNull(productClicked, "Could not locate/click any product on the listing");

            // 4. Click Add to Basket
            Object added = js.executeScript(
                "let btns=document.querySelectorAll('button'); for(let b of btns){ if(b.innerText.toLowerCase().includes('add to basket') && b.offsetParent!==null){ b.scrollIntoView({block:'center'}); b.click(); return true; } } return false;"
            );
            Thread.sleep(4000);
            System.out.println("4. Click Add to Basket - clicked=" + added);
            Assert.assertTrue(Boolean.TRUE.equals(added), "Could not find/click Add to Basket on product details page");

            // 5. Open basket and verify product is listed correctly
            driver.get("https://surplush.co.uk/basket");
            Thread.sleep(5000);
            String basketText = driver.findElement(By.tagName("body")).getText();
            System.out.println("5. Basket contents:\n" + basketText);
            Assert.assertTrue(basketText.contains("Remove"), "Basket appears empty - no 'Remove' link found, product not listed");
            Assert.assertTrue(basketText.contains("£"), "Basket does not show any pricing");
            System.out.println("5. PASS: Product is listed in basket correctly");

            // 6. Proceed to Checkout
            WebElement checkoutSpan = driver.findElement(By.xpath("//span[text()='Proceed to Checkout']"));
            checkoutSpan.click();
            Thread.sleep(5000);
            String checkoutUrl = driver.getCurrentUrl();
            System.out.println("6. Proceed to Checkout - DONE, URL: " + checkoutUrl);
            Assert.assertTrue(checkoutUrl.contains("checkout") || checkoutUrl.contains("order"),
                "Did not navigate to checkout/order page. Current URL: " + checkoutUrl);

            // 7. Verify user remains authenticated throughout (no forced re-login)
            String checkoutPageText = driver.findElement(By.tagName("body")).getText();
            boolean forcedBackToLogin = checkoutUrl.contains("/login")
                || (checkoutPageText.toLowerCase().contains("sign in")
                    && checkoutPageText.toLowerCase().contains("enter your email"));

            System.out.println("7. Checkout URL contains '/login'? " + checkoutUrl.contains("/login"));
            Assert.assertFalse(forcedBackToLogin,
                "User appears to have been forced back to login during checkout - session was not maintained");

            System.out.println("7. PASS: User remained authenticated throughout the full flow - no forced re-login");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("createBasketFinalOrderTest failed: " + e.getMessage());
        }
    }
}