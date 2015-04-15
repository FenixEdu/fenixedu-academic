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
package org.fenixedu.academic.domain.organizationalStructure;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

public class PlanetUnit extends PlanetUnit_Base {

    private PlanetUnit() {
        super();
        super.setType(PartyTypeEnum.PLANET);
    }

    public static PlanetUnit createNewPlanetUnit(MultiLanguageString planetName, String planetNameCard, Integer costCenterCode,
            String planetAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
            UnitClassification classification, Boolean canBeResponsibleOfSpaces, Space campus) {

        PlanetUnit planetUnit = new PlanetUnit();
        planetUnit.init(planetName, planetNameCard, costCenterCode, planetAcronym, beginDate, endDate, webAddress,
                classification, null, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(planetUnit);

        return planetUnit;
    }

    @Override
    public void edit(MultiLanguageString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

        checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(this);
    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        throw new DomainException("error.unit.cannot.have.parentUnit");
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
    }

    @Override
    public boolean isPlanetUnit() {
        return true;
    }

    private static void checkIfAlreadyExistsOnePlanetWithSameAcronymAndName(PlanetUnit planetUnit) {
        for (Unit unit : UnitUtils.readAllUnitsWithoutParents()) {
            if (!unit.equals(planetUnit)
                    && unit.isPlanetUnit()
                    && (planetUnit.getAcronym().equalsIgnoreCase(unit.getAcronym()) || planetUnit.getName().equalsIgnoreCase(
                            unit.getName()))) {
                throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
            }
        }
    }
}
