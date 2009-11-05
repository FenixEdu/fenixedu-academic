package net.sourceforge.fenixedu.domain.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.ProfessionType;
import net.sourceforge.fenixedu.domain.ProfessionalSituationConditionType;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StringUtils;

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

    private DomainReference<Unit> grantOwnerProvider;

    private String grantOwnerProviderName;

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

    private Double entryGrade;

    private Integer placingOption;

    private Collection<DomainReference<File>> documentFiles = new ArrayList<DomainReference<File>>();

    public CandidacyInformationBean(Registration registration) {
	setRegistration(registration);
    }

    public CandidacyInformationBean(IndividualCandidacy individualCandidacy) {
	setCountryOfResidence(individualCandidacy.getCountryOfResidence());
	setDistrictSubdivisionOfResidence(individualCandidacy.getDistrictSubdivisionOfResidence());
	setSchoolTimeDistrictSubdivisionOfResidence(individualCandidacy.getSchoolTimeDistrictSubDivisionOfResidence());
	setDislocatedFromPermanentResidence(individualCandidacy.getDislocatedFromPermanentResidence());
	setNumberOfCandidaciesToHigherSchool(individualCandidacy.getNumberOfCandidaciesToHigherSchool());
	setNumberOfFlunksOnHighSchool(individualCandidacy.getNumberOfFlunksOnHighSchool());

	setGrantOwnerType(individualCandidacy.getGrantOwnerType());
	setGrantOwnerProvider(individualCandidacy.getGrantOwnerProvider());
	setHighSchoolType(individualCandidacy.getHighSchoolType());
	setMaritalStatus(individualCandidacy.getMaritalStatus());
	setProfessionType(individualCandidacy.getProfessionType());
	setProfessionalCondition(individualCandidacy.getProfessionalCondition());

	setMotherSchoolLevel(individualCandidacy.getMotherSchoolLevel());
	setMotherProfessionType(individualCandidacy.getMotherProfessionType());
	setMotherProfessionalCondition(individualCandidacy.getMotherProfessionalCondition());

	setFatherSchoolLevel(individualCandidacy.getFatherSchoolLevel());
	setFatherProfessionType(individualCandidacy.getFatherProfessionType());
	setFatherProfessionalCondition(individualCandidacy.getFatherProfessionalCondition());

	setSpouseSchoolLevel(individualCandidacy.getSpouseSchoolLevel());
	setSpouseProfessionType(individualCandidacy.getSpouseProfessionType());
	setSpouseProfessionalCondition(individualCandidacy.getSpouseProfessionalCondition());
    }

    public CandidacyInformationBean() {
    }

    public Registration getRegistration() {
	return (this.registration != null) ? this.registration.getObject() : null;
    }

    public void setRegistration(Registration registration) {
	this.registration = (registration != null) ? new DomainReference<Registration>(registration) : null;
    }

    public boolean hasRegistration() {
	return getRegistration() != null;
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

    public Unit getGrantOwnerProvider() {
	return (this.grantOwnerProvider != null) ? this.grantOwnerProvider.getObject() : null;
    }

    public void setGrantOwnerProvider(Unit grantOwnerProvider) {
	this.grantOwnerProvider = (grantOwnerProvider != null) ? new DomainReference<Unit>(grantOwnerProvider) : null;
    }

    public String getGrantOwnerProviderName() {
	return grantOwnerProviderName;
    }

    public void setGrantOwnerProviderName(String grantOwnerProviderName) {
	this.grantOwnerProviderName = grantOwnerProviderName;
    }

    public UnitName getGrantOwnerProviderUnitName() {
	return (grantOwnerProvider == null) ? null : grantOwnerProvider.getObject().getUnitName();
    }

    public void setGrantOwnerProviderUnitName(UnitName grantOwnerProviderUnitName) {
	this.grantOwnerProvider = (grantOwnerProviderUnitName == null) ? null : new DomainReference<Unit>(
		grantOwnerProviderUnitName.getUnit());
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
	this.maritalStatus = (maritalStatus == MaritalStatus.UNKNOWN ? null : maritalStatus);
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

    public Double getEntryGrade() {
	return entryGrade;
    }

    public void setEntryGrade(Double entryGrade) {
	this.entryGrade = entryGrade;
    }

    public Integer getPlacingOption() {
	return placingOption;
    }

    public void setPlacingOption(Integer placingOption) {
	this.placingOption = placingOption;
    }

    public Set<String> validate() {

	final Set<String> result = new HashSet<String>();

	if (hasRegistration() && getRegistration().getRegistrationAgreement() != RegistrationAgreement.NORMAL
		&& getRegistration().getRegistrationAgreement() != RegistrationAgreement.TIME) {
	    return result;
	}

	if (getCountryOfResidence() == null || getGrantOwnerType() == null || getDislocatedFromPermanentResidence() == null
		|| (getMaritalStatus() == null || getMaritalStatus() == MaritalStatus.UNKNOWN)
		|| getProfessionalCondition() == null || getProfessionType() == null || getMotherSchoolLevel() == null
		|| getMotherProfessionType() == null || getMotherProfessionalCondition() == null
		|| getFatherProfessionalCondition() == null || getFatherProfessionType() == null
		|| getFatherSchoolLevel() == null || getCountryWhereFinishedPrecedentDegree() == null
		|| (getInstitution() == null && StringUtils.isEmpty(getInstitutionName()))) {
	    result.add("error.CandidacyInformationBean.required.information.must.be.filled");
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
	    result
		    .add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.is.required.for.dislocated.students");
	}

	if (getSchoolLevel() != null && getSchoolLevel() == SchoolLevelType.OTHER && StringUtils.isEmpty(getOtherSchoolLevel())) {
	    result
		    .add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.other.school.level.description.is.required");
	}

	if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
		&& getGrantOwnerProvider() == null) {
	    result
		    .add("error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
	}

	return result;

    }

    public boolean isValid() {
	return validate().isEmpty();
    }

    public void updateRegistrationWithMissingInformation() {
	getRegistration().editMissingCandidacyInformation(this);
    }

    public void updateRegistrationInformation() {
	getRegistration().editCandidacyInformation(this);
    }

    public void addDocumentFile(final File file) {
	documentFiles.add(new DomainReference<File>(file));
    }

    public Collection<File> getDocumentFiles() {
	final Collection<File> result = new ArrayList<File>();
	for (final DomainReference<File> file : documentFiles) {
	    result.add(file.getObject());
	}
	return result;
    }
    
    public String getFormattedValues() {
	Formatter result = new Formatter();

	final Student student = getRegistration().getStudent();
	result.format("Student Number: %d\n", student.getNumber());
	result.format("Name: %s\n", student.getPerson().getName());
	result.format("Degree: %s\n", getRegistration().getDegree().getPresentationName());

	return result.toString();
    }
    
}
