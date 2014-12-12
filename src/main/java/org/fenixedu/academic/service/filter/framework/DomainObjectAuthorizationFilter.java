/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14/Nov/2003
 * 
 */
package org.fenixedu.academic.service.filter.framework;

import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.filter.AuthorizationByRoleFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public abstract class DomainObjectAuthorizationFilter extends AuthorizationByRoleFilter {

    private static final Logger logger = LoggerFactory.getLogger(DomainObjectAuthorizationFilter.class);

    @Override
    abstract protected RoleType getRoleType();

    public void execute(String externalId) throws NotAuthorizedException {
        try {
            User id = Authenticate.getUser();

            /*
             * note: if it is neither an Integer nor an InfoObject representing
             * the object to be modified, it is supposed to throw a
             * RuntimeException to be caught and encapsulated in a
             * NotAuthorizedException
             */

            boolean isNew = externalId == null;

            if (((id != null && !getRoleType().isMember(id.getPerson().getUser()))) || (id == null)
                    || ((!isNew) && (!verifyCondition(id, externalId)))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean verifyCondition(User id, String objectId);
}