/*
 * Created on 31/Jul/2003, 16:48:21
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 16:48:21
 *  
 */
public class BDException extends FenixServiceException {
    private int errorType;

    /**
     * @return
     */
    public int getErrorType() {
        return this.errorType;
    }

    public BDException() {
    }

    public BDException(int errorType) {
        this.errorType = errorType;
    }

    public BDException(String s) {
        super(s);
    }

    public BDException(Throwable cause) {
        super(cause);
    }

    public BDException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[BDException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}