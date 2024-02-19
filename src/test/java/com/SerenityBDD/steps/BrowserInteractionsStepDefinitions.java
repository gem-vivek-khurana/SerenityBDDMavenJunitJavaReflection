package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

public class BrowserInteractionsStepDefinitions {
    @Steps
    Perform perform;

    @When("I scroll to the bottom/END of the page")
    public void iScrollToTheBottomOfThePage() {
        perform.scrollingToTheBottomOfThePage();
    }

    @When("I scroll to the middle of the page")
    public void iScrollToTheMiddleOfThePage() {
        perform.scrollingToTheMiddleOfThePage();
    }
}

