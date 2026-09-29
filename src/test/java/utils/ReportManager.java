package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {
    private static ReportManager instance;

    private ExtentReports extentReports;
    private ExtentTest currentTest;

    private ReportManager(){}

    public static ReportManager getInstance(){
        if (instance == null)
            instance = new ReportManager();
        return instance;
    }

    public void init(){
        extentReports = new ExtentReports();
        extentReports.attachReporter(new ExtentSparkReporter("target/reporte.html"));
    }

    public ExtentTest createTest(String name){
        currentTest = extentReports.createTest(name);
        return currentTest;
    }

    public ExtentTest getTest(){
        return currentTest;
    }

    public void flush(){
        extentReports.flush();
    }
}
