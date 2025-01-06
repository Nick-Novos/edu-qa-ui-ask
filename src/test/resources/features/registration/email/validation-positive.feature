@Email @Validation @Positive
Feature: No warning validation message displayed for valid email

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario Outline: No warning message when email contains only alphabetic characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick    |
      | Last Name        | Novos   |
      | Email            | <email> |
      | Group Code       | ASK     |
      | Password         | ABC123  |
      | Confirm Password | ABC123  |
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email            |
      | nick%s@novos.com |


  Scenario Outline: No warning message when email contains only numeric characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick    |
      | Last Name        | Novos   |
      | Email            | <email> |
      | Group Code       | ASK     |
      | Password         | ABC123  |
      | Confirm Password | ABC123  |
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email            |
      | nick%s@novos.com |


  @EdgeCase
  Scenario Outline: No warning message when email contains 128 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" containing 128 characters
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email                                                                                                    |
      | nick%s@novossssssssssssssssssssssssssssssssssssssssssss.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm |


  @EdgeCase
  Scenario Outline: No warning message when email contains local port of 64 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with local port of 64 characters
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email                                                                        |
      | nickkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk@novos%s.com |


  @EdgeCase
  Scenario Outline: No warning message when email contains domain of 64 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with domain of 63 characters
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email                                                                      |
      | nick%s@novosssssssssssssssssssssssssssssssssssssssssssssssssssssssssss.com |


  @EdgeCase
  Scenario Outline: No warning message when email contains top-level domain of 63 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with top-level domain of 63 characters
    Then no warning messages should be displayed next to the "Email" field
    When the user clicks the "Register" button
    Then the user is redirected to the successful registration page

    Examples:
      | email                                                                        |
      | nick%s@novos.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm |
