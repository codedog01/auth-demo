package com.lengao.auth.utils;

import java.io.Serializable;

/**
 * @Author: LengAo
 * @Date: 2021/08/31/14:09
 */
public class Result<R> implements Serializable {

    private boolean success;
    private int code;
    private String msg;
    private R data;

    private static String successMsg = "操作成功.";
    private static String failMsg = "操作失败.";

    public static <R> Result<R> ofSuccess() {
        return new Result<R>().setSuccess(true).setMsg(successMsg).setData(null);
    }

    public static <R> Result<R> ofSuccess(R data) {
        return new Result<R>().setSuccess(true).setMsg(successMsg).setData(data);
    }

    public static <R> Result<R> ofSuccessMsg(String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg);
    }

    public static <R> Result<R> ofSuccessWithMsg(R data, String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg).setData(data);
    }

    public static <R> Result<R> ofSuccess(int code, String msg) {
        return new Result<R>().setSuccess(true).setMsg(msg).setData(null).setCode(code);
    }

    public static <R> Result<R> ofFail() {
        return new Result<R>().setSuccess(false).setMsg(failMsg);
    }

    public static <R> Result<R> ofFail(int code, String msg) {
        Result<R> result = new Result<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <R> Result<R> ofFail(R data) {
        return new Result<R>().setSuccess(false).setMsg(failMsg).setData(data);
    }

    public static <R> Result<R> ofFail(String msg) {
        Result<R> result = new Result<>();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static <R> Result<R> ofFail(R data, String msg) {
        Result<R> result = new Result<>();
        result.setData(data);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static <R> Result<R> ofFail(int code, String msg, R data) {
        return new Result<R>().setSuccess(false).setMsg(msg).setCode(code).setData(data);
    }


    public static <R> Result<R> ofThrowable(int code, Throwable throwable) {
        Result<R> result = new Result<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result<R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public R getData() {
        return data;
    }

    public Result<R> setData(R data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" + "success=" + success + ", code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}