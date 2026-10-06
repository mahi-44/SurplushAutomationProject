package BaseClass;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import com.surplush.objectrepositoryutility.HomePage;


import surplushutility.Databaseutility;
import surplushutility.Fileutility;
import surplushutility.Javautility;
import surplushutility.propertyutility;
import surplushutility.webdriverutility;

public class Baseclass {

	public WebDriver driver;
	public Properties prop;
	public static WebDriver sdriver; // For listeners

	public webdriverutility wUtil = new webdriverutility();
	public Databaseutility dUtil = new Databaseutility();
	public Fileutility eUtil = new Fileutility();
	public propertyutility pu = new propertyutility();
	public Javautility jUtil = new Javautility();

	public WebDriver getDriver() {
		return driver;
	}
	// other setup code...

	// ========= 1. @BeforeSuite - DB Connection =========
	@BeforeSuite
	public void beforeSuite() {
		System.out.println("==BeforeSuite== DB Connection Started");
		// DataBaseUtility.connect();
	}

	// ========= 2. @BeforeTest - Parallel Execution =========
	@BeforeTest
	public void beforeTest() {
		System.out.println("==BeforeTest== Parallel Execution Config");
	}

	// ========= 3. @BeforeClass - Read Properties =========
	@BeforeClass
	public void beforeClass() throws Exception {
		System.out.println("==BeforeClass== Reading Properties");
		prop = new Properties();
		FileInputStream fis = new FileInputStream("./src/test/resources/CommonProperties.properties");
		prop.load(fis);
		String url = prop.getProperty("url");
		System.out.println("URL from properties: " + url);
	}

	@BeforeMethod
	public void StartingTest() throws Exception {
		System.out.println("==BeforeMethod== Starting test");

		// 1. CHROME OPTIONS TO BLOCK SAVE PASSWORD POPUP
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications", "--start-maximized", "--disable-popup-blocking");
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });

		// --- THIS FIXES YOUR SCREENSHOT ISSUE ---
		java.util.Map<String, Object> prefs = new java.util.HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		prefs.put("profile.password_manager_leak_detection", false);
		options.setExperimentalOption("prefs", prefs);

		driver = new ChromeDriver(options);
		sdriver = driver;
		driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
		driver.get("https://www.surplush.co.uk/");

		HomePage home = new HomePage(driver);
		home.clickProfile();
		home.login("mallisettymahesh10@gmail.com", "Mahesh44@Mahi");

		// 2. EXTRA SAFETY - Press ESC if popup still comes
		try {
			Thread.sleep(2000);
			new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
			System.out.println("Pressed ESC to close Save password popup");
		} catch (Exception e) {
		}

		System.out.println("Login Done, Popup Blocked - Ready to add product");
	}

	// ========= 5. @AfterMethod - Logout / Close =========
	@AfterMethod
	public void afterMethod() {
		System.out.println("==AfterMethod== Closing Browser");
		// driver.quit();
	}

	// ========= 6. @AfterClass - Close Properties =========
	@AfterClass
	public void afterClass() {
		System.out.println("==AfterClass== Closing Properties");
	}

	// ========= 7. @AfterTest - Close Parallel =========
	@AfterTest
	public void afterTest() {
		System.out.println("==AfterTest== Parallel Execution Closed");
	}

	// ========= 8. @AfterSuite - DB Close =========
	@AfterSuite
	public void afterSuite() {
		System.out.println("==AfterSuite== DB Connection Closed");
	}

}
