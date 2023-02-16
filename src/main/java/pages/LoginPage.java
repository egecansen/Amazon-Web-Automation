package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Utils;

public class LoginPage extends Utils {
    @FindBy(id = "ap_email")
    public WebElement mailInputBox;
    @FindBy(id = "ap_password")
    public WebElement passwordInputBox;
    @FindBy(id = "auth-error-message-box")
    public WebElement errorMessage;
}
