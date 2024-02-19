package com.SerenityBDD.navigation;

import com.SerenityBDD.pages.gemfin.GemfinLogin;
import com.SerenityBDD.pages.google.GoogleHome;
import com.microsoft.playwright.Page;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.DefaultUrl;

public class NavigateTo {
    Page page = Serenity.sessionVariableCalled("page");

    public void theGemfinLoginPage() {
        page.navigate(GemfinLogin.class.getAnnotation(DefaultUrl.class).value());
    }

    public void theHomePage() {
        page.navigate(GoogleHome.class.getAnnotation(DefaultUrl.class).value());
    }

}
