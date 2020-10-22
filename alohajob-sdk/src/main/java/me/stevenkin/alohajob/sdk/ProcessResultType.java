package me.stevenkin.alohajob.sdk;

import lombok.Getter;

public enum ProcessResultType {
    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    REDO(2, "redo");
    @Getter
    private Integer code;
    @Getter
    private String message;

    ProcessResultType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ProcessResultType of(Integer code) {
        for (ProcessResultType status : values()) {
            if (status.code.equals(code))
                return status;
        }
        throw new IllegalArgumentException();
    }
}
