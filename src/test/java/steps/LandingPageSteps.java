package steps;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebElement;
import pages.LandingPage;
import utils.ContextStore;

import static utils.WebUtilities.ElementState.DISPLAYED;
import static utils.WebUtilities.ElementState.ENABLED;

public class LandingPageSteps {
    LandingPage landingPage = new LandingPage();

    @Given("Go to login section")
    public void goToLoginPage() {
        landingPage.log.new Info("Navigating to login section");
        landingPage.clickElementUntil(landingPage.signInButton);
    }

   @Given("Click on drop down menu button and expend the targeted section")
    public void checkLinksOnMenu() {
       landingPage.log.new Info("Clicking on drop down menu button");
       landingPage.clickElementUntil(landingPage.dropDowMenuButton);

       landingPage.log.new Info("Clicking on 'See All' button");
       landingPage.clickAtAnOffset(landingPage.profileSectionOnMenu, 30, 460, true);
       //landingPage.clickElement(landingPage.seeAllButton, false);
    }

    @Given("Create a text file and save link info on the section named {} from the drop down menu on the landing page")
    public void checkLinks(String sectionName) {
        WebElement menuItem = landingPage.getElementFromListUntil(sectionName, landingPage.menuItems);
        landingPage.log.new Info("Clicking on menu item named: '" + sectionName + "'");
        landingPage.clickElementUntil(menuItem);

        landingPage.log.new Info("Saving the info on file named: " + sectionName);
       landingPage.saveLinkInfoOnTextFile(sectionName, landingPage.elements);

        landingPage.log.new Info("Getting back to main menu");
        landingPage.clickAtAnOffset(landingPage.profileSectionOnMenu, 5, 40, false);
    }
    @Given("Get back on main menu")
    public void getBackonMenu() {
        landingPage.log.new Info("Getting back to main menu");
    }
}
