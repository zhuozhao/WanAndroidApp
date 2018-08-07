package com.zz.wanandroid.bean;

/**
 * @author zhaozhuo
 * @date 2018/4/19 16:11
 */
public class Result<T> {

    private T data;
    private int errorCode;
    private String errorMsg;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
