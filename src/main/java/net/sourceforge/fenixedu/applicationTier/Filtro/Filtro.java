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
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

abstract public class Filtro {

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<RoleType> getNeededRoleTypes() {
        return null;
    }

    protected boolean containsRoleType(Collection<Role> roles) {
        final Collection<RoleType> neededRoleTypes = getNeededRoleTypes();
        if (neededRoleTypes == null || neededRoleTypes.isEmpty()) {
            return true;
        }
        if (roles != null) {
            for (final Role role : roles) {
                if (neededRoleTypes.contains(role.getRoleType())) {
                    return true;
                }
            }
        }
        return false;
    }

}
