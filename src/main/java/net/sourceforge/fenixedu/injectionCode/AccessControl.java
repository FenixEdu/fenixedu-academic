/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

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
