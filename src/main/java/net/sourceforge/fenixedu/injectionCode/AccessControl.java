/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.DomainObject;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 17:31:53,23/Nov/2005
 * @version $Id$
 */
public class AccessControl {

    static public IUserView getUserView() {
        return UserView.getUser();
    }

    static public Person getPerson() {
        final IUserView userView = getUserView();
        return userView == null ? null : userView.getPerson();
    }

    static public boolean hasPerson() {
        return getPerson() != null;
    }

    static public void check(DomainObject c, AccessControlPredicate<DomainObject> predicate) {
        Person requester = AccessControl.getPerson();
        boolean result = false;

        result |= (predicate != null && predicate.evaluate(c));

        if (!result) {
            StringBuilder message = new StringBuilder();
            message.append("User ").append(requester == null ? "" : requester.getUsername())
                    .append(" tried to execute access content instance number").append(c.getIdInternal());
            message.append("but he/she is not authorized to do so");

            throw new IllegalDataAccessException(message.toString(), requester);
        }
    }

    static public void check(Object c, AccessControlPredicate<Object> predicate) {
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

    static public void check(AccessControlPredicate<Object> predicate) {
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
