package base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.ReportManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver webDriver;

    @BeforeSuite
    public void initReport(){
        ReportManager.getInstance().init();
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser, Method method){
        ReportManager.getInstance().createTest(method.getName() + " [" + browser + "]");
        if (browser.equalsIgnoreCase("firefox")) {
            webDriver = new FirefoxDriver();
        } else {
            webDriver = new ChromeDriver(chromeSinGestorDeContrasenas());
        }
        webDriver.manage().window().maximize();
        webDriver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result){
        ExtentTest test = ReportManager.getInstance().getTest();
        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> test.pass("Test aprobado");
            // La captura se toma antes de cerrar el navegador
            case ITestResult.FAILURE -> test.fail(result.getThrowable().getMessage(), takeScreenshot());
            case ITestResult.SKIP -> test.skip("Test omitido");
        }
        if(webDriver != null)
            webDriver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport(){
        ReportManager.getInstance().flush();
    }

    // Si la captura falla, el FAIL se registra sin imagen y el navegador se cierra igual
    private Media takeScreenshot(){
        try {
            String screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);
            return MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build();
        } catch (Exception e) {
            return null;
        }
    }

    private ChromeOptions chromeSinGestorDeContrasenas(){
        Map<String, Object> preferencias = new HashMap<>();
        preferencias.put("credentials_enable_service", false);
        preferencias.put("profile.password_manager_enabled", false);
        preferencias.put("profile.password_manager_leak_detection", false);

        ChromeOptions opciones = new ChromeOptions();
        opciones.setExperimentalOption("prefs", preferencias);
        opciones.addArguments("--disable-features=PasswordLeakDetection,AutofillServerCommunication");
        return opciones;
    }
}