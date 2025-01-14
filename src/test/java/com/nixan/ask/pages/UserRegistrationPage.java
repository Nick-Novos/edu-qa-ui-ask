package com.nixan.ask.pages;

import com.nixan.ask.pages.model.Fields;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.nixan.ask.pages.model.Fields.EMAIL;

public class UserRegistrationPage extends BasePage {

    @FindBy(xpath = "//form")
    private WebElement form;

    @FindBy(xpath = "//form//input")
    private List<WebElement> inputs;

    @FindBy(xpath = "//form//button[@type='submit']")
    private WebElement registerButton;

    public UserRegistrationPage() {
        PageFactory.initElements(driver, this);
    }

    public void openPage() {
        driver.get("http://ask-qa.portnov.com/#/registration/");
    }

    public boolean isFormDisplayed() {
        return form.isDisplayed() && form.isEnabled();
    }

    public boolean areAllInputFieldsDisplayed() {
        return inputs.stream().allMatch(i -> i.isDisplayed() && i.isEnabled());
    }

    public boolean areAllInputFieldsEmpty() {
        return inputs.stream().allMatch(
                i -> Optional.ofNullable(i.getDomAttribute("value")).orElse("").isEmpty());
    }

    public void fillRegistrationForm(LinkedHashMap<String, String> userInfo) {
        userInfo.forEach((fieldName, value) -> {
            Fields field = Fields.findByName(fieldName);
            WebElement element = field.getActualElement(driver);
            sendKeysIfPresent(value, element);
        });
    }

    private void sendKeysIfPresent(String text, WebElement element) {
        if (text != null) {
            element.sendKeys(text);
        }
    }

    public void clickRegisterButton() {
        registerButton.click();
    }

    public HarRequest getCreatingUserRequest() {
        Har har = proxy.getHar();
        return har.getLog().getEntries().stream()
                .map(HarEntry::getRequest)
                .filter(request -> request.getMethod().equals("POST") && request.getUrl().contains("/sign-up"))
                .findFirst()
                .orElse(null);
    }

    public boolean isWarningMessageNextToFieldDisplayed(String fieldName) {
        Fields field = Fields.findByName(fieldName);
        return field.hasError(driver);
    }

    public String getWarningMessageNextToField(String fieldName) {
        Fields field = Fields.findByName(fieldName);
        return field.getError(driver).getText();
    }

    public boolean isExactlyOneWarningMessagePresent() {
        return Fields.warningMessageCount(driver) == 1;
    }

    public boolean isRedirectedToSuccessPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("registration-confirmation"));

        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "http://ask-qa.portnov.com/#/registration-confirmation";
        return expectedUrl.equals(currentUrl);
    }

    public void fillEmail(String email) {
        EMAIL.getActualElement(driver).sendKeys(email);
    }
}
