package com.SerenityBDD.pages.google;

import com.SerenityBDD.support.PageObjectOperations;
import com.microsoft.playwright.Page;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("https://www.google.com")
public class GoogleHome extends PageObjectOperations {
    Page page;
    public GoogleHome(Page page) {
        this.page = page;
    }
    public static final String PAGE_LOADED = "img[src*='/images/branding/googlelogo/']";
    public static final String SEARCH = "[aria-label='Search']";
}
