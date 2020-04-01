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
import org.fenixedu.academic.domain.exceptions.DomainException;

public abstract class AcademicalInstitutionUnit extends AcademicalInstitutionUnit_Base {

//    @Override
//    public boolean isAcademicalUnit() {
//        return true;
//    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
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

//    @Override
//    public Boolean isOfficial() {
//        return (getOfficial() != null && getOfficial().equals(Boolean.TRUE));
//    }

//    public Boolean isOfficialAndIsType(AcademicalInstitutionType type) {
//        return (isOfficial() && getInstitutionType() != null && getInstitutionType().equals(type));
//    }

//    protected static List<AcademicalInstitutionUnit> readOfficialUnits() {
//        final List<AcademicalInstitutionUnit> officialUnits = new ArrayList<AcademicalInstitutionUnit>();
//
//        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
//            if (unitName.getUnit().isOfficial()) {
//                officialUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
//            }
//        }
//        return officialUnits;
//    }

//    public static List<AcademicalInstitutionUnit> readOfficialParentUnitsByType(AcademicalInstitutionType type) {
//        final List<AcademicalInstitutionUnit> parentUnits = new ArrayList<AcademicalInstitutionUnit>();
//
//        Unit countryUnit = CountryUnit.getDefault();
//        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
//            if (unit.hasParentUnit(countryUnit) && unit.isOfficialAndIsType(type)) {
//                parentUnits.add(unit);
//            }
//        }
//        return parentUnits;
//    }

//    public static List<Unit> readOfficialChildUnits(AcademicalInstitutionUnit parentUnit) {
//        final List<Unit> childUnits = new ArrayList<Unit>();
//        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
//            if (unit.hasParentUnit(parentUnit)) {
//                childUnits.add(unit);
//            }
//        }
//        return childUnits;
//    }
//
//    public static List<AcademicalInstitutionUnit> readOtherAcademicUnits() {
//        final List<AcademicalInstitutionUnit> otherUnits = new ArrayList<AcademicalInstitutionUnit>();
//        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
//            if (unitName.getUnit().isAcademicalUnit() && !unitName.getUnit().isOfficial()) {
//                otherUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
//            }
//        }
//        return otherUnits;
//    }
//
//    public Boolean hasAnyOfficialChilds() {
//        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
//            if (unit.hasParentUnit(this)) {
//                return Boolean.TRUE;
//            }
//        }
//        return Boolean.FALSE;
//    }

//    public abstract String getFullPresentationName();

}
