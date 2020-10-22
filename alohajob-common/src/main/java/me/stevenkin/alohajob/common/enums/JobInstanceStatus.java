package me.stevenkin.alohajob.common.enums;

import lombok.Getter;

public enum JobInstanceStatus {
    NEW(0, "new"),
    ASSIGNED(1, "assigned"),
    DISTRIBUTED(2, "distributed"),
    SUCCESS(3, "success"),
    FAIL(4, "fail"),
    CALLBACKFINISH(5, "callback finish");
    @Getter
    private Integer code;
    @Getter
    private String message;

    JobInstanceStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static JobInstanceStatus of(Integer code) {
        for (JobInstanceStatus status : values()) {
            if (status.code.equals(code))
                return status;
        }
        throw new IllegalArgumentException();
    }

    public static boolean isFinish(JobInstanceStatus status) {
        return status == SUCCESS || status == FAIL;
    }

    public static boolean isCallbackFinish(JobInstanceStatus status) {
        return status == CALLBACKFINISH;
    }
}
