package steps;

import driver.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import utils.Utils;
//import utils.driver.Driver;


import java.util.concurrent.TimeUnit;

public class CommonSteps extends Utils {
    //Driver driver = new Driver();

    @Before
    public void before(Scenario scenario) {
        log.new Success("Running: '" + scenario.getName() + "'");
        Driver.setup();
        //driver.initialize();
    }

    @After
    public void after(Scenario scenario) {
        Driver.quitDriver();
        //driver.terminate();
        if (scenario.isFailed()) log.new Important(scenario.getName() + " is failed!");
        else log.new Success("PASS!");
    }

    @Given("Navigate to {}")
    public void navigateTo(String url) {
        Driver.driver.get(url);
        log.new Info("Navigated to " + url);
    }

    @Given("Wait {} seconds")
    public void wait(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
            log.new Info("Waited for " + duration + " seconds");
        }
        catch (InterruptedException ignored){}
    }
}
