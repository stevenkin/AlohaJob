package me.stevenkin.alohajob.node.core;

import me.stevenkin.alohajob.common.dto.*;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.stereotype.Component;

@Component
public class SchedulerServerClient {
    public UserDto getUser(Long id) {
        return null;
    }

    public AppDto getApp(Long id) {
        return null;
    }

    public JobDto getJob(Long id) {
        return null;
    }

    public JobTriggerDto getJobTrigger(String id) {
        return null;
    }

    public JobInstanceDto getRootJobInstance(String triggerId) {
        return null;
    }

    public JobInstanceDto getJobInstance(String instanceId) {
        return null;
    }

    public JobInstanceResultDto getJobInstanceResult(String instanceId) {
        return null;
    }

    public JobInstanceDto pullJobInstance(String triggerId, String address) {
        return null;
    }

    public boolean finishJobInstance(String instanceId, ProcessResult result) {
        return false;
    }

    public boolean checkJobInstanceIsExist(String instanceId) {
        return false;
    }

    public boolean checkJobInstanceIsExist(String instanceId, String subInstanceName) {
        return false;
    }

    public String newJobInstance(String triggerId, String parentInstanceId, String subInstanceName, String instanceParam) {
        return null;
    }

    public String newJobInstanceIfAbsent(String triggerId, String parentInstanceId, String subInstanceName, String instanceParam) {
        return null;
    }


}
