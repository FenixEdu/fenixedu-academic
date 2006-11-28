/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:31:53,23/Nov/2005
 * @version $Id$
 */
public class AccessControl {

    private static InheritableThreadLocal<IUserView> userView = new InheritableThreadLocal<IUserView>();

    public static IUserView getUserView() {
	return AccessControl.userView.get();
    }

    public static Person getPerson() {
	final IUserView userView = getUserView();
	return userView == null ? null : userView.getPerson();
    }

    public static void setUserView(IUserView userView) {
	AccessControl.userView.set(userView);
    }

    public static void check(DomainObject c, AccessControlPredicate<DomainObject> predicate) {
	Person requester = AccessControl.getUserView().getPerson();
	boolean result = false;

	result |= (predicate != null && predicate.evaluate(c));

	if (!result) {
	    StringBuilder message = new StringBuilder();
	    message.append("User ").append(requester.getUsername()).append(
		    " tried to execute access content instance number").append(c.getIdInternal());
	    message.append("but he/she is not authorized to do so");

	    throw new IllegalDataAccessException(message.toString(), requester);
	}
    }
}
