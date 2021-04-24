package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.File;
import java.time.Duration;
import java.util.function.Function;

import static base.ScenarioAttributes.*;
import static utility.FileReaders.getDataFromPropertiesFile;
import static utility.HandleErrors.errorHandler;

public class DriverFunctions {

    public void initBrowser(String browser){
        try{
            switch (browser.toLowerCase()){
                case "firefox":
//                    WebDriverManager.firefoxdriver().setup();
                    System.setProperty("webdriver.gecko.driver","driver/geckodriver");
                    driver=new FirefoxDriver();
                    break;
                default:
//                    WebDriverManager.chromedriver().setup();
                    System.setProperty("webdriver.chrome.driver","driver/chromedriver");
                    driver=new ChromeDriver();
                    break;
            }
            driver.manage().window().maximize();
        }catch(Exception e){
            e.printStackTrace();
            errorHandler("Exception while launching browser",false);
        }
    }

    public void cleanup(){
        driver.quit();
    }

    public void launchSite(){
        try{
            initBrowser(getDataFromPropertiesFile("TestConfig","browser"));
            driver.get(getDataFromPropertiesFile("TestConfig","url"));
        }catch(Exception ex){
            ex.printStackTrace();
            errorHandler("Exception while navigating to the url",false);
        }
    }

    public WebElement waitForElementAndReturnElement(final String xpath){
        FluentWait wait=new FluentWait(driver).withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(Exception.class);
        WebElement e=(WebElement) wait.until(new Function<WebDriver,WebElement>(){
            public WebElement apply(WebDriver driver){
                return driver.findElement(By.xpath(xpath));
            }
        });
        return e;
    }

    public void alertAction(String action){
        try{
            switch(action.toLowerCase()){
                case "accept":
                    driver.switchTo().alert().accept();
                    break;
                case "dismiss":
                    driver.switchTo().alert().dismiss();
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
            errorHandler("Exception while performing "+action+" on the alert",false);
        }
    }

    public boolean textBoxEntry(final String value,final String identifier,boolean retry){
        try {
            WebElement el = waitForElementAndReturnElement(identifier);
            el.sendKeys(value);
            return true;
        }catch (StaleElementReferenceException b) {
            if(retry){
                textBoxEntry(value,identifier,false);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while performing text box entry",false);
            return false;
        }
    }

    public boolean clickElement(final String identifier){
        try {
            WebElement el = waitForElementAndReturnElement(identifier);
            el.click();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while performing click",false);
            return false;
        }
    }

    public boolean jsClick(final String identifier){
        try {
            WebElement el = waitForElementAndReturnElement(identifier);
            JavascriptExecutor js= (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();",el);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while performing jsClick",false);
            return false;
        }
    }

    public boolean takeScreenshot(final String name){
        try{
            File F=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            if(F!=null){
                FileUtils.copyFile(F, new File(name+".png"));
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
