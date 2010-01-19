package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.IFormation;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.util.StringUtils;

public class FormationBean implements Serializable, IFormation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final int FIRST_YEAR = 1933;

    private String formationBeginYear;
    private String formationEndYear;

    private DomainReference<Formation> formation;

    private String designation;

    private Boolean concluded;

    private String institutionName;
    private DomainReference<Unit> institutionUnit;

    private String id;

    private String conclusionGrade;

    private DomainReference<ExecutionYear> conclusionExecutionYear;

    public FormationBean(Boolean hasConcluded) {
	this.id = UUID.randomUUID().toString();
	this.concluded = hasConcluded;
	setConclusionGrade("");
    }

    public FormationBean(Formation formation) {
	this.id = UUID.randomUUID().toString();
	this.setFormation(formation);
	this.setFormationBeginYear(formation.getBeginYear());
	this.setFormationEndYear(formation.getYear());
	this.setDesignation(formation.getDesignation());
	this.setConcluded(formation.getConcluded());
	setInstitutionUnit(formation.getInstitution());
	setInstitutionName(formation.getInstitution().getName());
	setConclusionGrade(formation.getConclusionGrade());
	setConclusionExecutionYear(formation.getConclusionExecutionYear());
    }

    public String getFormationBeginYear() {
	return formationBeginYear;
    }

    public void setFormationBeginYear(String formationBeginYear) {
	this.formationBeginYear = formationBeginYear;
    }

    public String getFormationEndYear() {
	return formationEndYear;
    }

    public void setFormationEndYear(String formationEndYear) {
	this.formationEndYear = formationEndYear;
    }

    public Formation getFormation() {
	return formation != null ? formation.getObject() : null;
    }

    public void setFormation(Formation formation) {
	this.formation = formation != null ? new DomainReference<Formation>(formation) : null;
    }

    public int getFirstYear() {
	return FIRST_YEAR;
    }

    public int getLastYear() {
	return Integer.valueOf(ExecutionYear.readLastExecutionYear().getYear()).intValue();
    }

    public String getDesignation() {
	return this.designation;
    }

    public void setDesignation(String designation) {
	this.designation = designation;
    }

    public Boolean isConcluded() {
	return this.concluded;
    }

    public void setConcluded(Boolean hasConcluded) {
	this.concluded = hasConcluded;
    }

    public String getInstitutionName() {
	return this.institutionName;
    }

    public void setInstitutionName(String value) {
	this.institutionName = value;
    }

    public Unit getInstitutionUnit() {
	return this.institutionUnit != null ? this.institutionUnit.getObject() : null;
    }

    public void setInstitutionUnit(Unit unit) {
	this.institutionUnit = unit != null ? new DomainReference<Unit>(unit) : null;
    }

    public UnitName getInstitutionUnitName() {
	return (institutionUnit == null) ? null : institutionUnit.getObject().getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institutionUnit = (institutionUnitName == null) ? null : new DomainReference<Unit>(institutionUnitName.getUnit());
    }

    public String getId() {
	return this.id;
    }

    public String getConclusionGrade() {
	return this.conclusionGrade;
    }

    public void setConclusionGrade(String value) {
	this.conclusionGrade = value;
    }

    public ExecutionYear getConclusionExecutionYear() {
	return this.conclusionExecutionYear != null ? this.conclusionExecutionYear.getObject() : null;
    }

    public void setConclusionExecutionYear(ExecutionYear executionYear) {
	this.conclusionExecutionYear = executionYear != null ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public Boolean validate(Set<String> designationErrorSet, Set<String> institutionErrorSet, Set<String> durationErrorSet) {

	if (StringUtils.isEmpty(this.designation)) {
	    designationErrorSet.add("error.formation.designation.required");
	}

	if (StringUtils.isEmpty(this.formationBeginYear)) {
	    durationErrorSet.add("error.formation.begin.year.required");
	}

	if (this.institutionUnit == null) {
	    institutionErrorSet.add("error.formation.institution.unit.required");
	}

	if (StringUtils.isEmpty(this.formationEndYear)) {
	    durationErrorSet.add("error.formation.end.year.required");
	}

	if (!StringUtils.isEmpty(this.formationBeginYear) && !StringUtils.isEmpty(this.formationEndYear)) {
	    int beginYear = Integer.parseInt(this.formationBeginYear);
	    int endYear = Integer.parseInt(this.formationEndYear);

	    if (endYear < beginYear) {
		durationErrorSet.add("error.formation.end.year.greater.than.begin.year");
	    }
	}

	return !designationErrorSet.isEmpty() || institutionErrorSet.isEmpty() || !durationErrorSet.isEmpty();
    }

}
