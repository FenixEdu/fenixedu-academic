package net.sourceforge.fenixedu.domain.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.ProfessionType;
import net.sourceforge.fenixedu.domain.ProfessionalSituationConditionType;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CandidacyInformationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1144682974757187722L;

    private DomainReference<Registration> registration;

    private DomainReference<Country> countryOfResidence;

    private DomainReference<DistrictSubdivision> districtSubdivisionOfResidence;

    private Boolean dislocatedFromPermanentResidence;

    private DomainReference<DistrictSubdivision> schoolTimeDistrictSubdivisionOfResidence;

    private GrantOwnerType grantOwnerType;

    private Integer numberOfCandidaciesToHigherSchool;

    private Integer numberOfFlunksOnHighSchool;

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

    private SchoolLevelType spouseSchoolLevel;

    private ProfessionType spouseProfessionType;

    private ProfessionalSituationConditionType spouseProfessionalCondition;

    private String conclusionGrade;

    private Integer conclusionYear;

    private DomainReference<Unit> institution;

    private String institutionName;

    private String degreeDesignation;

    private DomainReference<Country> countryWhereFinishedPrecedentDegree;

    private SchoolLevelType schoolLevel;

    private String otherSchoolLevel;

    public CandidacyInformationBean(final Registration registration) {
	setRegistration(registration);
	setCountryOfResidence(Country.readDefault());
	setGrantOwnerType(GrantOwnerType.STUDENT_WITHOUT_SCHOLARSHIP);
	setDislocatedFromPermanentResidence(Boolean.FALSE);
    }

    public Registration getRegistration() {
	return (this.registration != null) ? this.registration.getObject() : null;
    }

    public void setRegistration(Registration registration) {
	this.registration = (registration != null) ? new DomainReference<Registration>(registration) : null;
    }

    public Country getCountryOfResidence() {
	return (this.countryOfResidence != null) ? this.countryOfResidence.getObject() : null;
    }

    public void setCountryOfResidence(Country country) {
	this.countryOfResidence = (country != null) ? new DomainReference<Country>(country) : null;
    }

    public DistrictSubdivision getDistrictSubdivisionOfResidence() {
	return (this.districtSubdivisionOfResidence != null) ? this.districtSubdivisionOfResidence.getObject() : null;
    }

    public void setDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
	this.districtSubdivisionOfResidence = (districtSubdivision != null) ? new DomainReference<DistrictSubdivision>(
		districtSubdivision) : null;
    }

    public Boolean getDislocatedFromPermanentResidence() {
	return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
	this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public DistrictSubdivision getSchoolTimeDistrictSubdivisionOfResidence() {
	return (this.schoolTimeDistrictSubdivisionOfResidence != null) ? this.schoolTimeDistrictSubdivisionOfResidence
		.getObject() : null;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
	this.schoolTimeDistrictSubdivisionOfResidence = (districtSubdivision != null) ? new DomainReference<DistrictSubdivision>(
		districtSubdivision) : null;
    }

    public GrantOwnerType getGrantOwnerType() {
	return grantOwnerType;
    }

    public void setGrantOwnerType(GrantOwnerType grantOwnerType) {
	this.grantOwnerType = grantOwnerType;
    }

    public Integer getNumberOfCandidaciesToHigherSchool() {
	return numberOfCandidaciesToHigherSchool;
    }

    public void setNumberOfCandidaciesToHigherSchool(Integer numberOfCandidaciesToHigherSchool) {
	this.numberOfCandidaciesToHigherSchool = numberOfCandidaciesToHigherSchool;
    }

    public Integer getNumberOfFlunksOnHighSchool() {
	return numberOfFlunksOnHighSchool;
    }

    public void setNumberOfFlunksOnHighSchool(Integer numberOfFlunksOnHighSchool) {
	this.numberOfFlunksOnHighSchool = numberOfFlunksOnHighSchool;
    }

    public AcademicalInstitutionType getHighSchoolType() {
	return highSchoolType;
    }

    public void setHighSchoolType(AcademicalInstitutionType highSchoolType) {
	this.highSchoolType = highSchoolType;
    }

    public MaritalStatus getMaritalStatus() {
	return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
	this.maritalStatus = maritalStatus;
    }

    public ProfessionType getProfessionType() {
	return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
	this.professionType = professionType;
    }

    public ProfessionalSituationConditionType getProfessionalCondition() {
	return professionalCondition;
    }

    public void setProfessionalCondition(ProfessionalSituationConditionType professionalCondition) {
	this.professionalCondition = professionalCondition;
    }

    public SchoolLevelType getMotherSchoolLevel() {
	return motherSchoolLevel;
    }

    public void setMotherSchoolLevel(SchoolLevelType motherSchoolLevel) {
	this.motherSchoolLevel = motherSchoolLevel;
    }

    public ProfessionType getMotherProfessionType() {
	return motherProfessionType;
    }

    public void setMotherProfessionType(ProfessionType motherProfessionType) {
	this.motherProfessionType = motherProfessionType;
    }

    public ProfessionalSituationConditionType getMotherProfessionalCondition() {
	return motherProfessionalCondition;
    }

    public void setMotherProfessionalCondition(ProfessionalSituationConditionType motherProfessionalCondition) {
	this.motherProfessionalCondition = motherProfessionalCondition;
    }

    public SchoolLevelType getFatherSchoolLevel() {
	return fatherSchoolLevel;
    }

    public void setFatherSchoolLevel(SchoolLevelType fatherSchoolLevel) {
	this.fatherSchoolLevel = fatherSchoolLevel;
    }

    public ProfessionType getFatherProfessionType() {
	return fatherProfessionType;
    }

    public void setFatherProfessionType(ProfessionType fatherProfessionType) {
	this.fatherProfessionType = fatherProfessionType;
    }

    public ProfessionalSituationConditionType getFatherProfessionalCondition() {
	return fatherProfessionalCondition;
    }

    public void setFatherProfessionalCondition(ProfessionalSituationConditionType fatherProfessionalCondition) {
	this.fatherProfessionalCondition = fatherProfessionalCondition;
    }

    public SchoolLevelType getSpouseSchoolLevel() {
	return spouseSchoolLevel;
    }

    public void setSpouseSchoolLevel(SchoolLevelType spouseSchoolLevel) {
	this.spouseSchoolLevel = spouseSchoolLevel;
    }

    public ProfessionType getSpouseProfessionType() {
	return spouseProfessionType;
    }

    public void setSpouseProfessionType(ProfessionType spouseProfessionType) {
	this.spouseProfessionType = spouseProfessionType;
    }

    public ProfessionalSituationConditionType getSpouseProfessionalCondition() {
	return spouseProfessionalCondition;
    }

    public void setSpouseProfessionalCondition(ProfessionalSituationConditionType spouseProfessionalCondition) {
	this.spouseProfessionalCondition = spouseProfessionalCondition;
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
	return (this.institution != null) ? this.institution.getObject() : null;
    }

    public void setInstitution(Unit institution) {
	this.institution = (institution != null) ? new DomainReference<Unit>(institution) : null;
    }

    public String getInstitutionName() {
	return institutionName;
    }

    public void setInstitutionName(String institutionName) {
	this.institutionName = institutionName;
    }

    public UnitName getInstitutionUnitName() {
	return (institution == null) ? null : institution.getObject().getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institution = (institutionUnitName == null) ? null : new DomainReference<Unit>(institutionUnitName.getUnit());
    }

    public String getDegreeDesignation() {
	return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
	this.degreeDesignation = degreeDesignation;
    }

    public Country getCountryWhereFinishedPrecedentDegree() {
	return (this.countryWhereFinishedPrecedentDegree != null) ? this.countryWhereFinishedPrecedentDegree.getObject() : null;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country country) {
	this.countryWhereFinishedPrecedentDegree = (country != null) ? new DomainReference<Country>(country) : null;
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

    public boolean isValid() {
	return false;
    }
    
    public void copyInformationToCandidacy(final Registration registration){
		
    }

}
