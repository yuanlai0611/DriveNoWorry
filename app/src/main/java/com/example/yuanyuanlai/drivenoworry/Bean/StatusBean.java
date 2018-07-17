package com.example.yuanyuanlai.drivenoworry.Bean;

public class StatusBean<T> {

    private T data;
    private boolean success;
    private int status;
    private String  message;

    public T getData() {
        return data;
    }

    public void setData(T mData) {
        data = mData;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean mSuccess) {
        success = mSuccess;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int mStatus) {
        status = mStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mMessage) {
        message = mMessage;
    }
}
