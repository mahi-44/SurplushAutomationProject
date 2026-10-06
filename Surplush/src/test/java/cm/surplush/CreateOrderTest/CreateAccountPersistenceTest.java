package cm.surplush.CreateOrderTest;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import BaseClass.Baseclass;

public class CreateAccountPersistenceTest extends Baseclass {

    String loginEmail = "mallisettymahesh10@gmail.com";
    String loginPassword = "Mahesh44@Mahi";
    String productName = "10 Inch White Round BioCane Plates";

    @Test
    public void verifyProductPersistsAfterLogoutReLogin() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // 1. Login with valid credentials
            driver.get("https://surplush.co.uk/login");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            Thread.sleep(4000);

            WebElement emailBox = getEmailFieldSafely(driver, js);
            emailBox.clear();
            emailBox.sendKeys(loginEmail);

            WebElement passBox = (WebElement) js.executeScript("return document.querySelector('input[type=\"password\"]')");
            passBox.clear();
            passBox.sendKeys(loginPassword);

            js.executeScript("let btns=document.querySelectorAll('button'); for(let b of btns){ let t=b.innerText.toLowerCase(); if(t.includes('sign in')||t.includes('log in')){ b.scrollIntoView({block:'center'}); b.click(); break; } }");
            Thread.sleep(6000);
            System.out.println("1. Login with valid credentials - DONE");

            // 2. Search for the specific product and add it to basket
            //    (using the proven ?search= approach instead of DOM-walking the full listing)
            driver.get("https://surplush.co.uk/shop?search=" + productName);
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            Thread.sleep(4000);

            String searchResultsText = driver.findElement(By.tagName("body")).getText();
            System.out.println("DEBUG - Search results for '" + productName + "':\n"
                + searchResultsText.substring(0, Math.min(500, searchResultsText.length())));

            Object addedResult = js.executeScript(
                "let btns=document.querySelectorAll('button'); " +
                "for(let b of btns){ " +
                "  if(b.innerText.toLowerCase().includes('add to basket') && b.offsetParent!==null){ " +
                "    b.scrollIntoView({block:'center'}); " +
                "    b.click(); " +
                "    return true; " +
                "  } " +
                "} " +
                "return false;"
            );
            Thread.sleep(4000);
            System.out.println("2. Add product (" + productName + ") to basket - clicked=" + addedResult);
            Assert.assertTrue(Boolean.TRUE.equals(addedResult),
                "Could not find/click 'Add to Basket' for search results of: " + productName);

            Thread.sleep(3000);

            // 3. Open the basket and confirm presence before logout
            driver.get("https://surplush.co.uk/basket");
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            Thread.sleep(4000);

            String basketTextBefore = driver.findElement(By.tagName("body")).getText();
            System.out.println("DEBUG - Full basket contents before logout:\n" + basketTextBefore);
            System.out.println("----------------------------------------");

            boolean isItemPresentBeforeLogout = basketTextBefore.contains("Remove");

            Assert.assertTrue(isItemPresentBeforeLogout,
                "Product not confirmed in basket before logout - cannot proceed with persistence check. "
                + "Full basket text printed above - check if it's actually empty, or shows a different product name.");
            System.out.println("Basket before logout contains a product? true");

            // 4. Clean session logout
            driver.manage().deleteAllCookies();
            driver.get("https://surplush.co.uk");
            Thread.sleep(4000);
            System.out.println("3. Logout of the account - DONE");

            // 5. Re-Login Sequence
            driver.get("https://surplush.co.uk/login");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            Thread.sleep(4000);

            WebElement emailBoxRe = getEmailFieldSafely(driver, js);
            emailBoxRe.clear();
            emailBoxRe.sendKeys(loginEmail);

            WebElement passBoxRe = (WebElement) js.executeScript("return document.querySelector('input[type=\"password\"]')");
            passBoxRe.clear();
            passBoxRe.sendKeys(loginPassword);

            js.executeScript("let btns=document.querySelectorAll('button'); for(let b of btns){ let t=b.innerText.toLowerCase(); if(t.includes('sign in')||t.includes('log in')){ b.scrollIntoView({block:'center'}); b.click(); break; } }");
            Thread.sleep(6000);
            System.out.println("4. Login again with the same credentials - DONE");

            // 6. Final verification loop
            driver.get("https://surplush.co.uk/basket");
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            Thread.sleep(4000);

            String basketTextAfter = driver.findElement(By.tagName("body")).getText();
            System.out.println("--- Basket Contents After Re-Login --- \n" + basketTextAfter);

            Assert.assertTrue(basketTextAfter.contains("Remove"),
                "Persistence Failure: Shopping cart was dropped between re-login actions.");
            System.out.println("5. PASS: Item successfully persisted in cart across login sessions!");

        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail("Test script execution flagged an unhandled failure: " + e.getMessage());
        }
    }

    private WebElement getEmailFieldSafely(org.openqa.selenium.WebDriver driver, JavascriptExecutor js) throws InterruptedException {
        for (int attempt = 0; attempt < 10; attempt++) {
            WebElement field = (WebElement) js.executeScript("return document.querySelector('input[type=\"email\"]')");
            if (field != null) {
                return field;
            }
            Thread.sleep(1000);
        }
        System.out.println("DEBUG - Email field not found after waiting. Current URL: " + driver.getCurrentUrl());
        String bodyText = driver.findElement(By.tagName("body")).getText();
        System.out.println("DEBUG - Page text snippet: " + bodyText.substring(0, Math.min(400, bodyText.length())));
        throw new RuntimeException("Email input field not found on page: " + driver.getCurrentUrl());
    }
}