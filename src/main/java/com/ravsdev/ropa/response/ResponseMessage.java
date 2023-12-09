package com.ravsdev.ropa.response;

public class ResponseMessage {
    private int statusCode;
    private String message;

    public ResponseMessage(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}