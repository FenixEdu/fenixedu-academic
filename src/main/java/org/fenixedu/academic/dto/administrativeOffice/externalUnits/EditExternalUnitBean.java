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

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.Bennu;

public class EditExternalUnitBean extends CreateExternalUnitBean {

    private Unit externalUnit;

    public EditExternalUnitBean(final Unit unit) {
        super();
        setParentUnit(unit.equals(Bennu.getInstance().getEarthUnit()) ? null : unit.getParentUnits().iterator().next());
        setUnitType(unit.getPartyType().getType());
        setUnitName(unit.getName());
        setUnitCode(unit.getAcronym());
        setExternalUnit(unit);
    }

    public Unit getExternalUnit() {
        return this.externalUnit;
    }

    public void setExternalUnit(Unit externalUnit) {
        this.externalUnit = externalUnit;
    }
}
