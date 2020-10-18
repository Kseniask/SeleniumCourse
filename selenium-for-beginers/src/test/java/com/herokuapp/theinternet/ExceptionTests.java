package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionTests {
	private WebDriver driver;

	// crossbrowser testing
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional String browser) { //to make param optional OR @Optional("chrome")
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
			driver = new FirefoxDriver();
			break;
		default:
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			driver = new ChromeDriver();
			break;
		}
	}
	
	@Test
	public void notVisibleTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");
		WebElement startBtn = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startBtn.click();
		
		WebElement finishEl = driver.findElement(By.id("finish"));
		
		//Explicit WAIT
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.visibilityOf(finishEl));
		String finishText = finishEl.getText();
		
		Assert.assertTrue(finishText.contains("Hello World"));

	}

	@Test
	public void timeoutTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");
		WebElement startBtn = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startBtn.click();
		
		WebElement finishEl = driver.findElement(By.id("finish"));
		
		//Explicit WAIT
		WebDriverWait wait = new WebDriverWait(driver,2);
		wait.until(ExpectedConditions.visibilityOf(finishEl));
		String finishText = finishEl.getText();
		
		Assert.assertTrue(finishText.contains("Hello World"));

	}
	
	@Test
	public void StaleElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		WebElement removeBtn = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));
		removeBtn.click();
		WebElement checkbox = driver.findElement(By.id("checkbox"));
		
		WebDriverWait wait = new WebDriverWait(driver,10);
//		wait.until(ExpectedConditions.invisibilityOf(checkbox));
//		Assert.assertFalse(checkbox.isDisplayed());
		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)),"checkbox is still visible, but should not be");
		WebElement addBtn = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addBtn.click();
		//when add and then remover, rewrite checkbox since program doesn't treat it same
		checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(checkbox.isDisplayed());
		
	}
	
	@Test
	public void disabledElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		WebElement enableBtn = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
		enableBtn.click();
		WebElement textField = driver.findElement(By.xpath("(//input)[2]"));
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.elementToBeClickable(textField));
		textField.sendKeys("typed");
		Assert.assertEquals(textField.getAttribute("value"), "typed");
		
	}
	
	@AfterMethod
	private void endUp() {
		driver.quit();
	}
}
