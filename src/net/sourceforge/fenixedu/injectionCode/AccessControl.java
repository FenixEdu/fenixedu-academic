/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:31:53,23/Nov/2005
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
	    message.append("User ").append(requester.getUsername()).append(" tried to execute access content instance number")
		    .append(c.getIdInternal());
	    message.append("but he/she is not authorized to do so");

	    throw new IllegalDataAccessException(message.toString(), requester);
	}
    }
}
