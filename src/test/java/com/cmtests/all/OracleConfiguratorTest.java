package com.cmtests.all;

/*
This test will test weather Oracle Configurator application server is up and running or not
*/

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class OracleConfiguratorTest {
	private static final int priority = 0;
	ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
     
    @BeforeTest
    public void init()
    {
    	driver = new FirefoxDriver();
        extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/MonitoringResults.html", true);
    }
     
    @Test(priority=1)
    public void configTest() throws InterruptedException
    {
        test = extent.startTest("monitoringTest");        
        test.log(LogStatus.PASS, "Browser started");
        driver.get("http://cv01a027.w3cloud.grpc.ibm.com:9081/Configurator/index.jsp?e=login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement ele=driver.findElement(By.xpath("//div[@id='wrap']/div[1]/div[1]/strong"));
        String content=ele.getText();
        System.out.println("Alert Content is :  "+content);
        Assert.assertEquals("Enter Your IBM EmailId and Password.", content);
        test.log(LogStatus.PASS, "Login alert content");
        boolean searchEmailPresence = driver.findElement(By.xpath("//input[@name='login']")).isDisplayed();
        boolean searchPasswordPresence = driver.findElement(By.xpath("//input[@name='password']")).isDisplayed();
        
        if (searchEmailPresence==true && searchPasswordPresence==true)
        {
        	WebElement email=driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        	email.click();        	
        }    
       
        WebElement elem=driver.findElement(By.xpath(".//*[@id='wrap']/div[1]/div[1]/strong"));
        String alert=elem.getText();
        System.out.println("Alert Content is :  "+alert);
        Assert.assertEquals("Error!", alert);
        test.log(LogStatus.PASS, "Login failed alert content");
        
        
    }
        
    @AfterMethod
    public void getResult(ITestResult result) throws IOException
    {
        if(result.getStatus() == ITestResult.FAILURE)
        {
            String screenShotPath = GetScreenShot.capture(driver, "Monitoring");
            test.log(LogStatus.FAIL, result.getThrowable());
            test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(screenShotPath));
        }
        extent.endTest(test);
    }
     
         
    @AfterTest
    public void endreport()
    {
        driver.close();
        extent.flush();
        extent.close();
    }
}


