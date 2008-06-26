package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.joda.time.LocalDate;

public class CandidacyPrecedentDegreeInformationBean implements Serializable {

    private String degreeDesignation;

    private LocalDate conclusionDate;

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

    public CandidacyPrecedentDegreeInformationBean(final StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan.isBolonhaDegree() || !studentCurricularPlan.getRegistration().isRegistrationConclusionProcessed()) {
	    throw new IllegalArgumentException("error.studentCurricularPlan.must.be.pre.bolonha.and.concluded");
	}

	setDegreeDesignation(studentCurricularPlan.getName());
	setConclusionDate(new LocalDate(studentCurricularPlan.getRegistration().getConclusionDate()));
	setConclusionGrade(studentCurricularPlan.getRegistration().getFinalAverage());
	setInstitutionUnitName(RootDomainObject.getInstance().getInstitutionUnit().getUnitName());
    }

    public CandidacyPrecedentDegreeInformationBean(final StudentCurricularPlan studentCurricularPlan, final CycleType cycleType) {
	if (!studentCurricularPlan.isBolonhaDegree() || cycleType == null) {
	    throw new IllegalArgumentException();
	}

	setDegreeDesignation(studentCurricularPlan.getName());
	setInstitutionUnitName(RootDomainObject.getInstance().getInstitutionUnit().getUnitName());
	
	if (studentCurricularPlan.getConclusionDate(cycleType) != null) {
	    setConclusionDate(new LocalDate(studentCurricularPlan.getConclusionDate(cycleType)));
	}
	if (studentCurricularPlan.getFinalAverage(cycleType) != null) {
	    setConclusionGrade(studentCurricularPlan.getFinalAverage(cycleType));
	}
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

    public LocalDate getConclusionDate() {
	return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
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
