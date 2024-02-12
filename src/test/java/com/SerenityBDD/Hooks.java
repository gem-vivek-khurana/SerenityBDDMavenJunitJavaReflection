package com.SerenityBDD;

import com.SerenityBDD.support.BrowserObjectOperations;
import com.microsoft.playwright.Page;
import io.cucumber.java.*;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;

public class Hooks extends PageObject {
    static BrowserObjectOperations browserObjectOperations = new BrowserObjectOperations();
    static Page page = null;

    @Before
    public static void before(Scenario s) {
        page = browserObjectOperations.createPlaywrightPageInstance(BrowserObjectOperations.SupportedBrowsers.CHROME);
        Serenity.setSessionVariable("page").to(page);
    }

    @After
    public static void after(Scenario s) {
        if (page != null) page.close();
    }
}
