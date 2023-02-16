package driver;

import com.github.webdriverextensions.WebComponent;
import org.openqa.selenium.WebDriver;

public class Driver extends WebComponent {
    public static WebDriver driver;
    public static void setup(){
        driver = DriverFactory.getDriver();
    }

    public static void quitDriver(){
        driver.quit();
    }
}
