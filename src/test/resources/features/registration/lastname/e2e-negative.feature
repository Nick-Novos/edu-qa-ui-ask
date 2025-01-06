@LastName @E2E @Negative
Feature: Failed registration with invalid last name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: Registration fails when last name is empty
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user


  @IgnoreQuotes
  Scenario Outline: Registration fails when last name contains empty spaces
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | <last_name>      |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user

    Examples:
      | last_name |
      | " "       |
      | "      "  |
      | "No vos"  |
      | " Novos"  |
      | "Novos "  |


  @EdgeCase
  Scenario Outline: Registration fails when last name contains 256 characters
    When the user fills in the registration form with the following details:
      | First Name       | Nick             |
      | Last Name        | <last_name>      |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then no API request is made to create a user

    Examples:
      | last_name                                                                                                                                                                                                                                                        |
      | rmvpekrfkdkoaykqqmshqhayjyqyvuixclzncbzijmtemkbibrobocejxqypbsbrcoqmqbyqpjdiwrqefsjmonlqhrcjehystrzdjrvtxbuajmccqgrkmjbooktsmoihfynspyaudxouewqexpwjczdqsicdccsutmedeforoctanhjwroqnuwbgqnidbtvcdgjkjmxdzaxlfphddwfqztknjvfbbxqedvsbseidkcclngkzkuofnpsunpaucfqs |
