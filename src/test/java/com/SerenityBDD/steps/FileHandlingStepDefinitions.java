package com.SerenityBDD.steps;

import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.io.File;
public class FileHandlingStepDefinitions {
    @Then("I should see {string} file is downloaded")
    public void iShouldSeeFileIsDownloaded(String fileName) {
        String repositoryPath = System.getProperty("user.dir").replace("\\", "\\\\");
        String[] pathBreakdown = repositoryPath.split("\\\\");
        String basePath = String.join("\\\\", pathBreakdown[0], pathBreakdown[2], pathBreakdown[4], "Downloads");

        File f = new File(basePath + "\\" + fileName);

        Assert.assertTrue(f.exists());
    }


}

