package Pages;

import Utilities.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class P01_Home {

    private final WebDriver driver;

    // Locators
    private final By loginPageButton = By.cssSelector("div.actions > a.btn.login");

    // TODO: Create a constructor
    public P01_Home(WebDriver driver) {
        this.driver = driver;
    }

    // TODO: Go to the login page
    public void goToLoginPage() {
        Utility.clickingOnElement(driver, loginPageButton);
    }
}
