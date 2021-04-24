package utility;

import org.junit.Assert;

import static base.ScenarioAttributes.driver;

public class HandleErrors {

    public static void errorHandler(String error, boolean result){
        try{
            if(!result){
                driver.quit();
            }
            Assert.assertTrue(error,result);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("exception in error handler");
        }
    }
}
