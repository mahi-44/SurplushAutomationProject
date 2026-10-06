package surplushutility;

import java.io.FileInputStream;
import java.util.Properties;

public class propertyutility {

	public static String getDataFromPropertyFile(String key) throws Exception {

		FileInputStream fis = new FileInputStream("./src/test/resources/CommonProperties.properties");

		Properties prop = new Properties();

		prop.load(fis);

		return prop.getProperty(key);
	

	}

	
}