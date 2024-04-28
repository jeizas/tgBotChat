package com.jeizas.biz.dto;

import com.jeizas.common.constant.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Response.
 *
 * @param <T> the type parameter
 */
@Data
public class Response<T> implements Serializable {


    private T data;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;

    /**
     * Instantiates a new Response.
     */
    public Response() {
    }

    /**
     * Instantiates a new Response.
     *
     * @param data the data
     * @param code the code
     * @param msg  the msg
     */
    public Response(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    /**
     * Success response.
     *
     * @param <T> the type parameter
     * @return the response
     */
    public static <T> Response<T> success() {
        return new Response<T>(null, ResponseCode.SUCCESS, "ok");
    }

    /**
     * Success response.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the response
     */
    public static <T> Response<T> success(T data) {
        return new Response<T>(data, ResponseCode.SUCCESS, "ok");
    }

    /**
     * Error response.
     *
     * @param <T> the type parameter
     * @param msg the msg
     * @return the response
     */
    public static <T> Response<T> error(String msg) {
        return new Response<T>(null, ResponseCode.FAIL, msg);
    }
}

