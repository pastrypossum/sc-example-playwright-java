package com.sc.example.fixtures;

import com.microsoft.playwright.*;
import com.sc.example.pages.PermissionPage;
import com.sc.example.pages.ReviewPage;
import com.sc.example.pages.SchedulePage;
import com.sc.example.pages.SystemConfigPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class PlaywrightRunner {

    private static final String BASE_UI_URL = "https://screencloudsqa.lovable.app/";
    private static final String BASE_API_URL = "https://screencloudsqa.lovable.app";

    protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-testid");
        return playwright;
    });

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--start-maximised"))));

    protected APIRequestContext apiContext;
    protected BrowserContext uiContext;
    protected Page page;

    protected PermissionPage permissionPage;
    protected ReviewPage reviewPage;
    protected SchedulePage schedulePage;
    protected SystemConfigPage systemConfigPage;

    @BeforeAll
    public static void beforeAll() {}

    @BeforeEach()
    public void beforeEach() {

        uiContext = browser.get().newContext();
        page = uiContext.newPage();

        permissionPage = new PermissionPage(page);
        reviewPage = new ReviewPage(page);
        schedulePage = new SchedulePage(page);
        systemConfigPage = new SystemConfigPage(page);

        apiContext = playwright.get().request().newContext(
                        new APIRequest.NewContextOptions().setBaseURL(BASE_API_URL)
                                .setExtraHTTPHeaders(new java.util.HashMap<>() {{
                                    put("Accept", "application/json");
                                }}));

        page.navigate(BASE_UI_URL);
    }

    @AfterEach
    public void afterEach() {

        uiContext.close();
        page.close();
    }

    @AfterAll
    public static void afterAll() {

        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }
}
