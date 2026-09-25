package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver webDriver;
    private WebDriverWait wait;
    private By userInput = By.name("username");
    private By passWordInput = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver webDriver){
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    public void typeUserName(String user){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(userInput));
        element.sendKeys(user);
    }

    public void typePassWord(String passWord){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passWordInput));
        element.sendKeys(passWord);
    }

    public DashboardPage clickOnLoginButton(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        element.click();
        return new DashboardPage(webDriver);
    }

    public DashboardPage loginAs(String user, String passWord){
        typeUserName(user);
        typePassWord(passWord);
        return clickOnLoginButton();
    }
}
