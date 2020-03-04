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
package org.fenixedu.academic.domain.candidacy;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GrantOwnerType;
import org.fenixedu.academic.domain.ProfessionType;
import org.fenixedu.academic.domain.ProfessionalSituationConditionType;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.SchoolPeriodDuration;
import org.fenixedu.academic.domain.organizationalStructure.AcademicalInstitutionType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitName;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.fenixedu.academic.domain.raides.DegreeDesignation;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PersonalInformationBean implements Serializable {

    static private final LocalDate limitDate = new LocalDate(2014, 12, 16);

    static public boolean isPastLimitDate() {
        return new LocalDate().isAfter(limitDate);
    }

    static public Comparator<PersonalInformationBean> COMPARATOR_BY_DESCRIPTION = new Comparator<PersonalInformationBean>() {

        @Override
        public int compare(PersonalInformationBean o1, PersonalInformationBean o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    };

    static private final long serialVersionUID = 1144682974757187722L;

    private Registration registration;
    private Country countryOfResidence;
    private DistrictSubdivision districtSubdivisionOfResidence;
    private Boolean dislocatedFromPermanentResidence;
    private DistrictSubdivision schoolTimeDistrictSubdivisionOfResidence;
    private GrantOwnerType grantOwnerType;
    private Unit grantOwnerProvider;
    private String grantOwnerProviderName;
    private AcademicalInstitutionType highSchoolType;
    private MaritalStatus maritalStatus;
    private ProfessionType professionType;
    private ProfessionalSituationConditionType professionalCondition;
    private SchoolLevelType motherSchoolLevel;
    private ProfessionType motherProfessionType;
    private ProfessionalSituationConditionType motherProfessionalCondition;
    private SchoolLevelType fatherSchoolLevel;
    private ProfessionType fatherProfessionType;
    private ProfessionalSituationConditionType fatherProfessionalCondition;
    private String conclusionGrade;
    private Integer conclusionYear;
    private Unit institution;
    private String institutionName;
    private String degreeDesignation;
    private DegreeDesignation raidesDegreeDesignation;
    private Country countryWhereFinishedPreviousCompleteDegree;
    private Country countryWhereFinishedHighSchoolLevel;
    private SchoolLevelType schoolLevel;
    private String otherSchoolLevel;
    private DateTime lastModifiedDate;

    private boolean degreeChangeOrTransferOrErasmusStudent = false;
    private SchoolLevelType precedentSchoolLevel;
    private String otherPrecedentSchoolLevel;
    private Unit precedentInstitution;
    private String precedentInstitutionName;
    private String precedentDegreeDesignation;
    private DegreeDesignation precedentDegreeDesignationObject;
    private Integer numberOfPreviousYearEnrolmentsInPrecedentDegree;
    private SchoolPeriodDuration mobilityProgramDuration;

    public PersonalInformationBean(Registration registration) {
        setRegistration(registration);

        initFromLatestPersonalIngressionData();
    }

    public PersonalInformationBean(PrecedentDegreeInformation degreeInfo) {
        setRegistration(degreeInfo.getRegistration());
        setSchoolLevel(degreeInfo.getSchoolLevel());
        setOtherSchoolLevel(degreeInfo.getOtherSchoolLevel());
        setConclusionGrade(degreeInfo.getConclusionGrade());
        setConclusionYear(degreeInfo.getConclusionYear());
        setCountryWhereFinishedPreviousCompleteDegree(degreeInfo.getCountry());
        setCountryWhereFinishedHighSchoolLevel(degreeInfo.getCountryHighSchool());

        Unit institution = degreeInfo.getInstitution();
        if (!isUnitFromRaidesListMandatory() || (institution != null && !StringUtils.isEmpty(institution.getCode()))) {
            setInstitution(institution);
            setDegreeDesignation(degreeInfo.getDegreeDesignation());
        }

        setPrecedentDegreeDesignation(degreeInfo.getPrecedentDegreeDesignation());
        setPrecedentInstitution(degreeInfo.getPrecedentInstitution());
        setPrecedentSchoolLevel(degreeInfo.getPrecedentSchoolLevel());
        setOtherPrecedentSchoolLevel(degreeInfo.getOtherPrecedentSchoolLevel());
        setNumberOfPreviousYearEnrolmentsInPrecedentDegree(degreeInfo.getNumberOfEnrolmentsInPreviousDegrees());
        setMobilityProgramDuration(degreeInfo.getMobilityProgramDuration());
        if (getPrecedentDegreeDesignation() != null || getPrecedentInstitution() != null || getPrecedentSchoolLevel() != null
                || getNumberOfPreviousYearEnrolmentsInPrecedentDegree() != null) {
            setDegreeChangeOrTransferOrErasmusStudent(true);
        }
        if (isUnitFromRaidesListMandatory()) {
            setRaidesDegreeDesignation(DegreeDesignation.readByNameAndSchoolLevel(degreeDesignation, schoolLevel));
        }

        initFromLatestPersonalIngressionData();
    }

    public PersonalInformationBean() {
    }

    private void initFromLatestPersonalIngressionData() {
        PersonalIngressionData personalData = getStudent().getLatestPersonalIngressionData();
        if (personalData == null) {
            return;
        }

        setCountryOfResidence(personalData.getCountryOfResidence());
        setDistrictSubdivisionOfResidence(personalData.getDistrictSubdivisionOfResidence());
        setSchoolTimeDistrictSubdivisionOfResidence(personalData.getSchoolTimeDistrictSubDivisionOfResidence());
        setDislocatedFromPermanentResidence(personalData.getDislocatedFromPermanentResidence());
        setGrantOwnerType(personalData.getGrantOwnerType());
        setGrantOwnerProvider(personalData.getGrantOwnerProvider());
        setHighSchoolType(personalData.getHighSchoolType());
        setMaritalStatus(personalData.getMaritalStatus());
        setProfessionType(personalData.getProfessionType());
        setProfessionalCondition(personalData.getProfessionalCondition());
        setMotherSchoolLevel(personalData.getMotherSchoolLevel());
        setMotherProfessionType(personalData.getMotherProfessionType());
        setMotherProfessionalCondition(personalData.getMotherProfessionalCondition());
        setFatherSchoolLevel(personalData.getFatherSchoolLevel());
        setFatherProfessionType(personalData.getFatherProfessionType());
        setFatherProfessionalCondition(personalData.getFatherProfessionalCondition());
        setLastModifiedDate(personalData.getLastModifiedDate());
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public Registration getRegistration() {
        return this.registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    public Country getCountryOfResidence() {
        return this.countryOfResidence;
    }

    public void setCountryOfResidence(Country country) {
        this.countryOfResidence = country;
    }

    public DistrictSubdivision getDistrictSubdivisionOfResidence() {
        return this.districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
        this.districtSubdivisionOfResidence = districtSubdivision;
    }

    public Boolean getDislocatedFromPermanentResidence() {
        return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
        this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public DistrictSubdivision getSchoolTimeDistrictSubdivisionOfResidence() {
        return this.schoolTimeDistrictSubdivisionOfResidence;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
        this.schoolTimeDistrictSubdivisionOfResidence = districtSubdivision;
    }

    public GrantOwnerType getGrantOwnerType() {
        return grantOwnerType;
    }

    public void setGrantOwnerType(GrantOwnerType grantOwnerType) {
        this.grantOwnerType = grantOwnerType;
    }

    public Unit getGrantOwnerProvider() {
        return this.grantOwnerProvider;
    }

    public void setGrantOwnerProvider(Unit grantOwnerProvider) {
        this.grantOwnerProvider = grantOwnerProvider;
    }

    public String getGrantOwnerProviderName() {
        return grantOwnerProviderName;
    }

    public void setGrantOwnerProviderName(String grantOwnerProviderName) {
        this.grantOwnerProviderName = grantOwnerProviderName;
    }

    public UnitName getGrantOwnerProviderUnitName() {
        return (grantOwnerProvider == null) ? null : grantOwnerProvider.getUnitName();
    }

    public void setGrantOwnerProviderUnitName(UnitName grantOwnerProviderUnitName) {
        this.grantOwnerProvider = (grantOwnerProviderUnitName == null) ? null : grantOwnerProviderUnitName.getUnit();
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

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = (maritalStatus == MaritalStatus.UNKNOWN ? null : maritalStatus);
    }

    public boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    public ProfessionType getProfessionType() {
        return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
        this.professionType = professionType;
    }

    public boolean hasProfessionType() {
        return getProfessionType() != null;
    }

    public ProfessionalSituationConditionType getProfessionalCondition() {
        return professionalCondition;
    }

    public void setProfessionalCondition(ProfessionalSituationConditionType professionalCondition) {
        this.professionalCondition = professionalCondition;
    }

    public boolean hasProfessionalCondition() {
        return getProfessionalCondition() != null;
    }

    public SchoolLevelType getMotherSchoolLevel() {
        return motherSchoolLevel;
    }

    public void setMotherSchoolLevel(SchoolLevelType motherSchoolLevel) {
        this.motherSchoolLevel = motherSchoolLevel;
    }

    public boolean hasMotherSchoolLevel() {
        return getMotherSchoolLevel() != null;
    }

    public ProfessionType getMotherProfessionType() {
        return motherProfessionType;
    }

    public void setMotherProfessionType(ProfessionType motherProfessionType) {
        this.motherProfessionType = motherProfessionType;
    }

    public boolean hasMotherProfessionType() {
        return getMotherProfessionType() != null;
    }

    public ProfessionalSituationConditionType getMotherProfessionalCondition() {
        return motherProfessionalCondition;
    }

    public void setMotherProfessionalCondition(ProfessionalSituationConditionType motherProfessionalCondition) {
        this.motherProfessionalCondition = motherProfessionalCondition;
    }

    public boolean hasMotherProfessionalCondition() {
        return getMotherProfessionalCondition() != null;
    }

    public SchoolLevelType getFatherSchoolLevel() {
        return fatherSchoolLevel;
    }

    public void setFatherSchoolLevel(SchoolLevelType fatherSchoolLevel) {
        this.fatherSchoolLevel = fatherSchoolLevel;
    }

    public boolean hasFatherSchoolLevel() {
        return getFatherSchoolLevel() != null;
    }

    public ProfessionType getFatherProfessionType() {
        return fatherProfessionType;
    }

    public void setFatherProfessionType(ProfessionType fatherProfessionType) {
        this.fatherProfessionType = fatherProfessionType;
    }

    public boolean hasFatherProfessionType() {
        return getFatherProfessionType() != null;
    }

    public ProfessionalSituationConditionType getFatherProfessionalCondition() {
        return fatherProfessionalCondition;
    }

    public void setFatherProfessionalCondition(ProfessionalSituationConditionType fatherProfessionalCondition) {
        this.fatherProfessionalCondition = fatherProfessionalCondition;
    }

    public boolean hasFatherProfessionalCondition() {
        return getFatherProfessionalCondition() != null;
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

    public Unit getInstitution() {
        return this.institution;
    }

    public void setInstitution(Unit institution) {
        this.institution = institution;
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

    public String getDegreeDesignation() {
        if (isUnitFromRaidesListMandatory()) {
            return getRaidesDegreeDesignation() != null ? getRaidesDegreeDesignation().getDescription() : null;
        } else {
            return degreeDesignation;
        }
    }

    public boolean isUnitFromRaidesListMandatory() {
        return getSchoolLevel() != null && getSchoolLevel().isHigherEducation()
                && getCountryWhereFinishedPreviousCompleteDegree() != null
                && getCountryWhereFinishedPreviousCompleteDegree().isDefaultCountry();
    }

    public void setDegreeDesignation(String degreeDesignation) {
        this.degreeDesignation = degreeDesignation;
    }

    public Country getCountryWhereFinishedPreviousCompleteDegree() {
        return this.countryWhereFinishedPreviousCompleteDegree;
    }

    public void setCountryWhereFinishedPreviousCompleteDegree(Country country) {
        if ((getSchoolLevel() != null) && getSchoolLevel().isHighSchoolOrEquivalent()) {
            setCountryWhereFinishedHighSchoolLevel(country);
        }
        this.countryWhereFinishedPreviousCompleteDegree = country;
    }

    public boolean isHightSchoolCountryFieldRequired() {
        return (getSchoolLevel() != null) && !getSchoolLevel().isHighSchoolOrEquivalent()
                && !getSchoolLevel().isSchoolLevelBasicCycle();
    }

    public Country getCountryWhereFinishedHighSchoolLevel() {
        return this.countryWhereFinishedHighSchoolLevel;
    }

    public void setCountryWhereFinishedHighSchoolLevel(Country country) {
        this.countryWhereFinishedHighSchoolLevel = country;
    }

    public SchoolLevelType getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(SchoolLevelType schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public boolean hasSchoolLevel() {
        return getSchoolLevel() != null;
    }

    public String getOtherSchoolLevel() {
        return otherSchoolLevel;
    }

    public void setOtherSchoolLevel(String otherSchoolLevel) {
        this.otherSchoolLevel = otherSchoolLevel;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> validate() {

        final Set<String> result = new HashSet<String>();

        if (getConclusionGrade() == null || getConclusionYear() == null || getCountryOfResidence() == null
                || getGrantOwnerType() == null || getDislocatedFromPermanentResidence() == null || !isSchoolLevelValid()
                || !isHighSchoolLevelValid() || !isMaritalStatusValid() || !isProfessionalConditionValid()
                || !isProfessionTypeValid() || !isMotherSchoolLevelValid() || !isMotherProfessionTypeValid()
                || !isMotherProfessionalConditionValid() || !isFatherProfessionalConditionValid()
                || !isFatherProfessionTypeValid() || !isFatherSchoolLevelValid()
                || getCountryWhereFinishedPreviousCompleteDegree() == null || !isCountryWhereFinishedHighSchoolValid()
                || (getDegreeDesignation() == null && !isUnitFromRaidesListMandatory())
                || (getInstitution() == null && StringUtils.isEmpty(getInstitutionName()))) {
            result.add("error.CandidacyInformationBean.required.information.must.be.filled");
        }

        LocalDate now = new LocalDate();
        if (getConclusionYear() != null && now.getYear() < getConclusionYear()) {
            result.add("error.personalInformation.year.after.current");
        }

        int birthYear = getStudent().getPerson().getDateOfBirthYearMonthDay().getYear();
        if (getConclusionYear() != null && getConclusionYear() < birthYear) {
            result.add("error.personalInformation.year.before.birthday");
        }

        if (getSchoolLevel() != null && !getSchoolLevel().isSchoolLevelBasicCycle() && !getSchoolLevel().isOther()
                && getConclusionYear() != null && getConclusionYear() < birthYear + 15) {
            result.add("error.personalInformation.year.before.fifteen.years.old");
        }

        if (isUnitFromRaidesListMandatory()) {
            if (getInstitution() == null) {
                result.add("error.personalInformation.required.institution");
            }
            if (getDegreeDesignation() == null) {
                result.add("error.personalInformation.required.degreeDesignation");
            }
        }

        if (getCountryOfResidence() != null) {
            if (getCountryOfResidence().isDefaultCountry() && getDistrictSubdivisionOfResidence() == null) {
                result.add("error.CandidacyInformationBean.districtSubdivisionOfResidence.is.required.for.default.country");
            }
            if (!getCountryOfResidence().isDefaultCountry()
                    && (getDislocatedFromPermanentResidence() == null || !getDislocatedFromPermanentResidence())) {
                result.add("error.CandidacyInformationBean.foreign.students.must.select.dislocated.option");
            }
        }

        if (getDislocatedFromPermanentResidence() != null && getDislocatedFromPermanentResidence()
                && getSchoolTimeDistrictSubdivisionOfResidence() == null) {
            result.add(
                    "error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.is.required.for.dislocated.students");
        }

        if (getSchoolLevel() != null && getSchoolLevel() == SchoolLevelType.OTHER && StringUtils.isEmpty(getOtherSchoolLevel())) {
            result.add("error.CandidacyInformationBean.other.school.level.description.is.required");
        }

        if (isUnitFromRaidesListMandatory() && hasRaidesDegreeDesignation()
                && !getRaidesDegreeDesignation().getInstitutionUnitSet().contains(getInstitution())) {
            result.add("error.CandidacyInformationBean.designation.must.match.institution");
        }

        if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
                && getGrantOwnerProvider() == null) {
            result.add(
                    "error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
        }

        return result;

    }

    public Set<String> validateForAcademicService() {
        Set<String> result = validate();
        if (!result.isEmpty()) {
            return result;
        }
        if (isDegreeChangeOrTransferOrErasmusStudent()) {
            if (StringUtils.isEmpty(getPrecedentDegreeDesignation())
                    || (getPrecedentInstitution() == null && StringUtils.isEmpty(getPrecedentInstitutionName()))
                    || getPrecedentSchoolLevel() == null || getNumberOfPreviousYearEnrolmentsInPrecedentDegree() == null) {
                result.add("error.PersonInformationBean.precedentDegree.fields.mandatory");
            }
            if (SchoolLevelType.OTHER.equals(getPrecedentSchoolLevel()) && StringUtils.isEmpty(getOtherPrecedentSchoolLevel())) {
                result.add("error.other.precedent.school.level.description.is.required");
            }
        }
        return result;
    }

    public boolean isEditableByAcademicService() {
        Set<String> result = validate();
        if (!result.isEmpty()) {
            return true;
        }
        if (getPrecedentDegreeDesignation() == null || getPrecedentInstitution() == null || getPrecedentSchoolLevel() == null
                || getNumberOfPreviousYearEnrolmentsInPrecedentDegree() == null || getMobilityProgramDuration() == null) {
            return true;
        }
        return false;
    }

    public boolean isMaritalStatusValid() {
        return hasMaritalStatus() && getMaritalStatus() != MaritalStatus.UNKNOWN;
    }

    public boolean isSchoolLevelValid() {
        return hasSchoolLevel() && getSchoolLevel().isForStudent();
    }

    private boolean isCountryWhereFinishedHighSchoolValid() {
        if (getSchoolLevel() == null) {
            return false;
        }
        return (getCountryWhereFinishedHighSchoolLevel() != null && !getSchoolLevel().isSchoolLevelBasicCycle())
                || (getCountryWhereFinishedHighSchoolLevel() == null && getSchoolLevel().isSchoolLevelBasicCycle());
    }

    public boolean isHighSchoolLevelValid() {
        return !getSchoolLevel().isHighSchoolOrEquivalent() || getHighSchoolType() != null;
    }

    public boolean isProfessionTypeValid() {
        return hasProfessionType() && getProfessionType().isActive();
    }

    public boolean isProfessionalConditionValid() {
        return hasProfessionalCondition() && getProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE
                && getProfessionalCondition() != ProfessionalSituationConditionType.UNKNOWN;
    }

    public boolean isMotherProfessionTypeValid() {
        return hasMotherProfessionType() && getMotherProfessionType().isActive();
    }

    public boolean isMotherProfessionalConditionValid() {
        return hasMotherProfessionalCondition()
                && getMotherProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE
                && getMotherProfessionalCondition() != ProfessionalSituationConditionType.UNKNOWN;
    }

    public boolean isMotherSchoolLevelValid() {
        return hasMotherSchoolLevel() && getMotherSchoolLevel().isForStudentHousehold();
    }

    public boolean isFatherProfessionTypeValid() {
        return hasFatherProfessionType() && getFatherProfessionType().isActive();
    }

    public boolean isFatherProfessionalConditionValid() {
        return hasFatherProfessionalCondition()
                && getFatherProfessionalCondition() != ProfessionalSituationConditionType.MILITARY_SERVICE
                && getFatherProfessionalCondition() != ProfessionalSituationConditionType.UNKNOWN;
    }

    public boolean isFatherSchoolLevelValid() {
        return hasFatherSchoolLevel() && getFatherSchoolLevel().isForStudentHousehold();
    }

    public boolean isValid() {
        return validate().isEmpty();
    }

    public boolean isValidForAcademicService() {
        return validateForAcademicService().isEmpty();
    }

    public void resetInstitutionAndDegree() {
        setInstitution(null);
        setInstitutionName(null);
        setDegreeDesignation(null);
        setRaidesDegreeDesignation(null);
    }

    public void resetDegree() {
        setDegreeDesignation(null);
        setRaidesDegreeDesignation(null);
    }

    public String getFormattedValues() {
        try (Formatter result = new Formatter()) {
            final Student student = getStudent();
            result.format("Student Number: %d\n", student.getNumber());
            result.format("Name: %s\n", student.getPerson().getName());
            result.format("Degree: %s\n", getRegistration().getDegree().getPresentationName());
            return result.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PersonalInformationBean)) {
            return false;
        }

        final PersonalInformationBean other = (PersonalInformationBean) obj;

        if (hasRegistration() && other.hasRegistration()) {
            return getRegistration().equals(other.getRegistration());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + getRegistration().hashCode();

        return result;
    }

    public String getDescription() {
        return getRegistration().getDegreeNameWithDescription();
    }

    public void setRaidesDegreeDesignation(DegreeDesignation raidesDegreeDesignation) {
        this.raidesDegreeDesignation = raidesDegreeDesignation;
    }

    public DegreeDesignation getRaidesDegreeDesignation() {
        return raidesDegreeDesignation;
    }

    public boolean hasRaidesDegreeDesignation() {
        return raidesDegreeDesignation != null;
    }

    public void setDegreeChangeOrTransferOrErasmusStudent(boolean degreeChangeOrTransferOrErasmusStudent) {
        this.degreeChangeOrTransferOrErasmusStudent = degreeChangeOrTransferOrErasmusStudent;
    }

    public boolean isDegreeChangeOrTransferOrErasmusStudent() {
        return degreeChangeOrTransferOrErasmusStudent;
    }

    public SchoolLevelType getPrecedentSchoolLevel() {
        return precedentSchoolLevel;
    }

    public void setPrecedentSchoolLevel(SchoolLevelType precedentSchoolLevel) {
        this.precedentSchoolLevel = precedentSchoolLevel;
    }

    public void setOtherPrecedentSchoolLevel(String otherPrecedentSchoolLevel) {
        this.otherPrecedentSchoolLevel = otherPrecedentSchoolLevel;
    }

    public String getOtherPrecedentSchoolLevel() {
        return otherPrecedentSchoolLevel;
    }

    public Unit getPrecedentInstitution() {
        return precedentInstitution;
    }

    public void setPrecedentInstitution(Unit precedentInstitution) {
        this.precedentInstitution = precedentInstitution;
    }

    public String getPrecedentInstitutionName() {
        return precedentInstitutionName;
    }

    public void setPrecedentInstitutionName(String precedentInstitutionName) {
        this.precedentInstitutionName = precedentInstitutionName;
    }

    public UnitName getPrecedentInstitutionUnitName() {
        return (getPrecedentInstitution() == null) ? null : getPrecedentInstitution().getUnitName();
    }

    public void setPrecedentInstitutionUnitName(UnitName institutionUnitName) {
        setPrecedentInstitution(institutionUnitName == null ? null : institutionUnitName.getUnit());
    }

    public String getPrecedentDegreeDesignation() {
        return getPrecedentDegreeDesignationObject() != null ? getPrecedentDegreeDesignationObject()
                .getDescription() : precedentDegreeDesignation;
    }

    public void setPrecedentDegreeDesignation(String precedentDegreeDesignation) {
        this.precedentDegreeDesignation = precedentDegreeDesignation;
    }

    public void setPrecedentDegreeDesignationObject(DegreeDesignation precedentDegreeDesignationObject) {
        this.precedentDegreeDesignationObject = precedentDegreeDesignationObject;
    }

    public DegreeDesignation getPrecedentDegreeDesignationObject() {
        return precedentDegreeDesignationObject;
    }

    public Integer getNumberOfPreviousYearEnrolmentsInPrecedentDegree() {
        return numberOfPreviousYearEnrolmentsInPrecedentDegree;
    }

    public void setNumberOfPreviousYearEnrolmentsInPrecedentDegree(Integer numberOfPreviousYearEnrolmentsInPrecedentDegree) {
        this.numberOfPreviousYearEnrolmentsInPrecedentDegree = numberOfPreviousYearEnrolmentsInPrecedentDegree;
    }

    public SchoolPeriodDuration getMobilityProgramDuration() {
        return mobilityProgramDuration;
    }

    public void setMobilityProgramDuration(SchoolPeriodDuration mobilityProgramDuration) {
        this.mobilityProgramDuration = mobilityProgramDuration;
    }

//    private PrecedentDegreeInformation getPrecedentDegreeInformation() {
//        final ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(getRegistration().getDegree().getCalendar());
//        return getRegistration().getPrecedentDegreeInformation(currentExecutionYear);
//    }

    public Student getStudent() {
        return getRegistration().getStudent();
    }

//    @Atomic
//    public void updatePersonalInformation(boolean isStudentEditing) {
//        PrecedentDegreeInformation precedentInfo = getPrecedentDegreeInformation();
//        PersonalIngressionData personalData;
//
//        if (precedentInfo == null) {
//            precedentInfo = new PrecedentDegreeInformation();
//            precedentInfo.setRegistration(getRegistration());
//
//            final ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(getRegistration().getDegree().getCalendar());
//            personalData = getStudent().getPersonalIngressionDataByExecutionYear(currentExecutionYear);
//            if (personalData == null) {
//                personalData = new PersonalIngressionData(getStudent(), currentExecutionYear, precedentInfo);
//            } else {
//                personalData.addPrecedentDegreesInformations(precedentInfo);
//            }
//        } else {
//            personalData = precedentInfo.getPersonalIngressionData();
//        }
//
//        precedentInfo.edit(this, isStudentEditing);
//        personalData.edit(this);
//    }
}
