package com.nixan.ask.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixan.ask.pages.UserRegistrationPage;
import com.nixan.ask.support.annotations.IgnoreQuotes;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.lightbody.bmp.core.har.HarRequest;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserRegistrationSteps {

    protected final UserRegistrationPage userRegistrationPage = new UserRegistrationPage();

    private boolean ignoreQuotes;

    @Before
    public void before(Scenario scenario) {
        ignoreQuotes = scenario.getSourceTagNames().contains('@' + IgnoreQuotes.class.getSimpleName());
    }

    @Given("the user registration page is open")
    public void theUserRegistrationPageIsOpen() {
        userRegistrationPage.openPage();
    }

    @And("the registration form is blank")
    public void isRegistrationFormIsBlank() {
        assertTrue("The registration form is not displayed", userRegistrationPage.isFormDisplayed());
        assertTrue(userRegistrationPage.areAllInputFieldsDisplayed());
        assertTrue(userRegistrationPage.areAllInputFieldsEmpty());
    }

    @When("the user fills in the registration form with the following details:")
    public void theUserFillsInTheRegistrationFormWithTheFollowingDetails(Map<String, String> unmodifiedUserMap) {
        LinkedHashMap<String, String> userInfo = new LinkedHashMap<>(unmodifiedUserMap);
        if (ignoreQuotes) {
            removeQuotes(userInfo);
        }
        String email = userInfo.get("Email");
        if (isRequiredToBeUnique(email)) {
            email = uniquifyEmail(email);
            userInfo.put("Email", email);
        }
        userRegistrationPage.fillRegistrationForm(userInfo);
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

    private boolean isRequiredToBeUnique(String email) {
        return email != null && email.contains("%s");
    }

    private String uniquifyEmail(String email) {
        return email.formatted(System.currentTimeMillis());
    }

    @And("the user clicks the \"Register\" button")
    public void theUserClicksTheRegisterButton() {
        userRegistrationPage.clickRegisterButton();
    }

    @Then("no API request is made to create a user")
    public void noApiRequestIsMadeToCreateAUser() {
        HarRequest request = userRegistrationPage.getCreatingUserRequest();
        if (request != null) {
            fail("A user creation request was unexpectedly sent. Body:\n" + request.getPostData().getText());
        }
    }

    @And("an API request is made to create a user")
    public void anApiRequestIsMadeToCreateAUser() {
        HarRequest request = userRegistrationPage.getCreatingUserRequest();
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

    @Then("a warning message {string} should be displayed next to the {string} field")
    public void aWarningMessageShouldBeDisplayedNextToTheField(String message, String fieldName) {
        assertTrue("A warning message is not displayed next to the '%s' field.".formatted(fieldName),
                userRegistrationPage.isWarningMessageNextToFieldDisplayed(fieldName));
        assertEquals(message, userRegistrationPage.getWarningMessageNextToField(fieldName));
    }

    @And("there is exactly one warning message on the form")
    public void thereIsExactlyOneWarningMessageOnTheForm() {
        assertTrue(userRegistrationPage.isExactlyOneWarningMessagePresent());
    }

    @Then("no warning messages should be displayed next to the {string} field")
    public void noWarningMessagesShouldBeDisplayedNextToTheField(String fieldName) {
        assertFalse("A warning message is displayed next to the '%s' field.".formatted(fieldName),
                userRegistrationPage.isWarningMessageNextToFieldDisplayed(fieldName));
    }

    @Then("the user is redirected to the successful registration page")
    public void theUserIsRedirectedToTheSuccessfulRegistrationPage() {
        assertTrue("User was not redirected to the successful registration page",
                userRegistrationPage.isRedirectedToSuccessPage());
    }

    @And("the user fills in an email {string} containing {int} characters")
    public void theUserFillsInAnEmailContainingCharacters(String email, Integer length) {
        email = makeEmailExactLong(email, length);
        userRegistrationPage.fillEmail(email);
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
    public void theUserFillsInAnEmailWithLocalPortOfCharacters(String email) {
        email = uniquifyEmail(email);
        userRegistrationPage.fillEmail(email);
    }

    @And("the user fills in an email {string} with domain of 63 characters")
    @And("the user fills in an email {string} with domain of 64 characters")
    public void theUserFillsInAnEmailWithDomainOf64Characters(String email) {
        email = uniquifyEmail(email);
        userRegistrationPage.fillEmail(email);
    }

    @And("the user fills in an email {string} with top-level domain of 63 characters")
    @And("the user fills in an email {string} with top-level domain of 64 characters")
    public void theUserFillsInAnEmailWithTopLevelDomainOf64Characters(String email) {
        email = uniquifyEmail(email);
        userRegistrationPage.fillEmail(email);
    }
}
