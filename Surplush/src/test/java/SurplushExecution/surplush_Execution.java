package SurplushExecution;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class surplush_Execution {
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get("https://surplush.co.uk/");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[@class='w-8 h-8 md:w-10 md:h-10 flex items-center justify-center hover:scale-110 transition-all duration-300 rounded-full flex-shrink-0 text-green']")).click();
		driver.findElement(By.xpath("//input[@id='email']")).sendKeys("mallisettymahesh10@gmail.com");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Mahesh44@Mahi");
		driver.findElement(By.xpath("//button[text()='Sign In']")).click();
		driver.findElement(By.xpath("//div[@class='w-8 h-8 md:w-10 md:h-10 flex items-center justify-center cursor-pointer relative text-green']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='text-xl md:text-2xl lg:text-2xl xl:text-3xl font-bold text-gray-900 hover:text-green transition-colors duration-300 tracking-tight']")).click();
		
		driver.findElement(By.xpath("//button[text()='Accept All']")).click();
		driver.findElement(By.xpath("//img[@src='/_next/image?url=%2F1.jpg&w=96&q=75']")).click();
		driver.findElement(By.xpath("//span[@class='text-primary font-medium']")).click();
		driver.findElement(By.xpath("(//button[contains(., 'Add to Basket')])[5]")).click();
	}
	
}
