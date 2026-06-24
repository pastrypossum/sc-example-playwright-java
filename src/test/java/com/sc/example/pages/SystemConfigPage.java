package com.sc.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.sc.example.domain.ServiceDeploymentRequest;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

public class SystemConfigPage {

    private final Page page;
    private final Locator serviceName;
    private final Locator priority;
    private final Locator region;
    private final Locator alerts;
    private final Locator continueButton;

    public SystemConfigPage(Page page) {
        this.page = page;
        this.serviceName = page.getByPlaceholder("e.g. payments-api");
        this.region = page.getByRole(AriaRole.COMBOBOX);
        this.priority = page.getByRole(AriaRole.SLIDER);
        this.alerts = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setPressed(true));
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next →"));
    }

    @Step("Fill in system configuration details")
    public SystemConfigPage fillDetails(ServiceDeploymentRequest deploy) {

        serviceName.fill(deploy.getServiceName());
        region.selectOption(deploy.getRegion());
        priority.fill(deploy.getPriority());
        if(!deploy.isAlertsEnabled())
            alerts.click();

        return this;
    }

    public void selectContinue() {
        page.waitForRequest("**/validate-step", () -> {
            continueButton.click();
        });
    }

    public Request selectContinueWithRequest() {
        Request request = page.waitForRequest("**/validate-step", () -> {
            continueButton.click();
        });
        return request;
    }

    public Response selectContinueWithResponse() {
        Response response = page.waitForResponse("**/validate-step*", () -> {
            continueButton.click();
        });
        return response;
    }
}