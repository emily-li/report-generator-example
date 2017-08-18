package com.liemily.reportgeneration.exceptions;

/**
 * Created by Emily Li on 18/08/2017.
 */
public abstract class ReportGenerationException extends Exception {
    ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
