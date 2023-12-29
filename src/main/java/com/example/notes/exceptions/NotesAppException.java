package com.example.notes.exceptions;

import java.util.HashMap;
import java.util.Map;

public class NotesAppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> additionalInfo = new HashMap<>();

    public NotesAppException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public NotesAppException(String message,ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotesAppException(String message, Throwable cause,ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public NotesAppException(Throwable cause,ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected NotesAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder("ErrorCode: " + this.errorCode + " " + super.getMessage());
        if(!additionalInfo.isEmpty()){
            for (Map.Entry<String, Object> entry : additionalInfo.entrySet()) {
                message.append("\n").append(entry.getKey()).append(" : ").append(entry.getValue());
            }
        }
        return message.toString();
    }

    public NotesAppException set(String key, Object value){
        additionalInfo.put(key, value);
        return this;
    }

    public Map<String, Object> getAdditionalInfo(){
        return additionalInfo;
    }
}