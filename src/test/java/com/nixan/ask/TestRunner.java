package com.nixan.ask;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com/nixan/ask/definitions", "com/nixan/ask/support"}
)
public class TestRunner {
}