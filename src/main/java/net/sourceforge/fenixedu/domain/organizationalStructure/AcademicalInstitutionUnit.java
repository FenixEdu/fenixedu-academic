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
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public abstract class AcademicalInstitutionUnit extends AcademicalInstitutionUnit_Base {

    @Override
    public boolean isAcademicalUnit() {
        return true;
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.unit.empty.acronym");
        }
        super.setAcronym(acronym);
    }

    @Override
    public List<ExternalCurricularCourse> getAllExternalCurricularCourses() {
        final List<ExternalCurricularCourse> result = new ArrayList<ExternalCurricularCourse>(getExternalCurricularCourses());
        for (Unit subUnit : getSubUnits()) {
            if (subUnit.isDepartmentUnit()) {
                result.addAll(subUnit.getExternalCurricularCourses());
            }
        }
        return result;
    }

    @Override
    public Boolean isOfficial() {
        return (getOfficial() != null && getOfficial().equals(Boolean.TRUE));
    }

    public Boolean isOfficialAndIsType(AcademicalInstitutionType type) {
        return (isOfficial() && getInstitutionType() != null && getInstitutionType().equals(type));
    }

    protected static List<AcademicalInstitutionUnit> readOfficialUnits() {
        final List<AcademicalInstitutionUnit> officialUnits = new ArrayList<AcademicalInstitutionUnit>();

        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
            if (unitName.getUnit().isOfficial()) {
                officialUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
            }
        }
        return officialUnits;
    }

    public static List<AcademicalInstitutionUnit> readOfficialParentUnitsByType(AcademicalInstitutionType type) {
        final List<AcademicalInstitutionUnit> parentUnits = new ArrayList<AcademicalInstitutionUnit>();

        Unit countryUnit = CountryUnit.getDefault();
        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
            if (unit.hasParentUnit(countryUnit) && unit.isOfficialAndIsType(type)) {
                parentUnits.add(unit);
            }
        }
        return parentUnits;
    }

    public static List<Unit> readOfficialChildUnits(AcademicalInstitutionUnit parentUnit) {
        final List<Unit> childUnits = new ArrayList<Unit>();
        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
            if (unit.hasParentUnit(parentUnit)) {
                childUnits.add(unit);
            }
        }
        return childUnits;
    }

    public static List<AcademicalInstitutionUnit> readOtherAcademicUnits() {
        final List<AcademicalInstitutionUnit> otherUnits = new ArrayList<AcademicalInstitutionUnit>();
        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
            if (unitName.getUnit().isAcademicalUnit() && !unitName.getUnit().isOfficial()) {
                otherUnits.add((AcademicalInstitutionUnit) unitName.getUnit());
            }
        }
        return otherUnits;
    }

    public Boolean hasAnyOfficialChilds() {
        for (final AcademicalInstitutionUnit unit : readOfficialUnits()) {
            if (unit.hasParentUnit(this)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public abstract String getFullPresentationName();

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession> getInternshipCandidacySession() {
        return getInternshipCandidacySessionSet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacySession() {
        return !getInternshipCandidacySessionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getInternshipCandidacy() {
        return getInternshipCandidacySet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacy() {
        return !getInternshipCandidacySet().isEmpty();
    }

    @Deprecated
    public boolean hasInstitutionType() {
        return getInstitutionType() != null;
    }

    @Deprecated
    public boolean hasOfficial() {
        return getOfficial() != null;
    }

}
