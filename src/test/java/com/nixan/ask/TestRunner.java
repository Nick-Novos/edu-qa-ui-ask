package com.nixan.ask;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        features = "src/test/resources/features",
        glue = {"com/nixan/ask/pages", "com/nixan/ask/steps", "com/nixan/ask/support"},
        tags = ""
)
public class TestRunner {
}