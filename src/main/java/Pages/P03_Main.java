package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utilities.Utility.clickingOnElement;

public class P03_Main {
    // Locators
    private final By loginPageButton = By.xpath("//*[@id=\"__next\"]/div/div[1]/div[1]/div[2]/ul/div[3]/li[2]/a");
    private final By myBidsButton = By.xpath("//a[@href='/account/bids']");
    private final By myRFPsButton = By.xpath("//a[@href=\"/account/my-rfp\"]");

    private final WebDriver driver;
    public Logger log = LogManager.getLogger();

    // TODO: Create a constructor
    public P03_Main(WebDriver driver) {
        this.driver = driver;
    }


    // TODO: Click Logout
    public void clickLogout() {
        log.info("Clicking on Logout button");
        clickingOnElement(driver, loginPageButton);
    }

    // TODO: Click My RFPs
    public void clickMyRFPs() {
        log.info("Clicking on My RFPs button");
        clickingOnElement(driver, myRFPsButton);
    }

    // TODO: Click My Bids
    public void clickMyBids() {
        log.info("Clicking on My Bids button");
        clickingOnElement(driver, myBidsButton);
    }


}
