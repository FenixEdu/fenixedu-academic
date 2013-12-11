/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixframework.DomainObject;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 17:31:53,23/Nov/2005
 * @version $Id$
 */
public class AccessControl {

    static public Person getPerson() {
        final User userView = Authenticate.getUser();
        return userView == null ? null : userView.getPerson();
    }

    public static <T extends DomainObject> void check(T c, AccessControlPredicate<T> predicate) {
        Person requester = AccessControl.getPerson();
        boolean result = false;

        result |= (predicate != null && predicate.evaluate(c));

        if (!result) {
            StringBuilder message = new StringBuilder();
            message.append("User ").append(requester == null ? "" : requester.getUsername())
                    .append(" tried to execute access content instance number").append(c.getExternalId());
            message.append("but he/she is not authorized to do so");

            throw new IllegalDataAccessException(message.toString(), requester);
        }
    }

    public static <T> void check(T c, AccessControlPredicate<T> predicate) {
        Person requester = AccessControl.getPerson();
        boolean result = false;

        result |= (predicate != null && predicate.evaluate(c));

        if (!result) {
            StringBuilder message = new StringBuilder();
            message.append("User ").append(requester.getUsername()).append(" tried to execute access content instance number")
                    .append(c.toString());
            message.append("but he/she is not authorized to do so");

            throw new IllegalDataAccessException(message.toString(), requester);
        }
    }

    static public void check(AccessControlPredicate<?> predicate) {
        Person requester = AccessControl.getPerson();
        boolean result = false;

        result |= (predicate != null && predicate.evaluate(null));

        if (!result) {
            StringBuilder message = new StringBuilder();
            final String username = requester == null ? "<nobody>" : requester.getUsername();
            message.append("User ");
            message.append(username);
            message.append(" tried to execute method but he/she is not authorized to do so");
            throw new IllegalDataAccessException(message.toString(), requester);
        }
    }

}
