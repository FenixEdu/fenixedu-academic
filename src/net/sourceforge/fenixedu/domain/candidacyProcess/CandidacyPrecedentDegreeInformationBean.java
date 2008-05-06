package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.joda.time.YearMonthDay;

public class CandidacyPrecedentDegreeInformationBean implements Serializable {

    private String degreeDesignation;

    private YearMonthDay conclusionDate;

    private DomainReference<Unit> institution;

    private String institutionName;

    private String conclusionGrade;

    private DomainReference<CandidacyPrecedentDegreeInformation> precedentDegreeInformation;

    public CandidacyPrecedentDegreeInformationBean() {
    }

    public CandidacyPrecedentDegreeInformationBean(final CandidacyPrecedentDegreeInformation precedentDegreeInformation) {
	setPrecedentDegreeInformation(precedentDegreeInformation);
	setDegreeDesignation(precedentDegreeInformation.getDegreeDesignation());
	setConclusionDate(precedentDegreeInformation.getConclusionDate());
	setConclusionGrade(precedentDegreeInformation.getConclusionGrade());
	setInstitutionValue(precedentDegreeInformation);
    }

    private void setInstitutionValue(final CandidacyPrecedentDegreeInformation precedentDegreeInformation) {
	institution = precedentDegreeInformation.hasInstitution() ? new DomainReference<Unit>(precedentDegreeInformation
		.getInstitution()) : null;
    }

    public String getDegreeDesignation() {
	return degreeDesignation;
    }

    public void setDegreeDesignation(String degreeDesignation) {
	this.degreeDesignation = degreeDesignation;
    }

    public YearMonthDay getConclusionDate() {
	return conclusionDate;
    }

    public void setConclusionDate(YearMonthDay conclusionDate) {
	this.conclusionDate = conclusionDate;
    }

    public UnitName getInstitutionUnitName() {
	return (institution == null) ? null : institution.getObject().getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institution = (institutionUnitName == null) ? null : new DomainReference<Unit>(institutionUnitName.getUnit());
    }

    public String getInstitutionName() {
	return institutionName;
    }

    public Unit getInstitution() {
	return getInstitutionUnitName().getUnit();
    }

    public void setInstitutionName(String institutionName) {
	this.institutionName = institutionName;
    }

    public String getConclusionGrade() {
	return conclusionGrade;
    }

    public void setConclusionGrade(String conclusionGrade) {
	this.conclusionGrade = conclusionGrade;
    }

    public void setConclusionGrade(final Integer conclusionGrade) {
	this.conclusionGrade = String.valueOf(conclusionGrade);
    }

    public CandidacyPrecedentDegreeInformation getPrecedentDegreeInformation() {
	return (this.precedentDegreeInformation != null) ? this.precedentDegreeInformation.getObject() : null;
    }

    public void setPrecedentDegreeInformation(final CandidacyPrecedentDegreeInformation precedentDegreeInformation) {
	this.precedentDegreeInformation = (precedentDegreeInformation != null) ? new DomainReference<CandidacyPrecedentDegreeInformation>(
		precedentDegreeInformation)
		: null;
    }

}
