package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.SchoolLevelType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.apache.commons.lang.StringUtils;

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

    private DomainReference<Unit> institution;

    private String institutionName;

    private DomainReference<Country> countryWhereFinishedPrecedentDegree;

    private Integer numberOfCandidaciesToHigherSchool;

    private Integer numberOfFlunksOnHighSchool;

    private AcademicalInstitutionType highSchoolType;

    public OriginInformationForm() {
	super();
	setCountryWhereFinishedPrecedentDegree(Country.readDefault());
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

    public String getDegreeDesignation() {
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
	return (institution == null) ? null : institution.getObject().getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institution = (institutionUnitName == null) ? null : new DomainReference<Unit>(institutionUnitName.getUnit());
    }

    public Country getCountryWhereFinishedPrecedentDegree() {
	return (this.countryWhereFinishedPrecedentDegree != null) ? this.countryWhereFinishedPrecedentDegree.getObject() : null;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country countryWhereFinishedPrecedentDegree) {
	this.countryWhereFinishedPrecedentDegree = (countryWhereFinishedPrecedentDegree != null) ? new DomainReference<Country>(
		countryWhereFinishedPrecedentDegree) : null;
    }

    public Unit getInstitution() {
	return (this.institution != null) ? this.institution.getObject() : null;
    }

    public void setInstitution(Unit unit) {
	this.institution = (unit != null) ? new DomainReference<Unit>(unit) : null;
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

    @Override
    public List<LabelFormatter> validate() {
	if (this.schoolLevel == SchoolLevelType.OTHER && StringUtils.isEmpty(this.otherSchoolLevel)) {
	    return Collections.singletonList(new LabelFormatter().appendLabel(
		    "error.candidacy.workflow.OriginInformationForm.otherSchoolLevel.must.be.filled", "application"));
	}

	return Collections.emptyList();

    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.originInformationForm";
    }
}