package com.nixan.ask.support;

import lombok.Getter;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestContext {

    @Getter
    private static WebDriver driver;
    @Getter
    private static BrowserMobProxy proxy;

    public static void initialize() {
        ChromeOptions options = initOptions();
        initialize(options);
    }

    private static void initialize(ChromeOptions options) {
        driver = new ChromeDriver(options);
    }

    private static ChromeOptions initOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--window-size=1920x1080");
        return options;
    }

    public static void initializeWithProxy() {
        ChromeOptions options = initOptions();
        setProxy(options);
        initialize(options);
        proxy.newHar("ask-qa.portnov.com");
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
    }

    private static void setProxy(ChromeOptions options) {
        proxy = new BrowserMobProxyServer();
        proxy.start(0);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        options.setProxy(seleniumProxy);
    }

    public static void tearDown() {
        if (proxy != null) {
            proxy.stop();
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
