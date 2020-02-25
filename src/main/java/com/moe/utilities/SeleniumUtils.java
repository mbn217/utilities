package com.moe.utilities;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

public class SeleniumUtils {

	public static WebDriver driver;
	public static JavascriptExecutor JSdriver;
	private static Logger log = Logger.getLogger(SeleniumUtils.class);
	public static String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());


	public static void highlightElement(WebElement element) {
		log.info("Highlight element");
		for (int i = 0; i < 5; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: solid red; border: 5px solid blue;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}

	}


	public static void highlightListOfElement(WebDriver driver, List<WebElement> element) {
		log.info("Highlighting list of element");
		for (int i = 0; i < 5; i++) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: solid red; border: 5px solid blue;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}

	}


	public static void acceptAlertIfAvailable(long timeout) {
		log.info("Checking if there is a popup alert to accept it");
		long waitForAlert = System.currentTimeMillis() + timeout;
		boolean boolFound = false;
		do {
			try {
				Alert alert = driver.switchTo().alert();
				if (alert != null) {
					String alertMsg = alert.getText();
					alert.accept();
					log.info("Pop-up alert [" + alertMsg + "] was found and accepted");
					boolFound = true;
				}
			} catch (NoAlertPresentException ex) {
				log.error("Pop-up alert Not handled and Exception occured --> " + ex.getMessage());
			}
		} while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
	}

	public static void dismissAlertIfAvailable(long timeout) {
		log.info("Checking if there is a popup alert to dismiss it");
		long waitForAlert = System.currentTimeMillis() + timeout;
		boolean boolFound = false;
		do {
			try {
				Alert alert = driver.switchTo().alert();
				if (alert != null) {
					String alertMsg = alert.getText();
					alert.dismiss();
					log.info("Pop-up alert [" + alertMsg + "] was found and dismissed");
					boolFound = true;
				}
			} catch (NoAlertPresentException ex) {
				log.error("Pop-up alert Not handled and Exception occured --> " + ex.getMessage());
			}
		} while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
	}


	public static void takeScreenshot(String screenshotName) {
		log.info("Taking a screen shot");
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File("./src/test/resources/ScreenShots/" + screenshotName + timeStamp + ".png"));
			log.info("Screen Shot was taken");
		} catch (IOException e) {
			log.error("Something went Wrong --> " + e.getMessage());
		}
	}

	public static void takeScreenShot_Ashot(String outputFilePath) throws IOException {
		log.info("Taking a screen shot");
		Screenshot screenshot = new AShot().takeScreenshot(driver);
		ImageIO.write(screenshot.getImage(), "PNG", new File(outputFilePath + timeStamp + ".png"));
	}

	public static boolean waitForElement(WebElement element) throws IOException {
		log.info("Waiting for an element in the page...");
		boolean isElementPresent = true;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOf(element));
			log.info("Element is visible");
			return isElementPresent;
		} catch (Exception e) {
			log.info("waitForElement method failed! " + e.getMessage());
			return !isElementPresent;
		}
	}


	public static int[] getX_Y_cordinates(WebElement element) {
		log.info("Get X and Y Coordinates");
		int[] xy = null;
		Point p = element.getLocation();
		System.out.println("X Position: " + p.getX());
		System.out.println("Y Position: " + p.getY());
		int x = p.getX();
		int y = p.getY();
		xy = new int[x * y];
		return xy;

	}


	public static int[] getWidth_HeightOfElement(WebElement element) {
		log.info("Get with and Height");
		int[] xy = null;
		Dimension dimensions = element.getSize();
		System.out.println("Width : " + dimensions.width);
		System.out.println("Height : " + dimensions.height);
		int x = dimensions.getWidth();
		int y = dimensions.getHeight();
		xy = new int[x * y];
		return xy;

	}


	public static long getPageLoadTime() {
		log.info("Checking load time of the page");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");
		long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart;");
		long loadtime = (loadEventEnd - navigationStart) / 1000;
		System.out.println("Page Load Time is " + loadtime + " seconds.");
		return loadtime;
	}

	public static void zoomOut(int toExtent) {
		log.info("Performing a zoom out");
		for (int i = 0; i < toExtent; i++) {
			driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
		}
	}

	public static void zoomIn(int toExtent) {
		log.info("Performing a zoom in");
		for (int i = 0; i < toExtent; i++) {
			driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
		}
	}

	public static void zoomToDefault() {
		log.info("Performing a zoom to default");
		driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));

	}

	public static void addCookie(String cookieName, String cookieValue, String Domain, String path) throws Exception {
		log.info("Adding cookies to the browser");
		try {
			Cookie name = new Cookie(cookieName, cookieValue, Domain, path, new Date());
			driver.manage().addCookie(name);
			// refresh();
			log.info("Added Cookie " + cookieName);

		} catch (Exception e) {
			log.error("Something went Wrong --> " + e.getMessage());
		}

	}

	public static void deleteAllCookies() {
		log.info("Delete all cookies");
		driver.manage().deleteAllCookies();
	}


	public static boolean isElementPresent(WebElement element) {
		log.info("Checking if element is present");
		try {
			element.isDisplayed();
			return true;
		} catch (NoSuchElementException e) {
			log.error("The Element is not FOUND --> " + e.getMessage());
			return false;
		} catch (Exception e) {
			log.error("Something went Wrong --> " + e.getMessage());
			return false;
		}
	}

	public static void scrolltoElement(WebElement ScrolltoThisElement) {
		log.info("Scrolling to view with JS");
		String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

				((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, ScrolltoThisElement);
	}

	public static void checkboxChecking(WebElement checkbox) {
		log.info("Checking a checkbox");
		boolean checkstatus;
		checkstatus = checkbox.isSelected();
		if (checkstatus == true) {
			log.info("Checkbox is already checked");
		} else {
			checkbox.click();
			log.info("Checked the checkbox");
		}
	}

	public static void radiobuttonSelect(WebElement Radio) {
		log.info("Selecting a Radio button");
		boolean checkstatus;
		checkstatus = Radio.isSelected();
		if (checkstatus == true) {
			log.info("Radio Button is already selected");
		} else {
			Radio.click();
			log.info("Selected the Radio button");
		}
	}

	public static void checkboxUnchecking(WebElement checkbox) {
		log.info("UnChecking a checkbox");
		boolean checkstatus;
		checkstatus = checkbox.isSelected();
		if (checkstatus == true) {
			checkbox.click();
			log.info("Checkbox is unchecked");
		} else {
			log.info("Checkbox is already unchecked");
		}
	}

	public static void radioButtonDeselect(WebElement Radio) {
		log.info("Deselecting a Radio button");
		boolean checkstatus;
		checkstatus = Radio.isSelected();
		if (checkstatus == true) {
			Radio.click();
			log.info("Radio Button is deselected");
		} else {
			log.info("Radio Button is already Deselected");
		}
	}

	public static void downloadFile(String href, String fileName) throws Exception {
		log.info("Downloading a file");
		URL url = null;
		URLConnection con = null;
		int i;
		url = new URL(href);
		con = url.openConnection();
		File file = new File(".//OutputData//" + fileName);
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
			bos.flush();
			bis.close();
	}

	public static void navigate_back() {
		log.info("navigating to the previous page");
		driver.navigate().back();
	}

	public static void refresh() {
		log.info("Refreshing the page");
		driver.navigate().refresh();
	}

	public static void navigate_forward() {
		log.info("Navigating to the next page");
		driver.navigate().forward();
	}

	public static void clearTextField(WebElement element) {
		log.info("Clearing the text from textfield");
		element.clear();

	}

	public static void selectElementByValue(WebElement element, String value) {
		log.info("Selecting a value");
		Select selectitem = new Select(element);
		selectitem.selectByValue(value);
	}

	public static void deselectElementByValue(WebElement element, String value) {
		log.info("Deselecting a value");
		Select selectitem = new Select(element);
		selectitem.deselectByValue(value);
	}

	public static void selectElementByIndex(WebElement element, int index) {
		log.info("Selecting a value");
		Select selectitem = new Select(element);
		selectitem.selectByIndex(index);
	}

	public static void deselectElementByIndex(WebElement element, int index) {
		log.info("Deselecting a value");
		Select selectitem = new Select(element);
		selectitem.deselectByIndex(index);
	}

	public static void clickCheckboxFromList(String xpathOfElement, String valueToSelect) {
		log.info("Clicking on checkbox from a list of checkboxes");
		List<WebElement> lst = driver.findElements(By.xpath(xpathOfElement));
		for (int i = 0; i < lst.size(); i++) {
			List<WebElement> dr = lst.get(i).findElements(By.tagName("label"));
			for (WebElement f : dr) {
				System.out.println("value in the list : " + f.getText());
				if (valueToSelect.equals(f.getText())) {
					f.click();
					break;
				}
			}
		}
	}

	public static void verifyExpectedAndActualOptionsInDropdown(WebElement element,
			List<String> expectedListOfOptions) {
		log.info("Verifying two list of select options if they match");
		Select ele = new Select(element);
		List<String> expectedOptions = expectedListOfOptions;
		List<String> actualOptions = new ArrayList<String>();
			for (WebElement option : ele.getOptions()) {
				log.info("Dropdown options are: " + option.getText());
				actualOptions.add(option.getText());
			}
			log.info("Numbers of options present in the dropdown: " + actualOptions.size());

	}

	public static void deselectElementByName(WebElement element, String Name) {
		log.info("Deselect a value");
		Select selectitem = new Select(element);
		selectitem.deselectByVisibleText(Name);
	}

	public static void verifyDropdownHaveNoMultipleSelection(WebElement element, String Name) {
		log.info("Verify dropdown list has no multiple selection");
		Select ss = new Select(element);
	}

	public static void verifyDropdownHaveMultipleSelection(WebElement element, String Name) {
		log.info("Verify dropdown list has multiple selection");
		Select ss = new Select(element);
	}

	public static void verifyOptionsInDropdownInAphabeticalOrder(WebElement element) {
		log.info("Verify dropdown list is in Alphabetic order");
		Select ele = new Select(element);
		List<String> expectedOptions = new ArrayList<String>();
		List<String> actualOptions = new ArrayList<String>();
		for (WebElement option : ele.getOptions()) {
			log.info("Dropdown options are: " + option.getText());
			actualOptions.add(option.getText());
			expectedOptions.add(option.getText());
		}
		Collections.sort(actualOptions);
		log.info("Numbers of options present in the dropdown: " + actualOptions.size());
	}

	public static void verifyOptionsSizeOfDropdown(WebElement element, int numberOfOptions) {
		log.info("Verify the size of the dropdown list");
		Select ssd = new Select(element);
	}

	public static void dragAndDrop(WebElement fromWebElement, WebElement toWebElement) {
		log.info("Dragging and dropping an element");
		Actions builder = new Actions(driver);
		builder.dragAndDrop(fromWebElement, toWebElement);
	}

	public static void dragAndDrop_Method2(WebElement fromWebElement, WebElement toWebElement) {
		log.info("Dragging and dropping an element");
		Actions builder = new Actions(driver);
		Action dragAndDrop = builder.clickAndHold(fromWebElement).moveToElement(toWebElement).release(toWebElement)
				.build();
		dragAndDrop.perform();
	}

	public static void dragAndDrop_Method3(WebElement fromWebElement, WebElement toWebElement)
			throws InterruptedException {
		log.info("Dragging and dropping an element");
		Actions builder = new Actions(driver);
		builder.clickAndHold(fromWebElement).moveToElement(toWebElement).perform();
		Thread.sleep(2000);
		builder.release(toWebElement).build().perform();
	}

	public static void hoverToWebelement(WebElement HovertoWebElement) throws InterruptedException {
		log.info("Hovering over an element");
		Actions builder = new Actions(driver);
		builder.moveToElement(HovertoWebElement).perform();
		Thread.sleep(2000);
	}

	public static void doubleClickWebelement(WebElement doubleclickonWebElement) throws InterruptedException {
		log.info("Double clicking an element");
		Actions builder = new Actions(driver);
		builder.doubleClick(doubleclickonWebElement).perform();
		Thread.sleep(2000);
	}

	public static void selectElementByName(WebElement element, String Name) {
		log.info("Selecting an Element");
		Select selectitem = new Select(element);
		selectitem.selectByVisibleText(Name);
	}

	public static void switchToFrameByIndex(int frameValue) {
		log.info("Switching  to a frame");
		driver.switchTo().frame(frameValue);
	}

	public static void switchToFrameByWebElement(String frameName) throws Exception {
		log.info("Switching  to a frame");
		WebElement webelement = driver.findElement(By.tagName(frameName));
		try {
			driver.switchTo().frame(webelement);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void takeScreenshotOfWebelement(WebElement element, String screenshotName) throws Exception {
		log.info("Taking a screen shot of an element");
		File v = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage bi = ImageIO.read(v);
		org.openqa.selenium.Point p = element.getLocation();
		int n = element.getSize().getWidth();
		int m = element.getSize().getHeight();
		BufferedImage d = bi.getSubimage(p.getX(), p.getY(), n, m);
		ImageIO.write(d, "png", v);

		FileUtils.copyFile(v, new File("./src/test/resources/ScreenShots" + screenshotName + timeStamp + ".png"));
	}

	public static void switchToNewWindow() {
		log.info("Switching to new window");
		Set s = driver.getWindowHandles();
		Iterator itr = s.iterator();
		String w1 = (String) itr.next();
		String w2 = (String) itr.next();
		driver.switchTo().window(w2);
	}

	public static void turnOffImplicitWaits() {
		log.info("Turning off implicit wait");
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public static void switchToOldWindow() {
		log.info("Switching  to old window");
		Set s = driver.getWindowHandles();
		Iterator itr = s.iterator();
		String w1 = (String) itr.next();
		String w2 = (String) itr.next();
		driver.switchTo().window(w1);
	}

	public static void switchToDefaultContent() {
		log.info("Switching o default content");
		driver.switchTo().defaultContent();

	}

	public static String getScreenshot(String screenshotName) throws Exception {
		log.info("Taking a screen shot for the failed test case");
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		//System.getProperty("user.dir") +
		String destination =  "./ExtentReport/screenshots/"
				+ screenshotName + timeStamp + ".png";

		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		// Returns the captured file path
		return destination;
	}

	public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec) {
		log.info("Waiting for visibility of an element");
		WebDriverWait wait = new WebDriverWait(driver, timeToWaitInSec);
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static WebElement waitForClickablility(WebElement element, int timeout) {
		log.info("Waiting for an element to be clickable");
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void waitForPageToLoad(long timeOutInSeconds) {
		log.info("Waiting for page to load");
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		try {
			log.info("Waiting --> " + timeOutInSeconds +" Seconds");
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(expectation);
		} catch (Throwable error) {
			log.error(
					"Timeout waiting for Page Load Request to complete after " + timeOutInSeconds + " seconds");
		}
	}

	public static void scrollDown() {
		log.info("Scroll down the page");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 1000);");
	}

	public static List<String> getElementsText(List<WebElement> list) {
		log.info("Get list of elements");
		List<String> elemTexts = new ArrayList<String>();
		for (WebElement el : list) {
			if (!el.getText().isEmpty()) {
				elemTexts.add(el.getText());
			}
		}
		return elemTexts;
	}

	public static void waitMyTime(int i) {
		log.info("wait" + i + "seconds");
		driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

	}

	public static void setWindowSize(int Dimension1, int dimension2) {
		log.info("Setting the window size");
		driver.manage().window().setSize(new Dimension(Dimension1, dimension2));

	}

	public static void pressKeyDown(WebElement element) {
		log.info("Presing the DOWN Key");
		element.sendKeys(Keys.DOWN);
	}

	public void pressKeyEnter(WebElement element) {
		log.info("Presing the ENTER Key");
		element.sendKeys(Keys.ENTER);
	}

	public static void pressKeyUp(WebElement element) {
		log.info("Presing the UP Key");
		element.sendKeys(Keys.UP);
	}

	public static void moveToTab(WebElement element) {
		log.info("Moving to diffrent window tab Using ALT + TAB");
		element.sendKeys(Keys.chord(Keys.ALT, Keys.TAB));
	}

	public static WebElement waitUntilClickableThenClick(WebElement element) {
		log.info("Waiting for element to be clickable");
		WebElement webElement = null;
		for (int i = 1; i <= 6; i++) {
			try {
				webElement = new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(element));
				Thread.sleep(100);
				webElement.getText(); // Use getText method to test if such
										// element is stale or not
				webElement.click();
				return webElement;
			} catch (Throwable t) {
				if (webElement == null)
					break;
				log.info("Failed in attemption No. " + i);
				i++;
				continue;
			}
		}
		throw new RuntimeException("Element state is unknown or cannot find such element");
	}

	public static String waitUntilVisibleThenGetText(WebElement element) {
		log.info("Waiting for element to be visible");
		WebElement webElement = null;
		for (int i = 1; i <= 6; i++) {
			try {
				webElement = new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
				String text = webElement.getText();
				return text;
			} catch (Throwable t) {
				if (webElement == null)
					break;
				log.info("Failed in attemption No. " + i);
				i++;
				continue;
			}
		}
		throw new RuntimeException("Element state is unknown or cannot find such element");
	}

	public static void waitUntilClickableThenSentKeys(WebElement element, String textToSend) {
		log.info("Waiting for element to be visible");
		WebElement webElement = null;
		for (int i = 1; i <= 6; i++) {
			try {
				webElement = new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(element));
				webElement.clear();
				webElement.sendKeys(textToSend);
				return;
			} catch (Throwable t) {
				if (webElement == null)
					break;
				log.info("Failed in attemption No. " + i);
				i++;
				continue;
			}
		}
		throw new RuntimeException("Element state is unknown or cannot find such element");
	}

	public static void CertErrorHandler() {
		// Use the Webdriver instance to navigate to the result of the JS
		log.info("Handling SSL Certificate with JS");
		driver.navigate().to("javascript:document.getElementById('overriderlink').click()");
	}

	public static void selectDateByJS(WebElement element, String dateVal) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].setAttribute('value','" + dateVal + "');", element);

	}

	public static void generateAlert(String message) {
		log.info("Generating alert with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("alert('" + message + "')");
	}

	public static void clickElementByJS(WebElement element) {
		log.info("Clik on element with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].click();", element);

	}

	public static void refreshBrowserByJS() {
		log.info("Refresh the browser with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("history.go(0)");
	}

	public static String getTitleByJS() {
		log.info("Getting page title with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		String title = js.executeScript("return document.title;").toString();
		return title;
	}

	public static String getPageInnerText() {
		log.info("Generating innertext with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		String pageText = js.executeScript("return document.documentElement.innerText;").toString();
		return pageText;
	}

	public static void scrollPageDown() {
		log.info("Scrolling down the page with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	public static void scrollIntoView(WebElement element) {
		log.info("Scrolling to view with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void switchToWindow(String targetTitle) throws Throwable {
		log.info("Swtiching to window with JS");
		String origin = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			if (driver.getTitle().equals(targetTitle)) {
				return;
			}
		}
		driver.switchTo().window(origin);
	}

	public static WebElement fluentWait(final WebElement webElement, int timeinsec) {
		log.info("waiting ..."+ timeinsec +" seconds");
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return webElement;
			}
		});
		return element;
	}

	public static void scrollElementToCenterView(WebElement element) {
		String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

				((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
	}
	
	
	public static void enterTextByJS(WebElement element, String text) {
		log.info("Clik on element with JS");
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].value='"+text+"';", element);

	}
	
	
	
	
	public static void DropFile(File filePath, WebElement target, int offsetX, int offsetY) {
	    if(!filePath.exists()) throw new WebDriverException("File not found: " + filePath.toString());
	     //driver = ((WrapsDriver)target).getWrappedDriver();
	    JavascriptExecutor jse = (JavascriptExecutor)driver;
	    WebDriverWait wait = new WebDriverWait(driver, 30);

	    String JS_DROP_FILE =
	        "var target = arguments[0]," +
	        "    offsetX = arguments[1]," +
	        "    offsetY = arguments[2]," +
	        "    document = target.ownerDocument || document," +
	        "    window = document.defaultView || window;" +
	        "" +
	        "var input = document.createElement('INPUT');" +
	        "input.type = 'file';" +
	        "input.style.display = 'none';" +
	        "input.onchange = function () {" +
	        "  var rect = target.getBoundingClientRect()," +
	        "      x = rect.left + (offsetX || (rect.width >> 1))," +
	        "      y = rect.top + (offsetY || (rect.height >> 1))," +
	        "      dataTransfer = { files: this.files };" +
	        "" +
	        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
	        "    var evt = document.createEvent('MouseEvent');" +
	        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
	        "    evt.dataTransfer = dataTransfer;" +
	        "    target.dispatchEvent(evt);" +
	        "  });" +
	        "" +
	        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
	        "};" +
	        "document.body.appendChild(input);" +
	        "return input;";

	    WebElement input =  (WebElement)jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
	    input.sendKeys(filePath.getAbsoluteFile().toString());
	    wait.until(ExpectedConditions.stalenessOf(input));
	}
	

	private static void sendHumanKeys(WebElement element, String text) {
	    Random r = new Random();
	    for(int i = 0; i < text.length(); i++) {
	        try {
	            Thread.sleep((int)(r.nextGaussian() * 15 + 100));
	        } catch(InterruptedException e) {}
	        String s = new StringBuilder().append(text.charAt(i)).toString();
	        element.sendKeys(s);
	    }
	}

	public static void injectImageAttributeIntoDOM(WebElement parentElement,String imagePath) {
		
		 BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	        String binaryImageCode = JavaUtils.encodeImageToString(img, "png");
	        String final64BitImageCode = "data:image/png;base64,"+binaryImageCode;
		((JavascriptExecutor) driver).executeScript("var ele = arguments[0];var p = document.createElement('img');"
				+ "p.setAttribute('src', arguments[1]);ele.appendChild(p);",parentElement,final64BitImageCode);
	}
	
	
	
	public static void doubleClickWebelementJS(WebElement element) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents');"+ 
                   "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"+ 
                   "arguments[0].dispatchEvent(evt);", element);
        Thread.sleep(2000);
 }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
