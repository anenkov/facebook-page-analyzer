package me.anenkov.fbpageanalyzer;

/**
 * ApplicationException
 *
 * @author anenkov
 */
public class ApplicationException extends Exception {
    /**
     * @param throwable
     */
    public ApplicationException(Throwable throwable) {
        super(throwable);
    }
}
