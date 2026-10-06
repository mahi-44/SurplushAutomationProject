package com.surplush.objectrepositoryutility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;

/**
 * Loads locators from objectRepository.properties and converts them
 * into Selenium By objects. Supports prefixes: id:, name:, css:, xpath: (default).
 */
public class ObjectRepositoryUtility {

    private static Properties properties;
    private static final String OR_PATH = System.getProperty("user.dir")
            + "/src/main/resources/objectRepository.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(OR_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not load objectRepository.properties: " + e.getMessage());
        }
    }

    public static By getLocator(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("No locator found in Object Repository for key: " + key);
        }

        if (value.startsWith("id:")) {
            return By.id(value.substring(3));
        } else if (value.startsWith("name:")) {
            return By.name(value.substring(5));
        } else if (value.startsWith("css:")) {
            return By.cssSelector(value.substring(4));
        } else if (value.startsWith("xpath:")) {
            return By.xpath(value.substring(6));
        } else {
            // default: treat as xpath
            return By.xpath(value);
        }
    }
}