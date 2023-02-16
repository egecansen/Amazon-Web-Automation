package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Utils;

import java.util.List;

public class LandingPage extends Utils {

    @FindBy(css = ".nav-line-1-container")
    public WebElement signInButton;

    @FindBy(id = "nav-hamburger-menu")
    public WebElement dropDowMenuButton;

    @FindBy(css = "[class*='hmenu-visible'][class*='hmenu-translateX'] li a")
    public List<WebElement> elements;

    @FindBy(css = "[class*='hmenu-visible'] .hmenu-item")
    public List<WebElement> menuItems;

    @FindBy(id = "hmenu-customer-profile")
    public WebElement profileSectionOnMenu;

    @FindBy(css = "[class=\"hmenu-item hmenu-compressed-btn\"]")
    public WebElement seeAllButton;
    //buttonLoc1  : (0, 464)
    //buttonLoc2 : (0, 739)
}