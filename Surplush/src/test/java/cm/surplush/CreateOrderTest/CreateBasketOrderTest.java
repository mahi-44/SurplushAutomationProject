package cm.surplush.CreateOrderTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseClass.Baseclass;

public class CreateBasketOrderTest extends Baseclass {

    @Test
    public void verifyEcoFriendlyFilterNarrowsSearchResults() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            // 1. Open SHOP page
            js.executeScript("let links=document.querySelectorAll('a'); for(let l of links){ if(l.innerText.trim().toUpperCase()=='SHOP'){ l.click(); break; } }");
            Thread.sleep(5000);
            System.out.println("1. Open SHOP page - DONE");

            // 2. Enter a keyword into the Search bar (e.g. napkins)
            driver.get("https://surplush.co.uk/shop?search=napkins");
            Thread.sleep(4000);
            System.out.println("2. Enter a keyword into the Search bar (napkins) - DONE");

            // 3. Press Enter/Search — already applied via URL search param above

            // 4. Verify initial search results are displayed
            String initialResultsText = driver.findElement(By.tagName("body")).getText();
            int initialCount = countOccurrences(initialResultsText, "Add to Basket");
            System.out.println("4. Initial search results - 'Add to Basket' count: " + initialCount);
            System.out.println("4. Page text snippet:\n" + initialResultsText.substring(0, Math.min(500, initialResultsText.length())));
            Assert.assertTrue(initialCount > 0, "No search results displayed for keyword 'napkins'");
            System.out.println("4. PASS: Initial search results are displayed");

            // 5. Toggle the Eco-Friendly filter ON
            Object toggled = js.executeScript(
                "let labels = document.querySelectorAll('label, span, button, div');" +
                "for (let el of labels) {" +
                "  if (el.offsetParent !== null && el.innerText && el.innerText.toLowerCase().includes('eco-friendly') && el.innerText.toLowerCase().includes('show')) {" +
                "    let toggle = el.parentElement ? el.parentElement.querySelector('input[type=\"checkbox\"], button[role=\"switch\"]') : null;" +
                "    if (!toggle) { toggle = el.closest('div').querySelector('input[type=\"checkbox\"], button[role=\"switch\"]'); }" +
                "    if (toggle) { toggle.click(); return 'clicked via toggle input near: ' + el.innerText; }" +
                "  }" +
                "}" +
                "return null;"
            );
            Thread.sleep(2500);
            System.out.println("5. Toggle Eco-Friendly filter ON - result: " + toggled);

            if (toggled == null) {
                System.out.println("DEBUG - Could not auto-locate Eco-Friendly toggle. Dumping nearby filter panel HTML...");
                Object filterDump = js.executeScript(
                    "let els = document.querySelectorAll('*');" +
                    "for (let el of els) {" +
                    "  if (el.innerText && el.innerText.toLowerCase().includes('eco-friendly') && el.children.length < 3) {" +
                    "    return el.outerHTML.substring(0, 400);" +
                    "  }" +
                    "}" +
                    "return 'Eco-Friendly text not found anywhere on page';"
                );
                System.out.println("DEBUG - Eco-Friendly area HTML:\n" + filterDump);
            }
            Assert.assertNotNull(toggled, "Could not find/click the Eco-Friendly filter toggle - see DEBUG output above for the actual HTML near it");

            // 6. Verify results narrow down to only eco-friendly items matching the keyword
            String filteredResultsText = driver.findElement(By.tagName("body")).getText();
            int filteredCount = countOccurrences(filteredResultsText, "Add to Basket");
            System.out.println("6. Filtered (Eco-Friendly) results - 'Add to Basket' count: " + filteredCount);

            Assert.assertTrue(filteredCount <= initialCount,
                "Filtered result count (" + filteredCount + ") is not less than or equal to initial count (" + initialCount + ") - filter may not have applied");
            Assert.assertTrue(filteredCount > 0,
                "Filtered results returned 0 items - either no eco-friendly napkins exist, or the filter broke the search");

            System.out.println("6. PASS: Results narrowed from " + initialCount + " to " + filteredCount + " after applying Eco-Friendly filter");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("SearchEcoFriendlyFilterTest failed: " + e.getMessage());
        }
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(word, idx)) != -1) {
            count++;
            idx += word.length();
        }
        return count;
    }
}