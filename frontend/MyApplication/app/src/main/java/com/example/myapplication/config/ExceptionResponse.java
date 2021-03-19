package com.example.myapplication.config;

/**
 * This class stores an exception response from the server
 */
public class ExceptionResponse {
    private int httpStatusCode;
    private String error;

    public ExceptionResponse(int httpStatusCode, String error) {
        this.httpStatusCode = httpStatusCode;
        this.error = error;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}