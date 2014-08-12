package com.tellnow.api.exceptions;

@SuppressWarnings("serial")
public class DuplicateAnswerException extends Exception {
    private String message;
    private int status;

    /**
     *
     * @param message Message to show the user
     * @param status One of HttpServletResponse
     */
    public DuplicateAnswerException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
