package login;

import DriverSettings.DriverManager;
import Pages.P01_Home;
import Pages.P02_Login;
import Utilities.RetryAnalyzer;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static DriverSettings.DriverManager.*;
import static Utilities.DataUtils.getJsonData;
import static Utilities.DataUtils.getPropertyValue;

public class TC01_Login {


    @BeforeMethod
    public void setUp() throws IOException {
        String driverType = getPropertyValue("config", "driverType");
        WebDriver driver = DriverManager.createDriverHeadless(driverType);
        setDriver(driver);
        getDriver().get(getPropertyValue("config", "BASE_URL"));
        getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Step("Login Test Case")
    public void testLogin() throws IOException {
        // Navigate to the login page
        P01_Home homePage = new P01_Home(getDriver());
        homePage.goToLoginPage();
        // Perform login
        P02_Login loginPage = new P02_Login(getDriver());
        loginPage.enterUsername(getJsonData("login", "email"))
                .enterPassword(getJsonData("login", "password"))
                .clickLogin();


        String expectedUrl = getPropertyValue("config", "BASE_URL") + getPropertyValue("config", "ACCOUNT_URL");

        // Wait for the URL to change to the expected one
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));


        // Perform the assertion
        Assert.assertEquals(getDriver().getCurrentUrl(), expectedUrl);


    }

    @AfterMethod
    public void tearDown() {
        // TODO: Close the browser
        quitDriver();
    }
}
