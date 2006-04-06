package net.sourceforge.fenixedu.integrationTier.dspace;

public class DspaceClientException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DspaceClientException() {
        super();
    }

    public DspaceClientException(String message) {
        super(message);
    }

    public DspaceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DspaceClientException(Throwable cause) {
        super(cause);
    }

}
