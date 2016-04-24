package com.wuzhou.tool.isbn;

public class ISBNFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ISBNFormatException() {
        super("ISBN Error ...");
    }
    public ISBNFormatException(String arg0) {
        super(arg0);
    }
}