package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EmployeeListPage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    // ── Locators ──
    private By searchEmployeeIdInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By addButton = By.xpath("//button[normalize-space()='Add']");
    private By resultRows = By.cssSelector(".oxd-table-body .oxd-table-card");
    private By rowCells = By.cssSelector("[role='cell']");
    private By formLoader = By.cssSelector(".oxd-form-loader");

    public EmployeeListPage(WebDriver webDriver){
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    // ── Acciones ──
    private void waitForLoaderToDisappear(){
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        } catch (Exception ignored) {}
    }

    public void typeEmployeeId(String employeeId){
        waitForLoaderToDisappear();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchEmployeeIdInput));
        input.clear();
        input.sendKeys(employeeId);
    }

    public void clickSearch(){
        List<WebElement> oldRows = webDriver.findElements(resultRows);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        element.click();

        if (!oldRows.isEmpty()) {
            wait.until(ExpectedConditions.stalenessOf(oldRows.get(0)));
        }
    }

    public AddEmployeePage clickAddButton(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        element.click();
        return new AddEmployeePage(webDriver);
    }

    public String getNameForEmployeeId(String employeeId){
        try {
            return new WebDriverWait(webDriver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(driver -> {
                        for (WebElement row : driver.findElements(resultRows)) {
                            List<WebElement> cells = row.findElements(rowCells);
                            if (cells.size() > 2 && cells.get(1).getText().trim().equals(employeeId)) {
                                return cells.get(2).getText().trim();
                            }
                        }
                        return null;
                    });
        } catch (TimeoutException e) {
            return null;
        }
    }
}