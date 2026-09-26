package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    // ── Locators ──
    private By dashboardHeader = By.cssSelector("h6.oxd-topbar-header-breadcrumb-module");

    public DashboardPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    // ── Acciones ──
    public boolean isDashboardDisplayed(){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader));
            return true;
        } catch (TimeoutException e){
            return false;
        }
    }
}