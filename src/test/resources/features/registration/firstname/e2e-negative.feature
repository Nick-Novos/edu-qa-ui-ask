@FirstName @E2E @Negative
Feature: Failed registration with invalid first name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: Registration fails when first name is empty
    When the user fills in the registration form with the following details:
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user


  @IgnoreQuotes
  Scenario Outline: Registration fails when first name contains empty spaces
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user

    Examples:
      | first_name |
      | " "        |
      | "      "   |
      | "Ni ck"    |
      | " Nick"    |
      | "Nick "    |


  @EdgeCase
  Scenario Outline: Registration fails when first name contains 256 characters
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user

    Examples:
      | first_name                                                                                                                                                                                                                                                       |
      | rmvpekrfkdkoaykqqmshqhayjyqyvuixclzncbzijmtemkbibrobocejxqypbsbrcoqmqbyqpjdiwrqefsjmonlqhrcjehystrzdjrvtxbuajmccqgrkmjbooktsmoihfynspyaudxouewqexpwjczdqsicdccsutmedeforoctanhjwroqnuwbgqnidbtvcdgjkjmxdzaxlfphddwfqztknjvfbbxqedvsbseidkcclngkzkuofnpsunpaucfqs |
