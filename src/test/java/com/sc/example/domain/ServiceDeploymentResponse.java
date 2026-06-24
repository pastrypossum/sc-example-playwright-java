package com.sc.example.domain;

import java.util.List;

public record ServiceDeploymentResponse(
        boolean success,
        String deploymentId,
        String debug_mode,
        String serviceName,
        String region,
        String priority,
        boolean alertsEnabled,
        String deploymentWindow,
        String alertThreshold,
        List<String> notificationChannels,
        List<String> accessRoles,
        String deploymentNotes,
        String _hint
)
{}
