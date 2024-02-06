package com.SerenityBDD.pages;

import com.SerenityBDD.support.PageObjectOperations;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("https://www.google.com")
public class GoogleHome extends PageObjectOperations {
    public static final By PAGE_LOADED = By.cssSelector("img[src*='/images/branding/googlelogo/']");
}
