/*
 * Created on 19/Nov/2003
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * 
 * @author Barbosa
 * @author Pica
 */

public class GrantTypeNotFoundException extends FenixServiceException {

    public GrantTypeNotFoundException() {
    }

    public GrantTypeNotFoundException(String message) {
        super(message);
    }

    public GrantTypeNotFoundException(Throwable cause) {
        super(cause);
    }

    public GrantTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantTypeNotFoundException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}