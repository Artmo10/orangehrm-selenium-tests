package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SidebarMenu {
    private WebDriver webDriver;
    private WebDriverWait wait;

    private By pimMenuLink = By.cssSelector("a[href='/web/index.php/pim/viewPimModule']");

    public SidebarMenu(WebDriver webDriver){
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    public EmployeeListPage goToPIM(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(pimMenuLink));
        element.click();
        return new EmployeeListPage(webDriver);
    }
}