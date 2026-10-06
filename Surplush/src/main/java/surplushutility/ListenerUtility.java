package surplushutility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import BaseClass.Baseclass;

public class ListenerUtility implements ITestListener, ISuiteListener {

    // ============================================================
    // IStart - Suite starts
    // ============================================================

    @Override
    public void onStart(ISuite suite) {
        System.out.println("======================================");
        System.out.println("SUITE STARTED : " + suite.getName());
        System.out.println("======================================");
    }

    // ============================================================
    // IFinish - Suite finishes
    // ============================================================

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("======================================");
        System.out.println("SUITE FINISHED : " + suite.getName());
        System.out.println("======================================");
    }

    // ============================================================
    // Test Starts
    // ============================================================

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("TEST STARTED : " + result.getName());
    }

    // ============================================================
    // Test Passed
    // ============================================================

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("TEST PASSED : " + result.getName());
    }

    // ============================================================
    // Test Failed
    // ============================================================

    @Override
    public void onTestFailure(ITestResult result) {

        System.out.println("TEST FAILED : " + result.getName());

        try {

            // Get current test class object
            Baseclass baseClass = (Baseclass) result.getInstance();

            // Get WebDriver
            WebDriver driver = baseClass.getDriver();

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;

            File source = ts.getScreenshotAs(OutputType.FILE);

            // Screenshot location
            File destination = new File("./screenshots/"
                + result.getTestClass().getName() + "." + result.getName() + "_"
                + System.currentTimeMillis() + ".png");

            // Create folder if it doesn't exist
            destination.getParentFile().mkdirs();

            // Copy screenshot
            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Screenshot saved at: " + destination.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Unable to take screenshot");
            e.printStackTrace();
        }
    } // <-- onTestFailure closes HERE

    // ============================================================
    // Test Skipped
    // ============================================================

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("TEST SKIPPED : " + result.getName());
    }
}