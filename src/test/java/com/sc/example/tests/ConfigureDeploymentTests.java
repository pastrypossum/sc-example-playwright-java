package com.sc.example.tests;

import com.google.gson.Gson;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.sc.example.domain.ServiceDeploymentResponse;
import com.sc.example.domain.ServiceDeploymentRequest;
import com.sc.example.fixtures.PlaywrightRunner;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Feature("Configure deployment")
@DisplayName("Configure a scheduled deployment job")
public class ConfigureDeploymentTests extends PlaywrightRunner {

    @Test
    @DisplayName("Configure service deployment")
    public void configureServiceDeployment() {

        ServiceDeploymentRequest example = new ServiceDeploymentRequest();
        example.setServiceName("PaymentService");
        example.setRegion("us-east-1");
        example.setPriority("1");
        example.setAlertsEnabled(true);
        example.setDeploymentWindow("2026-06-28T02:00");
        example.setAlertThreshold("3");
        example.setNotificationChannels(new ArrayList<>(Arrays.asList("Email","Slack")));
        example.setAccessRoles(new ArrayList<>(Arrays.asList("Admin","Developer")));
        example.setDeploymentNotes("This is an example test");
        example.setConfirmation(true);

        systemConfigPage.fillDetails(example).selectContinue();
        schedulePage.fillDetails(example).selectContinue();
        Response response = permissionPage.fillDetails(example).selectContinueWithResponse();

        Assertions.assertThat(response.status())
                .withFailMessage("Expected status code of 200 but got " + response.status() + " and " + response.text())
                .isEqualTo(200);

        ServiceDeploymentResponse result = converToDeploymentResponse(response);
        verifyDeploymentJobResponse(result, example);

        PlaywrightAssertions.assertThat(page.getByText(result.deploymentId())).isVisible();
        PlaywrightAssertions.assertThat(page.getByText(example.getServiceName())).isVisible();
        PlaywrightAssertions.assertThat(page.getByText(example.getRegion())).isVisible();
//        Value not unique enough
//        strict mode violation: getByText("1") resolved to 4 elements:
//        PlaywrightAssertions.assertThat(page.getByText(result.priority())).isVisible();
//        PlaywrightAssertions.assertThat(page.getByText(result.alertThreshold())).isVisible();
        PlaywrightAssertions.assertThat(page.getByText(example.getDeploymentNotes())).isVisible();
        PlaywrightAssertions.assertThat(page.getByText(
                example.isAlertsEnabled() ? "Yes" : "No")).isVisible();

        example.getNotificationChannels().forEach(channel -> {
            PlaywrightAssertions.assertThat(page.getByText(channel)).isVisible();
        });
        example.getAccessRoles().forEach(role -> {
            PlaywrightAssertions.assertThat(page.getByText(role)).isVisible();
        });
    }

    private ServiceDeploymentResponse converToDeploymentResponse(Response response) {
        Gson gson = new Gson();
        ServiceDeploymentResponse test = gson.fromJson(response.text(), ServiceDeploymentResponse.class);
        return test;
    }

    @Step("Verify the deployment job response")
    public void verifyDeploymentJobResponse(ServiceDeploymentResponse actualJob, ServiceDeploymentRequest expectedJob) {

        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(actualJob.success())
                    .withFailMessage("Success expected true but was " + actualJob.success())
                    .isEqualTo(true);
            softly.assertThat(actualJob.deploymentId())
                    .withFailMessage("Deployment ID expected to be not null but was null")
                    .isNotNull();
            softly.assertThat(actualJob.serviceName())
                    .withFailMessage("Service name expected to be " + expectedJob.getServiceName() + " but was " + actualJob.serviceName())
                    .isEqualTo(expectedJob.getServiceName());
            softly.assertThat(actualJob.region())
                    .withFailMessage("Region expected to be " + expectedJob.getRegion() + " but was " + actualJob.region())
                    .isEqualTo(expectedJob.getRegion());
            softly.assertThat(actualJob.priority())
                    .withFailMessage("Priority expected to be " + expectedJob.getPriority() + " but was " + actualJob.priority())
                    .isEqualTo(expectedJob.getPriority());
            softly.assertThat(actualJob.alertsEnabled())
                    .withFailMessage("Alerts enabled expected to be " + expectedJob.isAlertsEnabled() + " but was " + actualJob.alertsEnabled())
                    .isEqualTo(expectedJob.isAlertsEnabled());
            softly.assertThat(actualJob.deploymentWindow())
                    .withFailMessage("Deployment window expected to be " + expectedJob.getDeploymentWindow() + " but was " + actualJob.deploymentWindow())
                    .isEqualTo(expectedJob.getDeploymentWindow());
            softly.assertThat(actualJob.alertThreshold())
                    .withFailMessage("Alert threshold expected to be " + expectedJob.getAlertThreshold() + " but was " + actualJob.alertThreshold())
                    .isEqualTo(expectedJob.getAlertThreshold());
            softly.assertThat(actualJob.notificationChannels())
                    .withFailMessage("Notification channels expected to be " + expectedJob.getNotificationChannels() + " but was " + actualJob.notificationChannels())
                    .isEqualTo(expectedJob.getNotificationChannels());
            softly.assertThat(actualJob.accessRoles())
                    .withFailMessage("Access roles expected to be " + expectedJob.getAccessRoles() + " but was " + actualJob.accessRoles())
                    .isEqualTo(expectedJob.getAccessRoles());
            softly.assertThat(actualJob.deploymentNotes())
                    .withFailMessage("Deployment notes expected to be " + expectedJob.getDeploymentNotes() + " but was " + actualJob.deploymentNotes())
                    .isEqualTo(expectedJob.getDeploymentNotes());
            softly.assertAll();
        });
    }
}
