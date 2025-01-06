package com.nixan.ask.definitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixan.ask.support.annotations.IgnoreQuotes;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserRegistration extends BaseTest {

    @FindBy(xpath = "//form")
    private WebElement form;

    @FindBy(xpath = "//form//input[@placeholder='First Name']")
    private WebElement firstName;

    @FindBy(xpath = "//form//input[@placeholder='Last Name']")
    private WebElement lastName;

    @FindBy(xpath = "//form//input[@placeholder='Email']")
    private WebElement email;

    @FindBy(xpath = "//form//input[@placeholder='Group Code']")
    private WebElement groupCode;

    @FindBy(xpath = "//form//input[@placeholder='Password']")
    private WebElement password;

    @FindBy(xpath = "//form//input[@placeholder='Confirm Password']")
    private WebElement confirmPassword;

    @FindBy(xpath = "//form//button[@type='submit']")
    private WebElement registerButton;

    private boolean ignoreQuotes;

    public UserRegistration() {
        PageFactory.initElements(driver, this);
    }

    @Before
    public void before(Scenario scenario) {
        ignoreQuotes = scenario.getSourceTagNames().contains('@' + IgnoreQuotes.class.getSimpleName());
    }

    @Given("the user registration page is open")
    public void theUserRegistrationPageIsOpen() {
        driver.get("http://ask-qa.portnov.com/#/registration/");
    }

    @And("the registration form is blank")
    public void isRegistrationFormIsBlank() {
        assertTrue(form.isDisplayed());
        List<WebElement> inputs = form.findElements(By.xpath("//input"));
        for (WebElement i : inputs) {
            assertTrue("Not displayed input field found", i.isDisplayed());
            assertTrue("Disabled input field found", i.isEnabled());
            String value = i.getDomAttribute("value");
            assertTrue("Prefilled input field found", value == null || value.isEmpty());
        }
    }

    @When("the user fills in the registration form with the following details:")
    public void theUserFillsInTheRegistrationFormWithTheFollowingDetails(Map<String, String> userInfo) {
        if (ignoreQuotes) {
            userInfo = new HashMap<>(userInfo);
            removeQuotes(userInfo);
        }
        sendKeysIfPresent(userInfo.get("First Name"), firstName);
        sendKeysIfPresent(userInfo.get("Last Name"), lastName);
        final String userEmail = userInfo.get("Email");
        sendKeysIfPresent((isRequiredToBeUnique(userEmail) ? generateUniqueEmail(userEmail) : userEmail), email);
        sendKeysIfPresent(userInfo.get("Group Code"), groupCode);
        sendKeysIfPresent(userInfo.get("Password"), password);
        sendKeysIfPresent(userInfo.get("Confirm Password"), confirmPassword);
    }

    private void sendKeysIfPresent(String text, WebElement element) {
        if (text != null) {
            element.sendKeys(text);
        }
    }

    private boolean isRequiredToBeUnique(String email) {
        return email != null && email.contains("%s");
    }

    private String generateUniqueEmail(String email) {
        return email.formatted(System.currentTimeMillis());
    }

    private void removeQuotes(Map<String, String> map) {
        map.replaceAll((k, v) -> {
            if (v == null) {
                return null;
            }
            if ((v.startsWith("\"") || v.startsWith("'")) && (v.endsWith("\"") || v.endsWith("'"))) {
                return v.substring(1, v.length() - 1);
            }
            return v;
        });
    }

    @And("the user clicks the \"Register\" button")
    public void theUserClicksTheRegisterButton() {
        registerButton.click();
    }

    @And("no API request is made to create a user")
    public void noApiRequestIsMadeToCreateAUser() {
        HarRequest request = getCreatingUserRequest();
        if (request != null) {
            fail("A user creation request was unexpectedly sent. Body:\n" + request.getPostData().getText());
        }
    }

    private HarRequest getCreatingUserRequest() {
        Har har = proxy.getHar();
        return har.getLog().getEntries().stream()
                .map(HarEntry::getRequest)
                .filter(request -> request.getMethod().equals("POST") && request.getUrl().contains("/sign-up"))
                .findFirst()
                .orElse(null);
    }


    @Then("a warning message {string} should be displayed next to the {string} field")
    public void aWarningMessageShouldBeDisplayedNextToTheField(String message, String fieldName) {
        switch (fieldName) {
            case "First Name" -> verifyWarningMessage("//mat-form-field[1]//mat-error", message, fieldName);
            case "Last Name" -> verifyWarningMessage("//mat-form-field[2]//mat-error", message, fieldName);
        }
    }

    private void verifyWarningMessage(String messageXpath, String expectedMessage, String fieldName) {
        List<WebElement> elements = driver.findElements(By.xpath(messageXpath));
        if (elements.isEmpty()) {
            fail("A warning message is not displayed next to the '%s' field.".formatted(fieldName));
        }
        assertEquals(expectedMessage, elements.get(0).getText());
    }

    @And("an API request is made to create a user")
    public void anApiRequestIsMadeToCreateAUser() {
        HarRequest request = getCreatingUserRequest();
        if (request == null || request.getPostData() == null || request.getPostData().getText().isBlank()) {
            fail("A user creation request was not sent.");
        } else {
            String requestBody = request.getPostData().getText();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> userData = objectMapper.readValue(requestBody, Map.class);
                assertAll("User creation request validation",
                        () -> assertPresent(userData.get("name")),
                        () -> assertPresent(userData.get("email")),
                        () -> assertPresent(userData.get("password")),
                        () -> assertPresent(userData.get("group"))
                );
            } catch (JsonProcessingException e) {
                fail("Failed to parse the user creation request body: " + e.getMessage());
            }
        }
    }

    private void assertPresent(String fieldName) {
        assertNotNull(fieldName);
        assertFalse(fieldName.isBlank());
    }

    @And("the user is redirected to the successful registration page")
    public void theUserIsRedirectedToTheSuccessfulRegistrationPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("registration-confirmation"));

        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "http://ask-qa.portnov.com/#/registration-confirmation";
        assertEquals("User was not redirected to the successful registration page", expectedUrl, currentUrl);
    }

    @Then("no warning messages should be displayed next to the {string} field")
    public void noWarningMessagesShouldBeDisplayedNextToTheField(String fieldName) {
        switch (fieldName) {
            case "First Name" -> verifyNoWarningMessage("//mat-form-field[1]//mat-error", fieldName);
            case "Last Name" -> verifyNoWarningMessage("//mat-form-field[2]//mat-error", fieldName);
        }
    }

    @Then("no warning messages are displayed for any field other than {string}")
    public void noWarningMessagesAreDisplayedForAnyFieldOtherThan(String fieldName) {
        List<WebElement> elements = driver.findElements(By.xpath("//mat-form-field//mat-error"));
        if (elements.size() > 1) {
            fail("There should be only one warning message");
        }
    }

    private void verifyNoWarningMessage(String messageXpath, String fieldName) {
        List<WebElement> elements = driver.findElements(By.xpath(messageXpath));
        if (!elements.isEmpty()) {
            fail("Warning validation message next to '%s' is displayed with the text: %s"
                    .formatted(fieldName, elements.get(0).getText()));
        }
    }

    @And("the user fills in an email {string} containing {int} characters")
    public void theUserFillsInAnEmailContainingCharacters(String email, Integer length) {
        email = makeEmailExactLong(email, length);
        this.email.sendKeys(email);
    }

    private String makeEmailExactLong(String email, int length) {
        long currentTimeMillis = System.currentTimeMillis();
        email = email.formatted(currentTimeMillis);
        if (email.length() < length) {
            int missingLength = length - email.length();
            int index = email.indexOf('@');
            email = email.substring(0, index) + "x".repeat(missingLength) + email.substring(index);
        }
        return email;
    }

    @And("the user fills in an email {string} with local port of 64 characters")
    @And("the user fills in an email {string} with local port of 65 characters")
    public void theUserFillsInAnEmailWithLocalPortOf64Characters(String email) {
        email = generateUniqueEmail(email);
        this.email.sendKeys(email);
    }

    @And("the user fills in an email {string} with domain of 63 characters")
    @And("the user fills in an email {string} with domain of 64 characters")
    public void theUserFillsInAnEmailWithDomainOf64Characters(String email) {
        email = generateUniqueEmail(email);
        this.email.sendKeys(email);
    }

    @And("the user fills in an email {string} with top-level domain of 63 characters")
    @And("the user fills in an email {string} with top-level domain of 64 characters")
    public void theUserFillsInAnEmailWithTopLevelDomainOf64Characters(String email) {
        email = generateUniqueEmail(email);
        this.email.sendKeys(email);
    }
}
