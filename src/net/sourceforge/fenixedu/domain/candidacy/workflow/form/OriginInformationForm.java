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

    private Unit institution;

    private String institutionName;

    private DegreeDesignation raidesDegreeDesignation;

    private Country countryWhereFinishedPrecedentDegree;

    private AcademicalInstitutionType highSchoolType;

    private OriginInformationForm() {
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
	if (getSchoolLevel() != null && getSchoolLevel().isHigherEducation()) {
	    return getRaidesDegreeDesignation().getDescription();
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

    public Country getCountryWhereFinishedPrecedentDegree() {
	return this.countryWhereFinishedPrecedentDegree;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country countryWhereFinishedPrecedentDegree) {
	this.countryWhereFinishedPrecedentDegree = countryWhereFinishedPrecedentDegree;
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

    @Override
    public List<LabelFormatter> validate() {
	if (this.schoolLevel == SchoolLevelType.OTHER && StringUtils.isEmpty(this.otherSchoolLevel)) {
	    return Collections.singletonList(new LabelFormatter().appendLabel(
		    "error.candidacy.workflow.OriginInformationForm.otherSchoolLevel.must.be.filled", "application"));
	}

	return Collections.emptyList();

    }

    @Override
    public String getSchemaName() {
	String schemaName = super.getSchemaName();
	if (getSchoolLevel() != null) {
	    if (getSchoolLevel().isHigherEducation()) {
		schemaName += ".higherEducation";
	    }
	    if (getSchoolLevel().isHighSchoolOrEquivalent()) {
		schemaName += ".highSchoolOrEquivalent";
	    }
	}
	return schemaName;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.originInformationForm";
    }

    public static OriginInformationForm createFrom(final StudentCandidacy studentCandidacy) {

	final OriginInformationForm result = new OriginInformationForm();
	result.setHighSchoolType(studentCandidacy.getHighSchoolType());
	if (studentCandidacy.hasPrecedentDegreeInformation()) {
	    result.setConclusionGrade(studentCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
	    result.setDegreeDesignation(studentCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
	    result.setInstitution(studentCandidacy.getPrecedentDegreeInformation().getInstitution());
	}

	return result;

    }
}