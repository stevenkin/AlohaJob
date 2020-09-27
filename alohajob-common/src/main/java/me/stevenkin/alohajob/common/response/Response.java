package me.stevenkin.alohajob.common.response;


import lombok.Data;

@Data
public class Response<T> {
    private boolean success;
    // 数据
    private T data;
    // 错误信息（success为 false 时存在）
    private String message;

    public static <T> Response<T> success(T data) {
        Response<T> r = new Response<>();
        r.success = true;
        r.data = data;
        return r;
    }

    public static <T> Response<T> failed(String message) {
        Response<T> r = new Response<>();
        r.success = false;
        r.message = message;
        return r;
    }
}
