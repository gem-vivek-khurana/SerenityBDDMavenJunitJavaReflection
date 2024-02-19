Feature: Test Base Google Site

  @run
  Scenario: Test Google
    Given I launch the browser and open the home page
    Then I should be on the "Google Home" page of the "Google" workflow
    When I set the "Search" text field as "Test"