package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AddEmployeePage {
    private WebDriver webDriver;
    private WebDriverWait wait;

    // ── Locators ──
    private By firstNameInput = By.name("firstName");
    private By middleNameInput = By.name("middleName");
    private By lastNameInput = By.name("lastName");
    private By employeeIdInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private By createLoginDetailsToggle = By.cssSelector(".oxd-switch-wrapper .oxd-switch-input");
    private By usernameInput = By.xpath("//label[text()='Username']/following::input[1]");
    private By passwordInputs = By.cssSelector("input[type='password']");
    private By statusEnabledRadio = By.cssSelector("input[type='radio'][value='1'] + span");
    private By statusDisabledRadio = By.cssSelector("input[type='radio'][value='2'] + span");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By successToast = By.xpath("//*[contains(@class,'oxd-toast') and contains(.,'Successfully Saved')]");
    private By employeeListTab = By.xpath("//a[normalize-space()='Employee List']");
    private By formLoader = By.cssSelector(".oxd-form-loader");

    public AddEmployeePage(WebDriver webDriver){
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

    public void typeFirstName(String firstName){
        waitForLoaderToDisappear();
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        element.clear();
        element.sendKeys(firstName);
    }

    public void typeMiddleName(String middleName){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(middleNameInput));
        element.clear();
        element.sendKeys(middleName);
    }

    public void typeLastName(String lastName){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        element.clear();
        element.sendKeys(lastName);
    }

    public String getGeneratedEmployeeId(){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdInput));
        return element.getAttribute("value");
    }

    public void enableCreateLoginDetails(){
        waitForLoaderToDisappear();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(createLoginDetailsToggle));
        element.click();
    }

    public void typeUsername(String username){
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        element.clear();
        element.sendKeys(username);
    }

    public void typePassword(String password){
        List<WebElement> passwordFields = webDriver.findElements(passwordInputs);
        passwordFields.get(0).sendKeys(password);
    }

    public void typeConfirmPassword(String confirmPassword){
        List<WebElement> passwordFields = webDriver.findElements(passwordInputs);
        passwordFields.get(1).sendKeys(confirmPassword);
    }

    public void selectStatus(String status){
        By radioLocator = status.equalsIgnoreCase("Enabled") ? statusEnabledRadio : statusDisabledRadio;
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(radioLocator));
        element.click();
    }

    public void saveEmployee(){
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        element.click();
    }

    public boolean isEmployeeSaved(){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public EmployeeListPage goToEmployeeListTab(){
        waitForLoaderToDisappear();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(employeeListTab));
        element.click();
        return new EmployeeListPage(webDriver);
    }
}