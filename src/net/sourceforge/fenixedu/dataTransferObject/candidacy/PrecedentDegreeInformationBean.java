package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
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

    public PrecedentDegreeInformationBean() {
	super();
    }

    public PrecedentDegreeInformationBean(PrecedentDegreeInformation information) {
	precedentDegreeInformation = information;
	degreeDesignation = information.getDegreeDesignation();
	conclusionGrade = information.getConclusionGrade();
	conclusionYear = information.getConclusionYear();
	country = (information.getCountry() == null) ? null : information.getCountry();
	institution = information.getInstitution() == null ? null : information.getInstitution();
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
	return precedentDegreeInformation;
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
	if (getSchoolLevel() != null) {
	    return getSchoolLevel().isHigherEducation() ? getRaidesDegreeDesignation().getDescription() : degreeDesignation;
	} else {
	    return degreeDesignation;
	}
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

}
