@FirstName @E2E @Positive
Feature: Successful registration with valid first name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: Registration successful when first name is at least 1 character long
    When the user fills in the registration form with the following details:
      | First Name       | N                |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then an API request is made to create a user


  Scenario Outline: Registration successful when first name contains only alphabetic characters
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then an API request is made to create a user

    Examples:
      | first_name |
      | Nick       |
      | nick       |
      | NICK       |


  Scenario: Registration successful when first name contains only numeric characters
    When the user fills in the registration form with the following details:
      | First Name       | 12345            |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then an API request is made to create a user


  Scenario: Registration successful when first name contains only special characters
    When the user fills in the registration form with the following details:
      | First Name       | !@#              |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then an API request is made to create a user


  Scenario: Registration successful when first name contains a combination of alphabetic, numeric, and special characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick123!@#       |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then an API request is made to create a user
