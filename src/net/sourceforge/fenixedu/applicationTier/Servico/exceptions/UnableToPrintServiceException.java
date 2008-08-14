/*
 * Created on 12/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author João Mota
 */
public class UnableToPrintServiceException extends FenixServiceException {

    /**
     *  
     */
    public UnableToPrintServiceException() {

    }

    /**
     * @param s
     */
    public UnableToPrintServiceException(String s) {
	super(s);

    }

    /**
     * @param cause
     */
    public UnableToPrintServiceException(Throwable cause) {
	super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public UnableToPrintServiceException(String message, Throwable cause) {
	super(message, cause);

    }

    public String toString() {
	String result = "[UnableToPrintServiceException\n";
	result += "message" + this.getMessage() + "\n";
	result += "cause" + this.getCause() + "\n";
	result += "]";
	return result;
    }
}