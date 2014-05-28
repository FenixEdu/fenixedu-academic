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

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DepartmentSite extends DepartmentSite_Base {

    public DepartmentSite(DepartmentUnit unit) {
        super();

        if (unit.hasSite()) {
            throw new DomainException("site.department.unit.already.has.site");
        }

        setUnit(unit);
    }

    public DepartmentSite(Department department) {
        this(department.getDepartmentUnit());
    }

    public Department getDepartment() {
        return getUnit().getDepartment();
    }

    @Override
    public Group getOwner() {
        return RoleGroup.get(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE).or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = super.getContextualPermissionGroups();

        groups.add(UnitGroup.recursiveWorkers(getDepartment().getDepartmentUnit()));

        return groups;
    }

    /**
     * The department already has the an internacionalized name.
     * 
     * @see Department#getNameI18n()
     */
    @Override
    public MultiLanguageString getUnitNameWithAcronym() {
        return getDepartment().getNameI18n();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString().with(MultiLanguageString.pt, getUnit().getAcronym());
    }

    @Override
    public String getReversePath() {
        return super.getReversePath() + "/" + getUnit().getAcronym().toLowerCase();
    }

}
