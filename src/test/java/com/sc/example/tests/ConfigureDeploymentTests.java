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
        example.setAlertThreshold("1");
        example.setNotificationChannels(new ArrayList<>(Arrays.asList("Email")));
        example.setAccessRoles(new ArrayList<>(Arrays.asList("Admin")));
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
    }

    private ServiceDeploymentResponse converToDeploymentResponse(Response response) {
        Gson gson = new Gson();
        ServiceDeploymentResponse test = gson.fromJson(response.text(), ServiceDeploymentResponse.class);
        return test;
    }

    @Step("Verify the deployment job response")
    public void verifyDeploymentJobResponse(ServiceDeploymentResponse serviceDeploymentJob, ServiceDeploymentRequest defaultRequest) {

        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(serviceDeploymentJob.success())
                    .withFailMessage("Success expected true but was " + serviceDeploymentJob.success())
                    .isEqualTo(true);
            softly.assertThat(serviceDeploymentJob.deploymentId())
                    .withFailMessage("Deployment ID expected to be not null but was null")
                    .isNotNull();
            softly.assertThat(serviceDeploymentJob.serviceName())
                    .withFailMessage("Service name expected to be " + defaultRequest.getServiceName() + " but was " + serviceDeploymentJob.serviceName())
                    .isEqualTo(defaultRequest.getServiceName());
            softly.assertThat(serviceDeploymentJob.region())
                    .withFailMessage("Region expected to be " + defaultRequest.getRegion() + " but was " + serviceDeploymentJob.region())
                    .isEqualTo(defaultRequest.getRegion());
            softly.assertThat(serviceDeploymentJob.priority())
                    .withFailMessage("Priority expected to be " + defaultRequest.getPriority() + " but was " + serviceDeploymentJob.priority())
                    .isEqualTo(defaultRequest.getPriority());
            softly.assertThat(serviceDeploymentJob.alertsEnabled())
                    .withFailMessage("Alerts enabled expected to be " + defaultRequest.isAlertsEnabled() + " but was " + serviceDeploymentJob.alertsEnabled())
                    .isEqualTo(defaultRequest.isAlertsEnabled());
            softly.assertThat(serviceDeploymentJob.deploymentWindow())
                    .withFailMessage("Deployment window expected to be " + defaultRequest.getDeploymentWindow() + " but was " + serviceDeploymentJob.deploymentWindow())
                    .isEqualTo(defaultRequest.getDeploymentWindow());
            softly.assertThat(serviceDeploymentJob.alertThreshold())
                    .withFailMessage("Alert threshold expected to be " + defaultRequest.getAlertThreshold() + " but was " + serviceDeploymentJob.alertThreshold())
                    .isEqualTo(defaultRequest.getAlertThreshold());
            softly.assertThat(serviceDeploymentJob.notificationChannels())
                    .withFailMessage("Notification channels expected to be " + defaultRequest.getNotificationChannels() + " but was " + serviceDeploymentJob.notificationChannels())
                    .isEqualTo(defaultRequest.getNotificationChannels());
            softly.assertThat(serviceDeploymentJob.accessRoles())
                    .withFailMessage("Access roles expected to be " + defaultRequest.getAccessRoles() + " but was " + serviceDeploymentJob.accessRoles())
                    .isEqualTo(defaultRequest.getAccessRoles());
            softly.assertThat(serviceDeploymentJob.deploymentNotes())
                    .withFailMessage("Deployment notes expected to be " + defaultRequest.getDeploymentNotes() + " but was " + serviceDeploymentJob.deploymentNotes())
                    .isEqualTo(defaultRequest.getDeploymentNotes());
            softly.assertAll();
        });
    }
}
