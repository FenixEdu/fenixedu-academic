package ServidorAplicacao.utils.exceptions;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsCommandConfigurationException extends FenixUtilException {

    /**
     *  
     */
    public SmsCommandConfigurationException() {
        super();
    }

    /**
     * 
     * @param message
     */
    public SmsCommandConfigurationException(String message) {
        super(message);
    }

    /**
     * @param arg0
     */
    public SmsCommandConfigurationException(Throwable throwable) {
        super(throwable);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SmsCommandConfigurationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}