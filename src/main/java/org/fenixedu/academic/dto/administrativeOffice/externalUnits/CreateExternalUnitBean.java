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

import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

public class CreateExternalUnitBean implements Serializable {

    private Unit parentUnit;
    private PartyTypeEnum unitType;
    private String unitName;
    private String unitCode;

    protected CreateExternalUnitBean() {
    }

    public CreateExternalUnitBean(final Unit parentUnit, PartyTypeEnum unitType) {
        setParentUnit(parentUnit);
        setUnitType(unitType);
    }

    public Unit getParentUnit() {
        return this.parentUnit;
    }

    public void setParentUnit(Unit parentUnit) {
        this.parentUnit = parentUnit;
    }

    public PartyTypeEnum getUnitType() {
        return unitType;
    }

    public void setUnitType(PartyTypeEnum unitType) {
        this.unitType = unitType;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
