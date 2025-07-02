package base;

import DriverSettings.DriverManager;
import Pages.P01_Home;
import Pages.P02_Login;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Duration;

import static DriverSettings.DriverManager.*;
import static Utilities.DataUtils.getJsonData;
import static Utilities.DataUtils.getPropertyValue;


public class BaseTest {

    public Logger log = LogManager.getLogger();

    @BeforeMethod
    public void login() throws IOException {
        String driverType = getPropertyValue("config", "driverType");

        // Create a new WebDriver instance based on the driver type specified in the config
        WebDriver driver = DriverManager.createDriver(driverType);

        // For headless mode, use the createDriverHeadless method
        //WebDriver driver = DriverManager.createDriverHeadless(driverType);

        setDriver(driver);
        getDriver().get(getPropertyValue("config", "BASE_URL"));

        // Set the window size to 1920x1080
        //getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));

        // Maximize the window
        getDriver().manage().window().maximize();

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        log.info("Browser opened");


        // Navigate to the login page
        P01_Home homePage = new P01_Home(getDriver());
        homePage.goToLoginPage();


        // Perform login
        P02_Login loginPage = new P02_Login(getDriver());
        loginPage.enterUsername(getJsonData("login", "email"))
                .enterPassword(getJsonData("login", "password"))
                .clickLogin();

        // Wait for the URL to change to the expected one
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        String expectedUrl = getPropertyValue("config", "BASE_URL") + getPropertyValue("config", "ACCOUNT_URL");

        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        log.info("Logged in successfully \n ");

        // Close the chat widget
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hubspot-conversations-iframe")));
//        ((JavascriptExecutor) driver).executeScript("""
//        const el = document.querySelector('#hubspot-messages-iframe-container');
//        if (el) {
//            el.setAttribute('style', 'display: none !important; visibility: hidden !important;');
//        }
//        """);
//        log.info("Chat widget is disabled");

    }


    @AfterMethod
    public void tearDown() {
        // TODO: Close the browser
        log.info("Closing the browser \n ");
        quitDriver();
    }


}
