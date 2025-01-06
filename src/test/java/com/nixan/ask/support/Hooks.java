package com.nixan.ask.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before(order = 0)
    public void scenarioStart() {
        TestContext.initializeWithProxy();
        TestContext.getDriver().manage().deleteAllCookies();
    }

    @After(order = 0)
    public void scenarioEnd(Scenario scenario) {
        if (scenario.isFailed()) {
            TakesScreenshot screenshot = ((TakesScreenshot) TestContext.getDriver());
            byte[] bytes = screenshot.getScreenshotAs(OutputType.BYTES);
            scenario.attach(bytes, "image/png", "screenshot_%s".formatted(System.currentTimeMillis()));
        }
        TestContext.tearDown();
    }
}
