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
package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class ExternalPersonBean extends PersonBean {

    private Unit unitDomainReference;

    private UnitName unitNameDomainReference;

    private PersonName personName;

    private String unitName;

    public ExternalPersonBean() {
        super();
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Unit getUnit() {
        return unitDomainReference;
    }

    public void setUnit(Unit unit) {
        this.unitDomainReference = unit;
    }

    public UnitName getUnitNameDomainReference() {
        return unitNameDomainReference;
    }

    public void setUnitNameDomainReference(UnitName unitName) {
        if (unitName != null) {
            this.unitNameDomainReference = unitName;
            setUnit(unitName.getUnit());
        }
    }

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        if (personName != null) {
            this.personName = personName;
            setPerson(personName.getPerson());
        }
    }
}
