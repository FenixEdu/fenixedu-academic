package net.sourceforge.fenixedu.domain.candidacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
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
import pt.ist.fenixWebFramework.services.Service;

public class CandidacyInformationBean implements Serializable {

    static public Comparator<CandidacyInformationBean> COMPARATOR_BY_DESCRIPTION = new Comparator<CandidacyInformationBean>() {

	@Override
	public int compare(CandidacyInformationBean o1, CandidacyInformationBean o2) {
	    return o1.getDescription().compareTo(o2.getDescription());
	}
    };

    static private final long serialVersionUID = 1144682974757187722L;

    private Registration registration;
    private StudentCandidacy candidacy;
    private IndividualCandidacy individualCandidacy;

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

    private Country countryWhereFinishedPrecedentDegree;

    private SchoolLevelType schoolLevel;

    private String otherSchoolLevel;

    private Double entryGrade;

    private Integer placingOption;

    private final Collection<File> documentFiles = new ArrayList<File>();

    public CandidacyInformationBean(Registration registration) {
	setRegistration(registration);
    }

    public CandidacyInformationBean(IndividualCandidacy individualCandidacy) {
	setCountryOfResidence(individualCandidacy.getCountryOfResidence());
	setDistrictSubdivisionOfResidence(individualCandidacy.getDistrictSubdivisionOfResidence());
	setSchoolTimeDistrictSubdivisionOfResidence(individualCandidacy.getSchoolTimeDistrictSubDivisionOfResidence());
	setDislocatedFromPermanentResidence(individualCandidacy.getDislocatedFromPermanentResidence());

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
    }

    public CandidacyInformationBean() {
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

    public StudentCandidacy getCandidacy() {
	return candidacy;
    }

    public void setCandidacy(StudentCandidacy candidacy) {
	this.candidacy = candidacy;
    }

    public boolean hasCandidacy() {
	return getCandidacy() != null;
    }

    public IndividualCandidacy getIndividualCandidacy() {
	return individualCandidacy;
    }

    public void setIndividualCandidacy(IndividualCandidacy individualCandidacy) {
	this.individualCandidacy = individualCandidacy;
    }

    public boolean hasIndividualCandidacy() {
	return getIndividualCandidacy() != null;
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
	return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
	this.degreeDesignation = degreeDesignation;
    }

    public Country getCountryWhereFinishedPrecedentDegree() {
	return this.countryWhereFinishedPrecedentDegree;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country country) {
	this.countryWhereFinishedPrecedentDegree = country;
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
	    result.add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.is.required.for.dislocated.students");
	}

	if (getSchoolLevel() != null && getSchoolLevel() == SchoolLevelType.OTHER && StringUtils.isEmpty(getOtherSchoolLevel())) {
	    result.add("error.CandidacyInformationBean.schoolTimeDistrictSubdivisionOfResidence.other.school.level.description.is.required");
	}

	if (getGrantOwnerType() != null && getGrantOwnerType() == GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER
		&& getGrantOwnerProvider() == null) {
	    result.add("error.CandidacyInformationBean.grantOwnerProviderInstitutionUnitName.is.required.for.other.institution.grant.ownership");
	}

	return result;

    }

    public boolean isValid() {
	return validate().isEmpty();
    }

    @Service
    public void updateCandidacyWithMissingInformation() {
	if (hasCandidacy()) {
	    getCandidacy().editMissingCandidacyInformation(this);
	} else if (hasIndividualCandidacy()) {
	    getIndividualCandidacy().editMissingCandidacyInformation(this);
	} else {
	    getRegistration().editMissingCandidacyInformation(this);
	}
    }

    @Service
    public void updateCandidacyInformation() {
	if (hasCandidacy()) {
	    getCandidacy().editCandidacyInformation(this);
	} else if (hasIndividualCandidacy()) {
	    getIndividualCandidacy().editCandidacyInformation(this);
	} else {
	    getRegistration().editCandidacyInformation(this);
	}
    }

    public void addDocumentFile(final File file) {
	documentFiles.add(file);
    }

    public Collection<File> getDocumentFiles() {
	final Collection<File> result = new ArrayList<File>();
	for (final File file : documentFiles) {
	    result.add(file);
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

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}

	if (!(obj instanceof CandidacyInformationBean)) {
	    return false;
	}

	final CandidacyInformationBean other = (CandidacyInformationBean) obj;

	if (hasCandidacy() && other.hasCandidacy()) {
	    return getCandidacy().equals(other.getCandidacy());
	} else if (hasIndividualCandidacy() && other.hasIndividualCandidacy()) {
	    return getIndividualCandidacy().equals(other.getIndividualCandidacy());
	} else if (hasRegistration() && other.hasRegistration()) {
	    return getRegistration().equals(other.getRegistration());
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	int result = 17;

	if (hasCandidacy()) {
	    result = 37 * result + getCandidacy().hashCode();
	}

	if (hasIndividualCandidacy()) {
	    result = 37 * result + getIndividualCandidacy().hashCode();
	}

	if (hasRegistration()) {
	    result = 37 * result + getRegistration().hashCode();
	}

	return result;
    }

    public String getDescription() {
	if (hasRegistration()) {
	    return getRegistration().getDegreeDescription();

	} else if (hasCandidacy()) {
	    return getCandidacy().getDescription();

	} else {
	    return getIndividualCandidacy().getDescription();
	}
    }
}
