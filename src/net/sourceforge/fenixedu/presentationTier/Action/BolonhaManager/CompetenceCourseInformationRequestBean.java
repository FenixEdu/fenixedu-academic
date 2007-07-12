package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;

public class CompetenceCourseInformationRequestBean implements Serializable {

    private DomainReference<CompetenceCourse> competenceCourse;

    private DomainReference<ExecutionPeriod> executionPeriod;

    private String justification;

    private String name;
    
    private String nameEn;
    
    private RegimeType regime;

    private String objectives;

    private String objectivesEn;

    private String program;

    private String programEn;

    private String evaluationMethod;

    private String evaluationMethodEn;

    private CompetenceCourseLevel competenceCourseLevel;

    private BibliographicReferences references;
    
    public CompetenceCourseInformationRequestBean(CompetenceCourseInformationChangeRequest request) {
	setCompetenceCourse(request.getCompetenceCourse());
	setRegime(request.getRegime());
	setObjectives(request.getObjectives());
	setObjectivesEn(request.getObjectivesEn());
	setProgram(request.getProgram());
	setProgramEn(request.getProgramEn());
	setEvaluationMethod(request.getEvaluationMethod());
	setEvaluationMethodEn(request.getEvaluationMethodEn());
	setCompetenceCourseLevel(request.getCompetenceCourseLevel());
	setExecutionPeriod(request.getExecutionPeriod());
	setReferences(request.getBibliographicReferences());
	setName(request.getName());
	setNameEn(request.getNameEn());
    }
    
    public CompetenceCourseInformationRequestBean(CompetenceCourseInformation information) {
	setCompetenceCourse(information.getCompetenceCourse());
	setRegime(information.getRegime());
	setObjectives(information.getObjectives());
	setObjectivesEn(information.getObjectivesEn());
	setProgram(information.getProgram());
	setProgramEn(information.getProgramEn());
	setEvaluationMethod(information.getEvaluationMethod());
	setEvaluationMethodEn(information.getEvaluationMethodEn());
	setCompetenceCourseLevel(information.getCompetenceCourseLevel());
	setExecutionPeriod(information.getExecutionPeriod());
	setReferences(information.getBibliographicReferences());
	setName(information.getName());
	setNameEn(information.getNameEn());
    }

    public CompetenceCourseInformationRequestBean(CompetenceCourse course, ExecutionPeriod period) {
	setExecutionPeriod(period);
	setCompetenceCourse(course);	
    }
    
    public CompetenceCourseInformationRequestBean() {
	this(null,null);
    }

    public boolean isCompetenceCourseDefinedForExecutionPeriod() {
	if (getCompetenceCourse() != null && getExecutionPeriod() != null) {
	    return getCompetenceCourse().isCompetenceCourseInformationDefinedAtExecutionPeriod(getExecutionPeriod());
	}
	return false;
    }
    
    public ExecutionPeriod getExecutionPeriod() {
	return executionPeriod.getObject();
    }

    public void setExecutionPeriod(ExecutionPeriod period) {
	executionPeriod = new DomainReference<ExecutionPeriod>(period);
    }

    public CompetenceCourse getCompetenceCourse() {
	return competenceCourse.getObject();
    }

    public void setCompetenceCourse(CompetenceCourse course) {
	competenceCourse = new DomainReference<CompetenceCourse>(course);
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
	return competenceCourseLevel;
    }

    public void setCompetenceCourseLevel(CompetenceCourseLevel competenceCourseLevel) {
	this.competenceCourseLevel = competenceCourseLevel;
    }

    public String getEvaluationMethod() {
	return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
	this.evaluationMethod = evaluationMethod;
    }

    public String getEvaluationMethodEn() {
	return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
	this.evaluationMethodEn = evaluationMethodEn;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }

    public String getObjectives() {
	return objectives;
    }

    public void setObjectives(String objectives) {
	this.objectives = objectives;
    }

    public String getObjectivesEn() {
	return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
	this.objectivesEn = objectivesEn;
    }

    public String getProgram() {
	return program;
    }

    public void setProgram(String program) {
	this.program = program;
    }

    public String getProgramEn() {
	return programEn;
    }

    public void setProgramEn(String programEn) {
	this.programEn = programEn;
    }

    public RegimeType getRegime() {
	return regime;
    }

    public void setRegime(RegimeType regime) {
	this.regime = regime;
    }

    public void update(CompetenceCourseInformation information) {
	setObjectives(information.getObjectives());
	setObjectivesEn(information.getObjectivesEn());
	setProgram(information.getProgram());
	setProgramEn(information.getProgramEn());
	setEvaluationMethod(information.getEvaluationMethod());
	setEvaluationMethodEn(information.getEvaluationMethodEn());
	setCompetenceCourseLevel(information.getCompetenceCourseLevel());
	setReferences(information.getBibliographicReferences());
	setName(information.getName());
	setNameEn(information.getNameEn());
    }
    
    public void reset() {
	setObjectives(null);
	setObjectivesEn(null);
	setProgram(null);
	setProgramEn(null);
	setEvaluationMethod(null);
	setEvaluationMethodEn(null);
	setCompetenceCourseLevel(null);
	setReferences(null);
	setName(null);
	setNameEn(null);
    }

    public BibliographicReferences getReferences() {
	if (references == null) {
	    references = new BibliographicReferences();
	}
	return references;
    }

    public void setReferences(BibliographicReferences references) {
        this.references = references;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
