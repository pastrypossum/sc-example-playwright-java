package com.sc.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class ReviewPage {

    private final Page page;
    private final Locator reviewTable;


    public ReviewPage(Page page) {
        this.page = page;
        this.reviewTable= page.locator("body > div > div > div:nth-child(3)");
    }

    public List<String> readDetail(){

        return reviewTable.allInnerTexts();
    }
}