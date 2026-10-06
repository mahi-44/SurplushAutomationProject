package surplushutility;

import java.io.FileInputStream;
import java.util.Properties;

public class Fileutility {
    
    String propertyFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Commonproperties.properties";
    
    public String readDataFromPropertyFile(String key) throws Exception {
        FileInputStream fis = new FileInputStream(propertyFilePath);
        Properties p = new Properties();
        p.load(fis);
        String value = p.getProperty(key);
        System.out.println("Reading Key: " + key + " => Value: " + value); // Debug print
        return value;
    }
}