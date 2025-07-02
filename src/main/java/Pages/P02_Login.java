package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utilities.Utility.clickingOnElement;
import static Utilities.Utility.enterData;


public class P02_Login {
    // TODO: Define the locators
    private final By usernameInput = By.xpath("//input[@name='email']");
    private final By passwordInput = By.xpath("//input[@name='password']");
    private final By loginButton = By.xpath("//button[@type='submit']");

    private final WebDriver driver;

    // TODO: Create a constructor
    public P02_Login(WebDriver driver) {
        this.driver = driver;
    }

    // TODO: Create a method to send the username
    public P02_Login enterUsername(String username) {
        enterData(driver, usernameInput, username);
        return this;
    }

    // TODO: Create a method to send the password
    public P02_Login enterPassword(String password) {
        enterData(driver, passwordInput, password);
        return this;
    }

    // TODO: Create a method to click on the login button
    public void clickLogin() {
        clickingOnElement(driver, loginButton);

    }

    // TODO: Create a method to assert the login
    public boolean assertLoginTc(String expectedUrl) {
        return driver.getCurrentUrl().equals(expectedUrl);
    }
}
