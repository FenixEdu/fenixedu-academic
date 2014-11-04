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
package org.fenixedu.academic.dto.administrativeOffice.externalUnits;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.dto.LinkObject;

public abstract class AbstractExternalUnitResultBean implements Serializable {

    transient protected static final List<AccountabilityTypeEnum> ACCOUNTABILITY_TYPES = Arrays
            .asList(new AccountabilityTypeEnum[] { AccountabilityTypeEnum.GEOGRAPHIC,
                    AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, AccountabilityTypeEnum.ACADEMIC_STRUCTURE });

    private PartyTypeEnum parentUnitType;
    private String fullName;

    public AbstractExternalUnitResultBean() {
    }

    public PartyTypeEnum getParentUnitType() {
        return parentUnitType;
    }

    public void setParentUnitType(PartyTypeEnum parentUnitType) {
        this.parentUnitType = parentUnitType;
    }

    private boolean hasParentUnitType() {
        return getParentUnitType() != null;
    }

    protected List<Unit> searchFullPath() {
        final List<Unit> units = UnitUtils.getUnitFullPath(getUnit(), ACCOUNTABILITY_TYPES);
        if (hasParentUnitType()) {
            removeAllUnitsUntilParentUnitType(units);
        }
        return units;
    }

    private void removeAllUnitsUntilParentUnitType(final List<Unit> units) {
        final Iterator<Unit> iterUnits = units.iterator();
        while (iterUnits.hasNext()) {
            if (iterUnits.next().getType() == getParentUnitType()) {
                iterUnits.remove();
                break;
            } else {
                iterUnits.remove();
            }
        }
    }

    public String getNumberOfUniversities() {
        return "-";
    }

    public String getNumberOfSchools() {
        return "-";
    }

    public String getNumberOfDepartments() {
        return "-";
    }

    public String getNumberOfExternalCurricularCourses() {
        return "-";
    }

    public String getFullName() {
        if (this.fullName == null) {
            final StringBuilder unitFullPathName = UnitUtils.getUnitFullPathName(getUnit(), ACCOUNTABILITY_TYPES);
            setFullName(unitFullPathName.toString() + " > " + getName());
        }
        return this.fullName;
    }

    protected void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    abstract public Unit getUnit();

    abstract public Enum getType();

    abstract public List<LinkObject> getFullPath();

    abstract public String getName();
}
