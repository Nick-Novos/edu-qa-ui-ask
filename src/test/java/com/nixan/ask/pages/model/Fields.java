package com.nixan.ask.pages.model;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public enum Fields {

    FIRST_NAME("First Name", By.xpath("//form//input[@placeholder='First Name']")),
    LAST_NAME("Last Name", By.xpath("//form//input[@placeholder='Last Name']")),
    EMAIL("Email", By.xpath("//form//input[@placeholder='Email']")),
    GROUP_CODE("Group Code", By.xpath("//form//input[@placeholder='Group Code']")),
    PASSWORD("Password", By.xpath("//form//input[@placeholder='Password']")),
    CONFIRM_PASSWORD("Confirm Password", By.xpath("//form//input[@placeholder='Confirm Password']"));

    private static final By error = By.xpath("//ancestor::mat-form-field//mat-error");

    @Getter
    private final String name;
    private final By locator;

    Fields(String name, By locator) {
        this.name = name;
        this.locator = locator;
    }

    public static Fields findByName(String name) {
        for (Fields value : Fields.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant with name: " + name);
    }

    public static int warningMessageCount(WebDriver driver) {
        return driver.findElements(error).size();
    }

    public WebElement getActualElement(WebDriver driver) {
        return driver.findElement(locator);
    }

    public boolean hasError(WebDriver driver) {
        try {
            return getError(driver).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public WebElement getError(WebDriver driver) {
        return driver.findElement(locator).findElement(error);
    }
}
