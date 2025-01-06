@FirstName @Validation @Positive
Feature: No warning validation message displayed for valid first name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: No warning message when first name is at least 1 character long
    When the user fills in the registration form with the following details:
      | First Name       | N                |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "First Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page


  Scenario Outline: No warning message when first name contains only alphabetic characters
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "First Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | first_name |
      | Nick       |
      | nick       |
      | NICK       |


  Scenario: No warning message when first name contains only numeric characters
    When the user fills in the registration form with the following details:
      | First Name       | 12345            |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "First Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page


  Scenario: No warning message when first name contains only special characters
    When the user fills in the registration form with the following details:
      | First Name       | !@#              |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "First Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page


  Scenario: No warning message when first name contains a combination of alphabetic, numeric, and special characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick123!@#       |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "First Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page
