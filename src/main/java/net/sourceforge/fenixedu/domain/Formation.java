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

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class Formation extends Formation_Base {

    static final public Comparator<Formation> COMPARATOR_BY_BEGIN_YEAR = new Comparator<Formation>() {
        @Override
        public int compare(final Formation f1, final Formation f2) {
            if (f1.getBeginYear() != null && f2.getBeginYear() != null) {
                return f1.getBeginYear().compareTo(f2.getBeginYear());
            } else {
                return f1.getBeginYear() != null ? 1 : -1;
            }
        }
    };

    public Formation() {
        super();
    }

    public Formation(Person person, FormationType formationType, QualificationType degree, EducationArea educationArea,
            String beginYear, String endYear, BigDecimal ectsCredits, Integer formationHours,
            AcademicalInstitutionUnit institution, AcademicalInstitutionUnit baseInstitution,
            AcademicalInstitutionType institutionType, CountryUnit countryUnit) {
        this();

        checkParameters(person, formationType, degree, educationArea, beginYear, endYear, ectsCredits, formationHours,
                baseInstitution, institutionType);

        setPerson(person);
        setFormationType(formationType);
        if (degree != null) {
            setDegree(degree.getName());
        }
        setType(degree);
        setEducationArea(educationArea);
        setBeginYear(beginYear);
        setYear(endYear);
        setInstitution(institution);
        setBaseInstitution(baseInstitution);
        setInstitutionType(institutionType);
        setEctsCredits(ectsCredits);
        setFormationHours(formationHours);
        setCountryUnit(countryUnit);
    }

    private void checkParameters(Person person, FormationType formationType, QualificationType degree,
            EducationArea educationArea, String beginYear, String endYear, BigDecimal ectsCredits, Integer formationHours,
            AcademicalInstitutionUnit institution, AcademicalInstitutionType institutionType) {

        if (person == null) {
            throw new DomainException("formation.creation.person.null");
        }
        if (formationType == null && degree == null && educationArea == null && StringUtils.isEmpty(beginYear)
                && StringUtils.isEmpty(endYear) && ectsCredits == null && institutionType == null && institution == null) {
            throw new DomainException("formation.creation.allFields.null");
        }

        if (!StringUtils.isEmpty(beginYear) && !StringUtils.isEmpty(endYear)) {
            if (Integer.parseInt(beginYear) > Integer.parseInt(endYear)) {
                throw new DomainException("formation.creation.beginDate.after.endDate");
            }
        }
    }

    @Override
    public void delete() {
        setEducationArea(null);
        setInstitution(null);
        setBaseInstitution(null);
        setCountryUnit(null);
        setCreator(null);
        super.delete();
    }

    public void edit(AlumniFormation formation, AcademicalInstitutionUnit academicalInstitutionUnit) {
        checkParameters(formation.getAssociatedFormation().getPerson(), formation.getFormationType(),
                formation.getFormationDegree(), formation.getEducationArea(), formation.getFormationBeginYear(),
                formation.getFormationEndYear(), formation.getFormationCredits(), formation.getFormationHours(),
                formation.getParentInstitution(), formation.getInstitutionType());
        setFormationType(formation.getFormationType());
        if (formation.getFormationDegree() != null) {
            setDegree(formation.getFormationDegree().getName());
        } else {
            setDegree(null);
        }
        setType(formation.getFormationDegree());
        setEducationArea(formation.getEducationArea());
        setBeginYear(formation.getFormationBeginYear());
        setYear(formation.getFormationEndYear());
        setEctsCredits(formation.getFormationCredits());
        setFormationHours(formation.getFormationHours());
        setInstitutionType(formation.getInstitutionType());
        setInstitution(academicalInstitutionUnit);
        setBaseInstitution(formation.getParentInstitution());
        setCountryUnit(formation.getCountryUnit());
        setLastModificationDateDateTime(new DateTime());
    }

    @Deprecated
    public boolean hasCountryUnit() {
        return getCountryUnit() != null;
    }

    @Deprecated
    public boolean hasFormationHours() {
        return getFormationHours() != null;
    }

    @Deprecated
    public boolean hasBaseInstitution() {
        return getBaseInstitution() != null;
    }

    @Deprecated
    public boolean hasEducationArea() {
        return getEducationArea() != null;
    }

    @Deprecated
    public boolean hasFormationType() {
        return getFormationType() != null;
    }

    @Deprecated
    public boolean hasBeginYear() {
        return getBeginYear() != null;
    }

    @Deprecated
    public boolean hasInstitutionType() {
        return getInstitutionType() != null;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

}
