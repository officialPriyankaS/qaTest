package pages;

import base.DriverFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static base.ScenarioAttributes.driver;
import static utility.FileReaders.getDataFromPropertiesFile;
import static utility.HandleErrors.errorHandler;

public class MidtransPage extends DriverFunctions {
    public void user_launch_the_store_site() {
        try{
            launchSite();
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while launching site",false);
        }
    }

    public void user_clicks_on_buy_now() {
        try{
            jsClick(getDataFromPropertiesFile("midtrans","buynow"));
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while clicking on buy now",false);
        }
    }

    public void click_on_checkout_with_credit_card(String cardnumber,String expirydate,String cvv,String otp) {
        try{
            jsClick(getDataFromPropertiesFile("midtrans","checkout"));
            driver.switchTo().frame("snap-midtrans");
            jsClick(getDataFromPropertiesFile("midtrans","continuePay"));
            jsClick(getDataFromPropertiesFile("midtrans","creditcard"));
            driver.findElement(By.name("cardnumber")).sendKeys(cardnumber);
            driver.findElement(By.xpath(getDataFromPropertiesFile("midtrans","expirydate"))).sendKeys(expirydate);
            driver.findElement(By.xpath(getDataFromPropertiesFile("midtrans","cvv"))).sendKeys(cvv);
            jsClick(getDataFromPropertiesFile("midtrans","paynow"));
            //driver.switchTo().defaultContent();
            WebElement e=waitForElementAndReturnElement(getDataFromPropertiesFile("midtrans","otpiframe"));
            driver.switchTo().frame(e);
            e=waitForElementAndReturnElement(getDataFromPropertiesFile("midtrans","otp"));
            e.sendKeys(otp);
            jsClick(getDataFromPropertiesFile("midtrans","okotp"));
            driver.switchTo().parentFrame();
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while checking out with credit card",false);
        }
    }

    public void transaction_successful_message() {
        try{
            WebElement e=waitForElementAndReturnElement(getDataFromPropertiesFile("midtrans","transactionsuccessful"));
            errorHandler("Error=> Expected: Transaction successful <==> Actual: "+e.getText(),e.getText().equalsIgnoreCase("Transaction successful"));
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while checking out transaction successful message",false);
        }
    }

    public void user_navigates_to_home_page() {
        try{
            Thread.sleep(4000);
            driver.switchTo().defaultContent();
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while navigating to home page",false);
        }
    }

    public void verify_purchase_successful_message() {
        try{
            WebElement e=waitForElementAndReturnElement(getDataFromPropertiesFile("midtrans","purchasesuccessful"));
            errorHandler("Error while checking out transaction successful message",e.getText().contains("Thank you for your purchase."));
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while verifying message",false);
        }
    }

    public void transaction_failure_message() {
        try{
            WebElement e=waitForElementAndReturnElement(getDataFromPropertiesFile("midtrans","transactionfailed"));
            errorHandler("Error-> Expected : Transaction failed <==> Actual: "+e.getText(),e.getText().equalsIgnoreCase("Transaction failed"));
            cleanup();
        }catch (Exception e){
            e.printStackTrace();
            errorHandler("Exception while checking out transaction successful message",false);
        }
    }
}
