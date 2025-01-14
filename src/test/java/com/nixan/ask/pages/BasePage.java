package com.nixan.ask.pages;

import com.nixan.ask.support.TestContext;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected final WebDriver driver = TestContext.getDriver();
    protected final BrowserMobProxy proxy = TestContext.getProxy();
}
