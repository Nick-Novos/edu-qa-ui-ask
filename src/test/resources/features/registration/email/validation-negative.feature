@Email @Validation @Negative
Feature: Warning validation message displayed for invalid email

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: Display validation message when email is empty
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user clicks the "Register" button
    Then a warning message "This field is required" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"


  @IgnoreQuotes
  Scenario Outline: Display validation message when email contains empty spaces
    When the user fills in the registration form with the following details:
      | First Name       | Nick    |
      | Last Name        | Novos   |
      | Email            | <email> |
      | Group Code       | ASK     |
      | Password         | ABC123  |
      | Confirm Password | ABC123  |
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email               |
      | " "                 |
      | "      "            |
      | "nick %s@novos.com" |
      | " nick%s@novos.com" |
      | "nick%s@novos.com " |


  Scenario Outline: Display validation message when email contains wrong patter
    When the user fills in the registration form with the following details:
      | First Name       | Nick    |
      | Last Name        | Novos   |
      | Email            | <email> |
      | Group Code       | ASK     |
      | Password         | ABC123  |
      | Confirm Password | ABC123  |
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email             |
      | nick%s@@novos.com |
      | nick%s@novos..com |
      | nick%snovos.com   |
      | nick%s@novoscom   |
      | nick%s.novos@com  |
      | nick%snovoscom    |
      | @novos%s.com      |
      | nick%s@novos.     |
      | nick%s@.com       |


  @EdgeCase
  Scenario Outline: Display validation message when email contains 129 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" containing 129 characters
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email                                                                                                    |
      | nick%s@novossssssssssssssssssssssssssssssssssssssssssss.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm |


  @EdgeCase
  Scenario Outline: Display validation message when email contains local port of 65 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with local port of 65 characters
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email                                                                         |
      | nickkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk@novos%s.com |


  @EdgeCase
  Scenario Outline: Display validation message when email contains domain of 64 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with domain of 64 characters
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email                                                                       |
      | nick%s@novossssssssssssssssssssssssssssssssssssssssssssssssssssssssssss.com |


  @EdgeCase
  Scenario Outline: Display validation message when email contains top-level domain of 64 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick   |
      | Last Name        | Novos  |
      | Group Code       | ASK    |
      | Password         | ABC123 |
      | Confirm Password | ABC123 |
    And the user fills in an email "<email>" with top-level domain of 64 characters
    And the user clicks the "Register" button
    Then a warning message "Should be a valid email address" should be displayed next to the "Email" field
    And no warning messages are displayed for any field other than "Email"

    Examples:
      | email                                                                         |
      | nick%s@novos.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm |
