package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.SchoolPeriodDuration;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.raides.DegreeDesignation;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PrecedentDegreeInformationBean implements Serializable {

    private static final long serialVersionUID = 574983352972623607L;

    private PrecedentDegreeInformation precedentDegreeInformation;
    private Unit institution;
    private String institutionName;
    private String degreeDesignation;
    private DegreeDesignation raidesDegreeDesignation;
    private String conclusionGrade;
    private Integer conclusionYear;
    private SchoolLevelType schoolLevel;
    private String otherSchoolLevel;
    private Country country;

    private boolean degreeChangeOrTransferOrErasmusStudent = false;
    private SchoolLevelType precedentSchoolLevel;
    private Unit precedentInstitution;
    private String precedentInstitutionName;
    private String precedentDegreeDesignation;
    private Integer numberOfPreviousEnrolmentsInDegrees;
    private SchoolPeriodDuration mobilityProgramDuration;

    public PrecedentDegreeInformationBean() {
	super();
    }

    public PrecedentDegreeInformationBean(PrecedentDegreeInformation information) {
	setPrecedentDegreeInformation(information);
	setDegreeDesignation(information.getDegreeDesignation());
	setConclusionGrade(information.getConclusionGrade());
	setConclusionYear(information.getConclusionYear());
	setCountry(information.getCountry());
	setInstitution(information.getInstitution());
	setSchoolLevel(information.getSchoolLevel());
	setOtherSchoolLevel(information.getOtherSchoolLevel());
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
	return precedentDegreeInformation;
    }

    public void setPrecedentDegreeInformation(PrecedentDegreeInformation precedentDegreeInformation) {
	this.precedentDegreeInformation = precedentDegreeInformation;
    }

    public Unit getInstitution() {
	return institution;
    }

    public void setInstitution(Unit institution) {
	this.institution = institution;
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

    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

    public String getDegreeDesignation() {
	if (isUnitFromRaidesListMandatory()) {
	    return getRaidesDegreeDesignation() != null ? getRaidesDegreeDesignation().getDescription() : null;
	} else {
	    return degreeDesignation;
	}
    }

    public boolean isUnitFromRaidesListMandatory() {
	return getSchoolLevel() != null && getSchoolLevel().isHigherEducation() && getCountry() != null
		&& getCountry().isDefaultCountry();
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

    public void validate() {
	if (this.schoolLevel == SchoolLevelType.OTHER && StringUtils.isEmpty(this.otherSchoolLevel)) {
	    throw new DomainException("error.registration.PrecedentDegreeInformationBean.otherSchoolLevel.must.be.filled");
	}
    }

    public void setRaidesDegreeDesignation(DegreeDesignation raidesDegreeDesignation) {
	this.raidesDegreeDesignation = raidesDegreeDesignation;
    }

    public DegreeDesignation getRaidesDegreeDesignation() {
	return raidesDegreeDesignation;
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
	return precedentDegreeDesignation;
    }

    public void setPrecedentDegreeDesignation(String precedentDegreeDesignation) {
	this.precedentDegreeDesignation = precedentDegreeDesignation;
    }

    public void setNumberOfPreviousEnrolmentsInDegrees(Integer numberOfPreviousEnrolmentsInDegrees) {
	this.numberOfPreviousEnrolmentsInDegrees = numberOfPreviousEnrolmentsInDegrees;
    }

    public Integer getNumberOfPreviousEnrolmentsInDegrees() {
	return numberOfPreviousEnrolmentsInDegrees;
    }

    public SchoolPeriodDuration getMobilityProgramDuration() {
	return mobilityProgramDuration;
    }

    public void setMobilityProgramDuration(SchoolPeriodDuration mobilityProgramDuration) {
	this.mobilityProgramDuration = mobilityProgramDuration;
    }
}
