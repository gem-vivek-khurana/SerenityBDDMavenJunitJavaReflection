package com.SerenityBDD.support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.Getter;

import java.util.ArrayList;

public class BrowserObjectOperations {
    public Page createPlaywrightPageInstance(SupportedBrowsers supportedBrowser) {
        String browserName = supportedBrowser.getBrowser();
        Browser browser;
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
        switch (browserName) {
            case "firefox" -> browser = Playwright.create().firefox().launch(launchOptions.setHeadless(false));
            case "chromium" ->
                    browser = Playwright.create().chromium().launch(launchOptions.setHeadless(false));
            case "chrome" ->
                    browser = Playwright.create().chromium().launch(launchOptions.setChannel("chrome").setHeadless(false));
            case "edge" ->
                    browser = Playwright.create().chromium().launch(launchOptions.setChannel("msedge").setHeadless(false));
            default -> throw new RuntimeException("Browser not supported at the moment");
        }
        return browser.newPage();
    }

    @Getter
    public enum SupportedBrowsers {
        FIREFOX("firefox"), CHROME("chrome"), CHROMIUM("chromium"), EDGE("edge");
        private final String browser;

        SupportedBrowsers(String browser) {
            this.browser = browser;
        }

    }
}
