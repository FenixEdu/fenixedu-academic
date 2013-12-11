/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

import pt.ist.bennu.core.domain.User;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidPasswordServiceException extends FenixServiceException {

    private User userView;

    /**
     *  
     */
    public InvalidPasswordServiceException() {
        super();

    }

    /**
     * @param s
     */
    public InvalidPasswordServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidPasswordServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidPasswordServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public InvalidPasswordServiceException(String s, User userView) {
        super(s);
        setUserView(userView);
    }

    private void setUserView(User userView) {
        this.userView = userView;
    }

    public User getUserView() {
        return this.userView;
    }

}