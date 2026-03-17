package com.mdkj.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    /**
     * 是否成功
     */
    private Boolean flag;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回数据
     */
    private Object data;

    public static R ok() {
        R r = new R();
        r.flag = true;
        r.code = 200;
        return r;
    }
    public static R ok(String message) {
        R r = new R();
        r.flag = true;
        r.code = 200;
        r.msg = message;
        return r;
    }

    public static R ok(String message, Object data) {
        R r = ok(message);
        r.data = data;
        return r;
    }

    public static R fail(String message) {
        R r = new R();
        r.flag = false;
        r.code = 500;
        r.msg = message;
        return r;
    }
    public static R fail(String message,Integer code) {
        R r = new R();
        r.flag = false;
        r.code = code;
        r.msg = message;
        return r;
    }

}
