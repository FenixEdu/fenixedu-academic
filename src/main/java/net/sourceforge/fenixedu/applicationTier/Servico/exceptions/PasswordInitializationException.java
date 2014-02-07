package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

public class PasswordInitializationException extends FenixServiceException {

    private static final long serialVersionUID = 1L;

    public PasswordInitializationException() {
        super();
    }

    public PasswordInitializationException(String message) {
        super(message);
    }

    public PasswordInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
