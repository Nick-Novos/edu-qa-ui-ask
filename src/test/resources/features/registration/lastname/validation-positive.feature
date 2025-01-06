@LastName @Validation @Positive
Feature: No warning validation message displayed for valid last name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: No warning message when the last name is at least 1 character long
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | N                |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "Last Name" field
    And the user clicks the "Register" button
    And the user is redirected to the successful registration page


  Scenario Outline: No warning message when last name contains only alphabetic characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | <last_name>      |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "Last Name" field
    And the user clicks the "Register" button
    And the user is redirected to the successful registration page

    Examples:
      | last_name |
      | Novos     |
      | novos     |
      | NOVOS     |


  Scenario: No warning message when last name contains only numeric characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | 12345            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "Last Name" field
    And the user clicks the "Register" button
    And the user is redirected to the successful registration page


  Scenario: No warning message when last name contains only special characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | !@#              |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "Last Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page


  Scenario: No warning message when last name contains a combination of alphabetic, numeric, and special characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | Novos123!@#      |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    Then no warning messages should be displayed next to the "Last Name" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page
