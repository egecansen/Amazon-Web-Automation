package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import pages.LoginPage;

import java.util.Map;

public class LoginPageSteps {
    LoginPage loginPage = new LoginPage();
    @Given("Submit the following info:")
    public void loginInfo(DataTable table) {
        loginPage.log.new Info("Submitting the given login info");
        Map<String, String> userMap = table.asMap();
        try {
            loginPage.log.new Info("Waiting for the mail box element to be clickable");
            loginPage.clickElement(loginPage.mailInputBox, false);

            loginPage.log.new Info("Submitting the given info");
            loginPage.mailInputBox.sendKeys(userMap.get("Email"), Keys.ENTER);
            loginPage.passwordInputBox.sendKeys(userMap.get("Password"), Keys.ENTER);
            if (loginPage.errorMessage.isDisplayed()) loginPage.log.new Warning("Invalid user information!");
            else loginPage.log.new Success("Submitted successfully");
        }
        catch (WebDriverException e) {
            loginPage.log.new Error("Login failed!", e);
        }
    }
}
