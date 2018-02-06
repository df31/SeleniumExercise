package AmazonSeleniumTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.util.List;

public class NikonTests {
    private WebDriver driver;

    @Parameters("targetBrowser")
    @BeforeClass(alwaysRun = true)
    private void setUp(String browserType)
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Projects\\Webdrivers\\chromedriver.exe");
        System.setProperty("webdriver.firefox.marionette","C:\\Projects\\Webdrivers\\geckodriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-infobars");

        if (browserType.equalsIgnoreCase("firefox"))
        {
            driver = new FirefoxDriver();
        }
        else if (browserType.equalsIgnoreCase("chrome"))
        {
            driver = new ChromeDriver(chromeOptions);
        }

        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    private void tearDown()
    {
        driver.quit();
    }

    @Parameters("baseUrl")
    @BeforeMethod(alwaysRun = true)
    public void openWebPage(String webUrl)
    {
        driver.get(webUrl);
    }

    @Parameters("expectedText")
    @Test(priority = 0)
    public void nikonTest1(String expectedText)
    {
        WebDriverWait wait = new WebDriverWait(driver,3);

        //Enter search key: 'Nikon'
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(" Nikon");

        //Click 'Go' button
        driver.findElement(By.xpath("//*[@value='Go']")).click();

        //Sort by "Price: High to Low "
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sort")));
        new Select(driver.findElement(By.id("sort"))).selectByValue("price-desc-rank");

        //Wait page refresh
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("top")));

        //Get results
        List<WebElement> results = driver.findElements(By.xpath("//h2[@class='a-size-medium s-inline  s-access-title  a-text-normal']"));
        String secondProductTitleText = results.get(1).getText();

        //Click second product
        results.get(1).click();

        //Wait page open
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));

        String productTitleText = driver.findElement(By.id("productTitle")).getText();
        assert (productTitleText.equals(secondProductTitleText));

        //Verify product title text contains "Nikon D3X"
        assert(productTitleText.contains(expectedText));
    }
}
