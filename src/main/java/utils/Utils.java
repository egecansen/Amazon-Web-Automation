package utils;

import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import driver.Driver;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Utils extends WebUtilities {

    //public final Printer log = new Printer(this.getClass());
    public int counter;

    public Utils() {
        super(Driver.driver);
        PageFactory.initElements(new WebDriverExtensionFieldDecorator(driver), this);
        //PageFactory.initElements(driver, this);
    }

    public void scroll(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollAndFill(WebElement inputBox, CharSequence... text) {
        scroll(inputBox);
        clickElementUntil(inputBox);
        inputBox.sendKeys(text);
    }

    public void scrollAndClick(WebElement element) {
        scroll(element);
        clickElementUntil(element);
        log.new Info("Scrolled to '" + element.getText() + "' and clicked");
    }

    public void clickAndDeleteAll(WebElement element) {
        clickElementUntil(element);
        element.sendKeys(Keys.COMMAND + "a", Keys.DELETE);
    }

    public void clickAllElements(List<WebElement> elements) {
        for (WebElement element : elements) {
            clickElementUntil(element);
        }
    }

    public void scrollThruCenter(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = " +
                "Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
    }
    public void scrollThruCenterAndClick(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = " +
                "Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
        clickElementUntil(element);
    }

    public void waitFor(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
            log.new Info("Waited for '" + duration + "' seconds");
        } catch (InterruptedException ignored) {
        }
    }

    public void waitUntilDisplayed(WebElement element) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>((WebDriver) driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        try {
            log.new Info("Waiting the element to be displayed");
            fluentWait.until(webDriver -> element.isDisplayed());
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred");

        }
    }

    public void waitUntilListToBeDisplayed(List<WebElement> elements) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>((WebDriver) driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        try {
            log.new Info("Waiting the element list to be displayed");
            fluentWait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException("Exception occurred: ", e);
        }
    }

    public void waitUntilStable(WebElement element) {
        log.new Info("Waiting " + element.getText() + " to be stable");
        final long startTime = System.currentTimeMillis();

        FluentWait<WebDriver> fluentWait = new FluentWait<>((WebDriver) driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        fluentWait.until(ExpectedConditions.stalenessOf(element));

        long endTime = System.currentTimeMillis();
        int totalTime = (int) (endTime - startTime);
        log.new Info("Waited for " + totalTime + " second(s)");
    }

    public void clickElementUntil(WebElement element) {

        final long startTime = System.currentTimeMillis();
        boolean isClicked = false;
        int attemptCounter = 0;
        int duration = 30000;
        while (System.currentTimeMillis() - startTime < duration) {
            try {
                element.click();
                isClicked = true;
                break;
            } catch (WebDriverException e) {
                attemptCounter++;
            }
        }
        if (!isClicked) {
            throw new RuntimeException("Could not click to '" + element.getText() + "' after " + attemptCounter + " attempts");
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        //log.new Info("Clicked for " + totalTime/1000 + " seconds");
    }

    public void moveToElement(WebElement target) {
        Actions actions = new Actions(driver);
        actions.moveToElement(target).perform();
    }
    public void moveToElementAndClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
        clickElementUntil(element);
    }

    public void verifyImgStatus(WebElement imgElement) {
        try {
            RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
            HttpClientBuilder customizedClientBuilder = HttpClients.custom().setDefaultRequestConfig(customizedRequestConfig);
            CloseableHttpClient client = customizedClientBuilder.build();
            HttpGet request = new HttpGet(imgElement.getAttribute("src"));
            HttpResponse response = client.execute(request);
            // print image link and response
            String imgLink = imgElement.getAttribute("src");
            log.new Info("Image Link: " + imgLink);
            log.new Info("Status: " + response.getStatusLine());
            // print name
            //WebElement followingSibling = imgElement.findElement(By.xpath("following-sibling::*"));
            //log.new Info("Image name: " + followingSibling.getText());

            if (response.getStatusLine().getStatusCode() < 200) counter++;
            else if (response.getStatusLine().getStatusCode() > 226) counter++;
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred\n" + e.getMessage());
        }
    }

    public void verifyLinkElements(List<WebElement> linkElements) {
        for (WebElement linkElement : linkElements) {
            if(linkElement.getAttribute("href") != null) {
                try {
                    String webLink = linkElement.getAttribute("href");
                    log.new Info("Link: " + webLink);

                    RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
                    HttpClientBuilder customizedClientBuilder = HttpClients.custom().setDefaultRequestConfig(customizedRequestConfig);
                    CloseableHttpClient client = customizedClientBuilder.build();
                    HttpGet request = new HttpGet(webLink);
                    HttpResponse response = client.execute(request);
                    log.new Info("Status: " + response.getStatusLine());

                    if (response.getStatusLine().getStatusCode() >= 400) counter++;
                } catch (WebDriverException | IOException e) {
                    log.new Warning(e.getMessage());
                }
            }
        }
    }

    public void saveLinkInfoOnTextFile(String fileName, List<WebElement> linkElements) {
        for (WebElement linkElement : linkElements) {
            if(linkElement.getText().length() > 1 && linkElement.getAttribute("href") != null) {
                try {
                    String webLink = linkElement.getAttribute("href");
                    String elementText = linkElement.getText();
                    log.new Info("Element name: " + elementText);
                    log.new Info("Link: " + webLink);

                    RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
                    HttpClientBuilder customizedClientBuilder = HttpClients.custom().setDefaultRequestConfig(customizedRequestConfig);
                    CloseableHttpClient client = customizedClientBuilder.build();
                    HttpGet request = new HttpGet(linkElement.getAttribute("href"));
                    HttpResponse response = client.execute(request);
                    log.new Success("Status: " + response.getStatusLine());

                    if (response.getStatusLine().getStatusCode() >= 400) counter++;

                    saveTextToTextFile(fileName, "Element name: " + elementText
                            + "\n" + "Status: " + response.getStatusLine()
                            + "\n" + "Links: " + webLink + "\n");

                } catch (WebDriverException | IOException e) {
                    log.new Warning(e.getMessage());
                }
            }
        }
    }

    public String turkishCharReplacer(String text) {
        return text.replace("ı", "i")
                .replace("ö", "o")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ğ", "g")
                .replace("ç", "c")
                .replace("Ü", "U")
                .replace("İ", "I")
                .replace("Ö", "O")
                .replace("Ü", "U")
                .replace("Ş", "S")
                .replace("Ğ", "G")
                .replace("Ç", "C");
    }

    public void tabSwitcher(int tabNumber) {
        ContextStore.put("parentHandle", driver.getWindowHandle());
        List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
        log.new Info(browserTabs.size());
        driver.switchTo().window(browserTabs.get(tabNumber));
        log.new Info("Tab focus switched");
    }

    public String switchWindowByIndex(Integer expectedNumberOfWindows, Integer tabIndex) {
        new WebDriverWait(driver, Duration.ofSeconds(5000)).until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
        log.new Info("Switching the tab with the window index: " + tabIndex);
        String parentWindowHandle = driver.getWindowHandle();
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        String handle = handles.get(tabIndex);
        driver = (ChromeDriver) driver.switchTo().window(handle);
        return parentWindowHandle;
    }

    public void windowSwitcher() {
        log.new Info("Changing window focus");
        ContextStore.put("parentWindow", driver.getWindowHandle());
        String currentWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(currentWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        log.new Info("Window focus changed");
    }

    public void twoTabSwitcher() {
        log.new Info("Changing window focus");
        new WebDriverWait(driver, Duration.ofSeconds(5000)).until(ExpectedConditions.numberOfWindowsToBe(2));
        ContextStore.put("parentWindow", driver.getWindowHandle());
        String currentWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(currentWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        log.new Info("Window focus changed");
    }

    public void verifyTextOfListedElement(String labelText, List<WebElement> elements) {
        Assert.assertEquals("Text of the listed element could not be verified", labelText, getElementFromList(labelText, elements).getText());
    }

    public WebElement getElementFromList(String labelText, List<WebElement> elements) {
        log.new Info("Getting '" + labelText + "' from the given list");
        for (WebElement element : elements)
            if (element.getText().equalsIgnoreCase(labelText)) return element;
        throw new RuntimeException("Element not found!!");
    }
    public WebElement getElementFromListUntil(String labelText, List<WebElement> elements) {
        final long startTime = System.currentTimeMillis();
        int duration = 30000;
        log.new Info("Getting '" + labelText + "' from the given list");
        while (System.currentTimeMillis() - startTime < duration){
            for (WebElement element : elements)
                if (element.getText().equalsIgnoreCase(labelText)){
                    return element;
                }
        }
        throw new RuntimeException("Element not found!!");
    }

    public WebElement getElementFromElements(List<WebElement> elements) {
        try {
            for (WebElement element : elements)
                return element;
        }
        catch (WebDriverException e){
            log.new Warning(e.getMessage());
        }
            return null;
    }

    public void saveLinksToTextFile(String fileName, List<WebElement> elements) {
        log.new Info("Saving links to a text file named: " + fileName);
        File f = new File(fileName + ".txt");
        String linkTexts;
        if (f.exists()) f.delete();
        try {
            FileWriter fw = new FileWriter(f, true);
            for (WebElement element : elements) {
                String linkText = element.getAttribute("href");
                //write text to file
                fw.write(linkText + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.new Info("Error!" + e.getMessage());
        }
    }

    public List<String> readLinksOnBrowser(List<WebElement> elements) {
        List<String> linkList = new ArrayList<String>();
        for (WebElement element : elements){
            log.new Info(element.getAttribute("href"));
            linkList.add(element.getAttribute("href"));
        }
        return linkList;

    }

    public List<String> readTextFromFile(String fileName) {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));

            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void saveTextToTextFile(String fileName, String text) {
        log.new Info("Saving text on a text file named: " + fileName);
        File f = new File("src/test/resources/results/" + fileName + ".txt");
        try {
            FileWriter fw = new FileWriter(f, true);
            fw.write(text + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.new Info("Error!" + e.getMessage());
        }

    }

}