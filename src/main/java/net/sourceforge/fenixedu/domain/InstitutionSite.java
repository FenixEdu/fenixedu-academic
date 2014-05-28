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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class InstitutionSite extends InstitutionSite_Base {

    protected InstitutionSite() {
        super();
    }

    protected InstitutionSite(Unit unit) {
        super();

        setUnit(unit);
    }

    @Override
    public Group getOwner() {
        RoleType roleType = RoleType.MANAGER;
        return UserGroup.of(Person.convertToUsers(getManagers())).or(RoleGroup.get(roleType));
    }

    /**
     * Initializes the site for the institution unit. If the site was already
     * initialized nothing is done.
     * 
     * @return the site associated with the institution unit
     */
    public static UnitSite initialize() {
        Unit unit = Bennu.getInstance().getInstitutionUnit();

        if (unit.hasSite()) {
            return unit.getSite();
        } else {
            return new InstitutionSite(unit);
        }
    }

}
