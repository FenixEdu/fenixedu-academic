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

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class DegreeUnit extends DegreeUnit_Base {

    private DegreeUnit() {
        super();
        super.setType(PartyTypeEnum.DEGREE_UNIT);
    }

    public static DegreeUnit createNewInternalDegreeUnit(final LocalizedString unitName, final String unitNameCard,
            final Integer costCenterCode, final String acronym, final YearMonthDay beginDate, final YearMonthDay endDate,
            final Unit parentUnit, final AccountabilityType accountabilityType, final String webAddress, final Degree degree,
            final UnitClassification classification, final Boolean canBeResponsibleOfSpaces, final Space campus) {

        DegreeUnit degreeUnit = new DegreeUnit();
        degreeUnit.init(unitName, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        degreeUnit.setDegree(degree);
        degreeUnit.addParentUnit(parentUnit, accountabilityType);

        checkIfAlreadyExistsOneDegreeWithSameAcronym(degreeUnit);

        return degreeUnit;
    }

    @Override
    public void edit(final LocalizedString unitName, final String unitNameCard, final Integer unitCostCenter,
            final String acronym, final YearMonthDay beginDate, final YearMonthDay endDate, final String webAddress,
            final UnitClassification classification, final Department department, final Degree degree,
            final AdministrativeOffice administrativeOffice, final Boolean canBeResponsibleOfSpaces, final Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        setDegree(degree);

        checkIfAlreadyExistsOneDegreeWithSameAcronym(this);
    }

    @Override
    public Accountability addParentUnit(final Unit parentUnit, final AccountabilityType accountabilityType) {
        if (parentUnit != null && !parentUnit.isInternal()) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

    @Override
    public void setAcronym(final String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
    }

    @Override
    public void setType(final PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isDegreeUnit() {
        return true;
    }

    @Override
    public void setDegree(final Degree degree) {
        if (degree == null) {
            throw new DomainException("error.DegreeUnit.empty.degree");
        }
        super.setDegree(degree);
    }

    public void edit(final Collection<Unit> parentUnits, final LocalizedString name, final String acronym, final LocalDate begin,
            final LocalDate end) {

        setPartyName(name);
        setAcronym(acronym);
        updateParentUnits(parentUnits, begin, end);
    }

    @Override
    public void delete() {
        super.setDegree(null);
        super.delete();
    }

    private static void checkIfAlreadyExistsOneDegreeWithSameAcronym(final DegreeUnit degreeUnit) {
        for (Unit unit : UnitUtils.readInstitutionUnit().getAllSubUnits()) {
            if (!unit.equals(degreeUnit) && unit.isDegreeUnit() && degreeUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
                throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
            }
        }
    }

//    public SchoolUnit getSchoolUnit() {
//        Unit current = this;
//        while (current != null) {
//            if (current.getType().equals(PartyTypeEnum.SCHOOL)) {
//                return (SchoolUnit) current;
//            }
//            Collection<Unit> parentUnits = current.getParentUnits(AccountabilityTypeEnum.ACADEMIC_STRUCTURE);
//            current = parentUnits.size() > 0 ? parentUnits.iterator().next() : null;
//        }
//        return null;
//    }
}
