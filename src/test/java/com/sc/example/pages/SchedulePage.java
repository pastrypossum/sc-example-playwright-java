package com.sc.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.sc.example.domain.ServiceDeploymentRequest;
import io.qameta.allure.Step;

public class SchedulePage {

    private final Page page;
    private final Locator deploymentWindow;
    private final Locator alertThreshold;
    private final Locator email;
    private final Locator slack;
    private final Locator pager;
    private final Locator webhook;
    private final Locator continueButton;
    private final Locator backButton;

    public SchedulePage(Page page) {
        this.page = page;
        this.deploymentWindow = page.locator("input[type='datetime-local']");
        this.alertThreshold = page.getByPlaceholder("Trigger alert after N consecutive failures");
        this.email = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Email"));
        this.slack = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Slack"));
        this.pager = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("PagerDuty"));
        this.webhook = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Webhook"));
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next →"));
        this.backButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("← Back"));
    }

    @Step("Fill in schedule details")
    public SchedulePage fillDetails(ServiceDeploymentRequest deploy) {

        deploymentWindow.fill(deploy.getDeploymentWindow());
        alertThreshold.fill(deploy.getAlertThreshold());
        if(!email.isChecked() && deploy.getNotificationChannels().contains("Email"))
            email.check();
        if(!slack.isChecked() && deploy.getNotificationChannels().contains("Slack"))
            slack.check();
        if(!pager.isChecked() && deploy.getNotificationChannels().contains("PagerDuty"))
            pager.check();
        if(!webhook.isChecked() && deploy.getNotificationChannels().contains("Webhook"))
            webhook.check();
        return this;
    }

    public void selectContinue() {
        page.waitForResponse("**/validate-step", () -> {
            continueButton.click();
        });
    }
}