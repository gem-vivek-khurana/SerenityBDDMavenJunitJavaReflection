package com.SerenityBDD.navigation;

import com.SerenityBDD.pages.GoogleHome;

public class NavigateTo {
    final GoogleHome googleHome = new GoogleHome();

    public void theHomePage() {
        googleHome.open();
    }

}
