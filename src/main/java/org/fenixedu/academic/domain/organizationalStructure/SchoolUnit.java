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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

public class SchoolUnit extends SchoolUnit_Base {

    private SchoolUnit() {
        super();
        super.setType(PartyTypeEnum.SCHOOL);
    }

    @Override
    public boolean isSchoolUnit() {
        return true;
    }

    public static SchoolUnit createNewSchoolUnit(LocalizedString schoolName, String schoolNameCard, Unit parentUnit,
            Boolean official, String code, AcademicalInstitutionType institutionType) {

        SchoolUnit schoolUnit = new SchoolUnit();
        schoolUnit.setPartyName(schoolName);
        schoolUnit.setIdentificationCardLabel(schoolNameCard);
        schoolUnit.setOfficial(official);
        schoolUnit.setCode(code);
//        schoolUnit.setInstitutionType(institutionType);
        schoolUnit.setBeginDateYearMonthDay(YearMonthDay.fromDateFields(new GregorianCalendar().getTime()));
        schoolUnit.setCanBeResponsibleOfSpaces(Boolean.FALSE);
        return createNewUnit(parentUnit, schoolUnit, Boolean.FALSE);
    }

    public static SchoolUnit createNewSchoolUnit(LocalizedString schoolName, String schoolNameCard, Integer costCenterCode,
            String schoolAcronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, String webAddress,
            UnitClassification classification, Boolean canBeResponsibleOfSpaces, Space campus) {

        SchoolUnit schoolUnit = new SchoolUnit();
        schoolUnit.init(schoolName, schoolNameCard, costCenterCode, schoolAcronym, beginDate, endDate, webAddress,
                classification, null, canBeResponsibleOfSpaces, campus);
        return createNewUnit(parentUnit, schoolUnit, Boolean.TRUE);
    }

    private static SchoolUnit createNewUnit(Unit parentUnit, SchoolUnit schoolUnit, Boolean checkExistingUnit) {
        if (parentUnit.isCountryUnit()) {
            schoolUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC));
        } else if (parentUnit.isUniversityUnit()) {
            schoolUnit.addParentUnit(parentUnit, AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        }

        if (checkExistingUnit) {
            checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(schoolUnit);
        }

        return schoolUnit;
    }

    @Override
    public void edit(LocalizedString name, String acronym) {
        super.edit(name, acronym);
        checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(this);
    }

    @Override
    public void edit(LocalizedString unitName, String unitNameCard, Integer unitCostCenter, String acronym,
            YearMonthDay beginDate, YearMonthDay endDate, String webAddress, UnitClassification classification,
            Department department, Degree degree, AdministrativeOffice administrativeOffice, Boolean canBeResponsibleOfSpaces,
            Space campus) {

        super.edit(unitName, unitNameCard, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department,
                degree, administrativeOffice, canBeResponsibleOfSpaces, campus);
        checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(this);
    }

    protected static void checkIfAlreadyExistsOneSchoolWithSameAcronymAndName(SchoolUnit schoolUnit) {
        for (Unit parentUnit : schoolUnit.getParentUnits()) {
            for (Unit unit : parentUnit.getAllSubUnits()) {
                if (!unit.equals(schoolUnit)
                        && unit.isSchoolUnit()
                        && ((schoolUnit.getAcronym() != null && schoolUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) || schoolUnit
                                .getName().equalsIgnoreCase(unit.getName()))) {
                    throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
                }
            }
        }
    }

//    @Override
//    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
//        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCoursesSet());
//        for (Unit subUnit : getSubUnits()) {
//            if (subUnit.isDepartmentUnit()) {
//                result.addAll(subUnit.getExternalCurricularCoursesSet());
//            }
//        }
//        return result;
//    }

    @Override
    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
        if (parentUnit != null
                && (!parentUnit.isOfficialExternal() || (!parentUnit.isPlanetUnit() && !parentUnit.isCountryUnit() && !parentUnit
                        .isUniversityUnit()))) {
            throw new DomainException("error.unit.invalid.parentUnit");
        }
        return super.addParentUnit(parentUnit, accountabilityType);
    }

//    @Override
//    public String getFullPresentationName() {
//        StringBuilder output = new StringBuilder();
//        output.append(getName().trim());
//        output.append(" da ");
//        List<Unit> parents = getParentUnitsPath();
//        output.append(parents.get(parents.size() - 1).getName());
//        return output.toString();
//    }
}
