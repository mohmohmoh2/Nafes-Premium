package Utilities;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static java.lang.System.getProperty;

public class Utility {
    private static final String SCREENSHOTS_PATH = "test-outputs/Screenshots/";
    private static final String userHome = getProperty("user.dir");
    private static final int waitTime = 20; // seconds

    /**
     * Waits for an element to be clickable and then clicks it.
     */
    public static void clickingOnElement(WebDriver driver, By locator) {
        try {
            waitForElementToBeClickable(driver, locator);
            driver.findElement(locator).click();
        } catch (Exception e) {
            LogsUtils.error("Failed to click element: " + e.getMessage());
        }
    }

    /**
     * Clicks on an element using JavaScript.
     */
    public static void clickingOnElementJS(WebDriver driver, By locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", findWebElement(driver, locator));
    }

    /**
     * Clicks on a specific element from a list.
     */
    public static void clickingOnElementFromList(WebDriver driver, By locator, int index) {
        waitForElementToBeClickable(driver, locator);
        scrollingToElementFromList(driver, locator, index);
        driver.findElements(locator).get(index).click();
    }

    /**
     * Clears text from an input field.
     */
    public static void clearData(WebDriver driver, By locator) {
        waitForVisibility(driver, locator);
        driver.findElement(locator).clear();
    }

    /**
     * Enters text into a field.
     */
    public static void enterData(WebDriver driver, By locator, String data) {
        waitForVisibility(driver, locator);
        clearData(driver, locator);
        driver.findElement(locator).sendKeys(data);
    }

    /**
     * Retrieves the visible text from a WebElement.
     */
    public static String getText(WebDriver driver, By locator) {
        waitForVisibility(driver, locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Retrieves the visible text from a list of WebElements.
     */
    public static String getTextFromList(WebDriver driver, By locator, int index) {
        waitForVisibility(driver, locator);
        return driver.findElements(locator).get(index).getText();
    }

    public static WebDriverWait generalWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(waitTime));
    }

    /**
     * Scrolls to an element using Actions.
     */
    public static void scrolling(WebDriver driver, By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(findWebElement(driver, locator)).perform();
    }

    /**
     * Scrolls to an element using JavaScript.
     */
    public static void scrollingJS(WebDriver driver, By locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", findWebElement(driver, locator));
    }

    /**
     * Scrolls to a specific element in a list.
     */
    public static void scrollingToElementFromList(WebDriver driver, By locator, int index) {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElements(locator).get(index)).perform();
    }

    /**
     * Finds and returns a WebElement.
     */
    public static WebElement findWebElement(WebDriver driver, By locator) {
        return driver.findElement(locator);
    }

    /**
     * Selects an option from a dropdown using visible text.
     */
    public static void selectingFromDropDown(WebDriver driver, By locator, String option) {
        new Select(findWebElement(driver, locator)).selectByVisibleText(option);
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ssa").format(new Date());
    }

    /**
     * Takes a screenshot and attaches it to the Allure report.
     */
    public static void takeScreenShot(WebDriver driver, String screenshotName) {
        try {
            File screenshotSrc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(SCREENSHOTS_PATH + screenshotName + "-" + getTimeStamp() + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotFile);
            Allure.addAttachment(screenshotName, Files.newInputStream(Path.of(screenshotFile.getPath())));
        } catch (Exception e) {
            LogsUtils.error("Screenshot error: " + e.getMessage());
        }
    }

    /**
     * Uploads a file using the sendKeys method.
     */
    public static void uploadFile(WebDriver driver, By locator, String filePath) {
        try {
            String absolutePath = userHome + File.separator + filePath;
            File file = new File(absolutePath);
            if (!file.exists()) throw new FileNotFoundException("File not found: " + absolutePath);

            findWebElement(driver, locator).sendKeys(absolutePath);
            LogsUtils.info("File uploaded successfully: " + absolutePath);
        } catch (Exception e) {
            LogsUtils.error("File upload error: " + e.getMessage());
        }
    }

    /**
     * Uploads a file from an indexed list of file input elements.
     */
    public static void uploadFileFromList(WebDriver driver, By locator, String filePath, int index) throws InterruptedException {
        String absolutePath = userHome + File.separator + filePath;
        scrollingToElementFromList(driver, locator, index);
        driver.findElements(locator).get(index).sendKeys(absolutePath);
        Thread.sleep(1000);
    }

    /**
     * Uploads a file using Robot class.
     */
    public static void uploadFileRobot(WebDriver driver, By locator, String filePath) {
        try {
            String absolutePath = userHome + File.separator + filePath;
            File file = new File(absolutePath);
            if (!file.exists()) throw new FileNotFoundException("File not found: " + absolutePath);

            clickingOnElement(driver, locator);
            Thread.sleep(1000);

            StringSelection stringSelection = new StringSelection(absolutePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            LogsUtils.info("File uploaded successfully using Robot: " + absolutePath);
        } catch (Exception e) {
            LogsUtils.error("File upload error: " + e.getMessage());
        }
    }

    /**
     * Generates a random number between 1 and upperBound (inclusive).
     */
    public static int generateRandomNumber(int upperBound) {
        return new Random().nextInt(upperBound) + 1;
    }

    /**
     * Generates a set of unique random numbers.
     */
    public static Set<Integer> generateUniqueNumber(int numberOfProductsNeeded, int totalNumberOfProducts) {
        Set<Integer> generatedNumbers = new HashSet<>();
        while (generatedNumbers.size() < numberOfProductsNeeded) {
            generatedNumbers.add(generateRandomNumber(totalNumberOfProducts));
        }
        return generatedNumbers;
    }

    /**
     * Verifies if the current URL matches the expected URL.
     */
    public static boolean VerifyURL(WebDriver driver, String expectedURL) {
        try {
            generalWait(driver).until(ExpectedConditions.urlToBe(expectedURL));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Set<Cookie> getAllCookies(WebDriver driver) {
        return driver.manage().getCookies();
    }

    public static void restoreSession(WebDriver driver, Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            driver.manage().addCookie(cookie);
        }
    }

    public static File getLatestFile(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) return null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];
    }

    public static void refreshPage(WebDriver driver) {
        driver.navigate().refresh();
        LogsUtils.info("Page refreshed");
    }

    /**
     * Wait until an element is visible.
     */
    public static void waitForVisibility(WebDriver driver, By locator) {
        generalWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is clickable.
     */
    public static void waitForElementToBeClickable(WebDriver driver, By locator) {
        generalWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until an element disappears from the DOM.
     */
    public static void waitForInvisibility(WebDriver driver, By locator) {
        generalWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Checks if an element is displayed on the page.
     */
    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if the text is present in the elements.
     */
    public static boolean isTextPresentInElements(WebDriver driver, By locator, String text) throws InterruptedException {
        Thread.sleep(2000);
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (element.getText().contains(text)) {
                return true;
            }
        }
        return false;
    }

}