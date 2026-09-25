package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver webDriver;
    //private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private By dashboardHeader = By.cssSelector("h6.oxd-topbar-header-breadcrumb-module");

    public DashboardPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public boolean isDashboardDisplayed(){
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String getDashboardHeaderText(){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader));
        return element.getText();
    }
}
