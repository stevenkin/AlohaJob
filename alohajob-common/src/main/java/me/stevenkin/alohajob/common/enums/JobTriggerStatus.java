package me.stevenkin.alohajob.common.enums;

import lombok.Getter;

public enum JobTriggerStatus {
    RUNNING(1, "running"),
    SUCCESS(3, "success"),
    FAIL(4, "fail"),
    TIMEOUT(5, "timeout"),
    TERMINATE(6, "terminate");
    @Getter
    private Integer code;
    @Getter
    private String message;

    JobTriggerStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static JobTriggerStatus of(Integer code) {
        for (JobTriggerStatus status : values()) {
            if (status.code.equals(code))
                return status;
        }
        throw new IllegalArgumentException();
    }

    public static boolean isFinish(JobTriggerStatus status) {
        return status != RUNNING;
    }
}
