package com.sc.example.pages;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.sc.example.domain.ServiceDeploymentRequest;
import io.qameta.allure.Step;

public class PermissionPage {

    private final Page page;
    private final Locator admin;
    private final Locator developer;
    private final Locator viewer;
    private final Locator auditor;
    private final Locator notes;
    private final Locator confirmation;
    private final Locator continueButton;
    private final Locator backButton;

    public PermissionPage(Page page) {
        this.page = page;
        this.admin = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Admin"));
        this.developer = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Developer"));
        this.viewer = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Viewer"));
        this.auditor = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Auditor"));
        this.notes = page.getByRole(AriaRole.TEXTBOX);
        this.confirmation = page.getByRole(AriaRole.CHECKBOX).nth(4);
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Deploy Configuration"));
        this.backButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("← Back"));
    }

    @Step("Fill in permission details")
    public PermissionPage fillDetails(ServiceDeploymentRequest deploy) {

        if(!admin.isChecked() && deploy.getAccessRoles().contains("Admin"))
            admin.check();
        if(!developer.isChecked() && deploy.getAccessRoles().contains("Developer"))
            developer.check();
        if(!viewer.isChecked() && deploy.getAccessRoles().contains("Viewer"))
            viewer.check();
        if(!auditor.isChecked() && deploy.getAccessRoles().contains("Auditor"))
            auditor.check();

        notes.fill(deploy.getDeploymentNotes());

        if(!confirmation.isChecked() && deploy.isConfirmation())
            confirmation.check();

        return this;
    }

    public void selectContinue() {

        page.waitForResponse("**/submit-config", () -> {
            continueButton.isVisible();
            continueButton.click();
        });
    }

    public Response selectContinueWithResponse() {
        Response response = page.waitForResponse("**/submit-config", () -> {
            continueButton.click();
        });
        return response;
    }
}