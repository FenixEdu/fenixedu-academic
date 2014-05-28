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
package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class OriginInformationForm extends Form {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SchoolLevelType schoolLevel;

    private String otherSchoolLevel;

    private String conclusionGrade;

    private String degreeDesignation;

    private Integer conclusionYear;

    private Integer birthYear;

    private Unit institution;

    private String institutionName;

    private DegreeDesignation raidesDegreeDesignation;

    private Country countryWhereFinishedPreviousCompleteDegree;

    private AcademicalInstitutionType highSchoolType;

    private OriginInformationForm() {
        super();
        setCountryWhereFinishedPreviousCompleteDegree(Country.readDefault());
    }

    public SchoolLevelType getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(SchoolLevelType schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public String getOtherSchoolLevel() {
        return otherSchoolLevel;
    }

    public void setOtherSchoolLevel(String otherSchoolLevel) {
        this.otherSchoolLevel = otherSchoolLevel;
    }

    public String getConclusionGrade() {
        return conclusionGrade;
    }

    public void setConclusionGrade(String conclusionGrade) {
        this.conclusionGrade = conclusionGrade;
    }

    public Integer getConclusionYear() {
        return conclusionYear;
    }

    public void setConclusionYear(Integer conclusionYear) {
        this.conclusionYear = conclusionYear;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getDegreeDesignation() {
        if (getSchoolLevel() != null) {
            return getSchoolLevel().isHigherEducation() && getRaidesDegreeDesignation() != null ? getRaidesDegreeDesignation()
                    .getDescription() : degreeDesignation;
        }
        return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
        this.degreeDesignation = degreeDesignation;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public UnitName getInstitutionUnitName() {
        return (institution == null) ? null : institution.getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
        this.institution = (institutionUnitName == null) ? null : institutionUnitName.getUnit();
    }

    public Country getCountryWhereFinishedPreviousCompleteDegree() {
        return this.countryWhereFinishedPreviousCompleteDegree;
    }

    public void setCountryWhereFinishedPreviousCompleteDegree(Country countryWhereFinishedPreviousCompleteDegree) {
        this.countryWhereFinishedPreviousCompleteDegree = countryWhereFinishedPreviousCompleteDegree;
    }

    public Unit getInstitution() {
        return this.institution;
    }

    public void setInstitution(Unit unit) {
        this.institution = unit;
    }

    public AcademicalInstitutionType getHighSchoolType() {
        if ((getSchoolLevel() != null) && (getSchoolLevel().isHighSchoolOrEquivalent())) {
            return highSchoolType;
        }
        return null;
    }

    public void setHighSchoolType(AcademicalInstitutionType highSchoolType) {
        this.highSchoolType = highSchoolType;
    }

    public void setRaidesDegreeDesignation(DegreeDesignation raidesDegreeDesignation) {
        this.raidesDegreeDesignation = raidesDegreeDesignation;
    }

    public DegreeDesignation getRaidesDegreeDesignation() {
        return raidesDegreeDesignation;
    }

    private static String roundUpGrade(String grade) {
        return String.valueOf(Math.round(Float.valueOf(grade)));
    }

    @Override
    public List<LabelFormatter> validate() {
        if (schoolLevel == SchoolLevelType.OTHER && StringUtils.isEmpty(otherSchoolLevel)) {
            return Collections.singletonList(new LabelFormatter().appendLabel(
                    "error.candidacy.workflow.OriginInformationForm.otherSchoolLevel.must.be.filled", "candidate"));
        }

        LocalDate now = new LocalDate();
        if (now.getYear() < conclusionYear) {
            return Collections.singletonList(new LabelFormatter().appendLabel("error.personalInformation.year.after.current",
                    "candidate"));
        }

        if (conclusionYear < getBirthYear()) {
            return Collections.singletonList(new LabelFormatter().appendLabel("error.personalInformation.year.before.birthday",
                    "candidate"));
        }

        return Collections.emptyList();

    }

    @Override
    public String getSchemaName() {
        if (getSchoolLevel() != null) {
            if (getInstitution() != null) {
                if (getSchoolLevel().isHigherEducation() && StringUtils.isEmpty(getInstitution().getCode())) {
                    setInstitution(null);
                    setInstitutionName(null);
                    setInstitutionUnitName(null);
                    setRaidesDegreeDesignation(null);
                }
                if (getSchoolLevel().isHighSchoolOrEquivalent() && !StringUtils.isEmpty(getInstitution().getCode())) {
                    setInstitution(null);
                    setInstitutionName(null);
                    setInstitutionUnitName(null);
                    setRaidesDegreeDesignation(null);
                }
            } else {
                setInstitution(null);
                setInstitutionName(null);
                setInstitutionUnitName(null);
            }

            if (getSchoolLevel().isHigherEducation() && getInstitution() != null) {
                return super.getSchemaName() + ".higherEducation";
            }
            if (getSchoolLevel().isHigherEducation() && getInstitution() == null) {
                return super.getSchemaName() + ".higherEducationNoInstitution";
            }
            if (getSchoolLevel().isHighSchoolOrEquivalent()) {
                return super.getSchemaName() + ".highSchoolOrEquivalent";
            }
        }
        return super.getSchemaName();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.originInformationForm";
    }

    public static OriginInformationForm createFrom(final StudentCandidacy studentCandidacy) {

        final OriginInformationForm form = new OriginInformationForm();
        form.setBirthYear(studentCandidacy.getPerson().getDateOfBirthYearMonthDay().getYear());
        form.setHighSchoolType(studentCandidacy.getHighSchoolType());
        if (studentCandidacy.hasPrecedentDegreeInformation()) {
            form.setConclusionGrade(roundUpGrade(studentCandidacy.getPrecedentDegreeInformation().getConclusionGrade()));
            form.setDegreeDesignation(studentCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
            form.setInstitution(studentCandidacy.getPrecedentDegreeInformation().getInstitution());
        }

        return form;

    }
}