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

public class GrantOrientationTeacherNotFoundException extends FenixServiceException {

    public GrantOrientationTeacherNotFoundException() {
    }

    public GrantOrientationTeacherNotFoundException(String message) {
        super(message);
    }

    public GrantOrientationTeacherNotFoundException(Throwable cause) {
        super(cause);
    }

    public GrantOrientationTeacherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantOrientationTeacherNotFoundException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}