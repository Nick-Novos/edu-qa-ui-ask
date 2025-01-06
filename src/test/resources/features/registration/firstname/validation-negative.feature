@FirstName @Validation @Negative
Feature: Warning validation message displayed for invalid first name

  Background:
    Given the user registration page is open
    And the registration form is blank


  Scenario: Display validation message when first name is empty
    When the user fills in the registration form with the following details:
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then a warning message "This field is required" should be displayed next to the "First Name" field
    And no warning messages are displayed for any field other than "First Name"


  @IgnoreQuotes
  Scenario Outline: Display validation message when first name contains empty spaces
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then a warning message "Whitespaces are not allowed" should be displayed next to the "First Name" field
    And no warning messages are displayed for any field other than "First Name"

    Examples:
      | first_name |
      | " "        |
      | "      "   |
      | "Ni ck"    |
      | " Nick"    |
      | "Nick "    |


  @EdgeCase
  Scenario Outline: Display validation message when first name contains 256 characters
    When the user fills in the registration form with the following details:
      | First Name       | <first_name>     |
      | Last Name        | Novos            |
      | Email            | nick%s@novos.com |
      | Group Code       | ASK              |
      | Password         | ABC123           |
      | Confirm Password | ABC123           |
    And the user clicks the "Register" button
    Then a warning message "Maximum 255 characters" should be displayed next to the "First Name" field
    And no warning messages are displayed for any field other than "First Name"

    Examples:
      | first_name                                                                                                                                                                                                                                                       |
      | rmvpekrfkdkoaykqqmshqhayjyqyvuixclzncbzijmtemkbibrobocejxqypbsbrcoqmqbyqpjdiwrqefsjmonlqhrcjehystrzdjrvtxbuajmccqgrkmjbooktsmoihfynspyaudxouewqexpwjczdqsicdccsutmedeforoctanhjwroqnuwbgqnidbtvcdgjkjmxdzaxlfphddwfqztknjvfbbxqedvsbseidkcclngkzkuofnpsunpaucfqs |
