package com.sc.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class ServiceDeploymentRequest {

    private String serviceName;
    private String region;
    private String priority;
    private boolean alertsEnabled;
    private String deploymentWindow;
    private String alertThreshold;
    private List<String> notificationChannels;
    private List<String> accessRoles;
    private String deploymentNotes;
    private boolean confirmation;
}
