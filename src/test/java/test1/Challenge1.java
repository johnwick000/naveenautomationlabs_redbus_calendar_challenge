package test1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Challenge1 {

	static WebDriver driver;
	static WebDriverWait wait;

	public static void main(String[] args) {
		ChromeOptions option = new ChromeOptions();
		option.addArguments("--disable-notifications");
		driver = new ChromeDriver(option);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		System.setProperty("webdriver.chrome.driver",
				"src/test/resources/chromedriver.exe");
		// WebDriver driver = new ChromeDriver();
		driver.get("https://www.redbus.in");
		driver.manage().window().maximize();

		List<String> weekendDates=getWeekEndDates("Jan 2025");
		System.out.println("Weekend Dates are : " + weekendDates);
		driver.close();

	}


	static List<String> getWeekEndDates(String givenMonthYear) {

		WebElement calendarImage = driver.findElement(By.cssSelector("i[class$='datev2']"));
		calendarImage.click();
		WebElement monthAndYearElement = driver
				.findElement(By.cssSelector("div[class^='DayNavigator'] div:nth-child(2)"));

		if (!monthAndYearElement.getText().contains(givenMonthYear)) {
			WebElement nextArrow = driver.findElement(By.cssSelector("#Layer_1"));
			nextArrow.click();
		}

		while (!monthAndYearElement.getText().contains(givenMonthYear)) {
			
			WebElement nextArrow = driver.findElement(By.cssSelector("div[class*='CalendarHeader'] div:nth-child(3)"));
			wait.until(ExpectedConditions.elementToBeClickable(nextArrow));
			
			Actions a = new Actions(driver);
			a.moveToElement(nextArrow).click().perform();

			monthAndYearElement = driver.findElement(By.cssSelector("div[class^='DayNavigator'] div:nth-child(2)"));
		}
		List<WebElement> holidayCount = driver.findElements(By.cssSelector("div[class='holiday_count']"));
		String holidayCountString = "No Holidays in this Month";
		if (!holidayCount.isEmpty()) {
			holidayCountString = holidayCount.get(0).getText();
		}

		List<WebElement> weekends = driver.findElements(By.cssSelector(
				"div[class^='DayTiles_']  span:not([class$='hVMWpe'], [class$='dkWAbH'], [class$='gigHYE'])"));

		List<String> weekendDates = new ArrayList<>();

		for (WebElement weekend : weekends) {
			weekendDates.add(weekend.getText());
		}

		System.out.println("Given month and year is " + givenMonthYear);
		
		System.out.println("No. of holidays (excluding weekends) : " + holidayCountString);

		return weekendDates;
	}

}
