package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class OriginInformationForm extends Form {

    private static final Integer DEFAULT_COUNTRY_ID = Integer.valueOf(1);

    private String conclusionGrade;

    private String degreeDesignation;

    private Integer conclusionYear;

    private DomainReference<Unit> institution;

    private String institutionName;

    private DomainReference<Country> countryWhereFinishedPrecedentDegree;

    public OriginInformationForm() {
	super();
	setCountryWhereFinishedPrecedentDegree(RootDomainObject.getInstance().readCountryByOID(
		DEFAULT_COUNTRY_ID));
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

    public Country getCountryWhereFinishedPrecedentDegree() {
	return (this.countryWhereFinishedPrecedentDegree != null) ? this.countryWhereFinishedPrecedentDegree
		.getObject()
		: null;
    }

    public void setCountryWhereFinishedPrecedentDegree(Country countryWhereFinishedPrecedentDegree) {
	this.countryWhereFinishedPrecedentDegree = (countryWhereFinishedPrecedentDegree != null) ? new DomainReference<Country>(
		countryWhereFinishedPrecedentDegree)
		: null;
    }

    public Unit getInstitution() {
	return (this.institution != null) ? this.institution.getObject() : null;
    }

    public void setInstitution(Unit unit) {
	this.institution = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }

    @Override
    public List<LabelFormatter> validate() {
	return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.originInformationForm";
    }
}