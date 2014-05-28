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
/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author jpvl
 */
public abstract class AuthorizationByRoleFilter extends Filtro {
    /**
     * This method returns the role that we want to authorize.
     * 
     * @return RoleType
     */
    abstract protected RoleType getRoleType();

    public void execute() throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        if (((userView != null && userView.getPerson().getPersonRolesSet() != null && !userView.getPerson()
                .hasRole(getRoleType()))) || (userView == null) || (userView.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }

    }
}