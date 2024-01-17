package com.example.notes.exceptions;

public class LabelException extends RuntimeException{

    public LabelException(){
        super();
    }

    public  LabelException(String exception){
        super(exception);
    }
}
