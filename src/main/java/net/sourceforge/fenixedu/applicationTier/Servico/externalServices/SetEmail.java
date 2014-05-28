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
package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class SetEmail {

    public static class NotAuthorizedException extends FenixServiceException {
    }

    public class UserAlreadyHasEmailException extends FenixServiceException {

        private final String email;

        public UserAlreadyHasEmailException(final String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

    }

    public static class UserDoesNotExistException extends FenixServiceException {

    }

    private static final Set<String> allowedHosts = new HashSet<String>();
    private static final String password;
    static {
        final String allowedHostString = FenixConfigurationManager.getConfiguration().getEmailAdminAllowedHosts();
        if (allowedHostString != null) {
            final String[] allowedHostTokens = allowedHostString.split(",");
            for (String allowedHostToken : allowedHostTokens) {
                allowedHosts.add(allowedHostToken);
            }
        }
        password = FenixConfigurationManager.getConfiguration().getEmailAdminPassword();
    }

    public static boolean isAllowed(final String host, final String ip, final String password) {
        return SetEmail.password != null && SetEmail.password.equals(password)
                && (allowedHosts.contains(host) || allowedHosts.contains(ip));
    }

    private static void set(final String userUId, final String email) throws FenixServiceException {
        final User user = User.findByUsername(userUId);
        if (user == null) {
            throw new UserDoesNotExistException();
        }
        final Person person = user.getPerson();
        if (person == null) {
            throw new UserDoesNotExistException();
        }
        final String newEmail = StringUtils.isEmpty(email) ? null : email;
        person.setInstitutionalEmailAddressValue(newEmail);
    }

    @Atomic
    public static void run(final String host, final String ip, final String password, final String userUId, final String email)
            throws FenixServiceException {
        if (isAllowed(host, ip, password)) {
            set(userUId, email);
        } else {
            throw new NotAuthorizedException();
        }
    }

}