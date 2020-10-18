package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTests {
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

	// testNG
	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void PositiveLoginTest() {
		// create driver

		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		// maximize browser window
//		driver.manage().window().maximize();
		// enter username and password
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");
		// click login button

		WebElement loginBtn = driver.findElement(By.tagName("button"));
		loginBtn.click();

		// verification if it worked (new url/logout visible/ms
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not as expected");

		WebElement logoutBtn = driver.findElement(By.xpath("//a[@href='/logout']"));
		Assert.assertTrue(logoutBtn.isDisplayed(), "button is not visible");

		WebElement msg = driver.findElement(By.cssSelector("div#flash"));
		String expectedMsg = "You logged into a secure area!\n" + "×";
		String actualMsg = msg.getText();

		Assert.assertEquals(actualMsg, expectedMsg, "Actual message is not same as expected");

	}

	@Parameters({ "username", "password", "msg" })

	@Test(priority = 1, groups = { "negativeTests", "smokeTest" })
	public void invalidLoginTest(String username, String password, String msg) {

		driver.get("http://the-internet.herokuapp.com/login");

		WebElement usernameEl = driver.findElement(By.id("username"));
		usernameEl.sendKeys(username);
		WebElement passwordEl = driver.findElement(By.id("password"));
		passwordEl.sendKeys(password);
		WebElement btn = driver.findElement(By.xpath("//form[@id='login']//i[@class='fa fa-2x fa-sign-in']"));
		btn.click();

		WebElement msgEl = driver.findElement(By.cssSelector("div#flash"));
		String actualMsg = msgEl.getText();
		Assert.assertTrue(actualMsg.contains(msg), "The message is wrong");
	}

	@AfterMethod
	private void endUp() {
		driver.quit();
	}
}
