package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;

public class PhdStudyPlanEntryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<Degree> degree;

    private DomainReference<CompetenceCourse> competenceCourse;

    private String courseName;

    private PhdStudyPlanEntryType entryType;

    private DomainReference<PhdStudyPlan> studyPlan;

    private Boolean internalEntry;

    public PhdStudyPlanEntryBean(PhdStudyPlan studyPlan) {
	setStudyPlan(studyPlan);
	this.internalEntry = true;
	this.entryType = PhdStudyPlanEntryType.NORMAL;

    }

    public String getCourseName() {
	return courseName;
    }

    public void setCourseName(String courseName) {
	this.courseName = courseName;
    }

    public PhdStudyPlanEntryType getEntryType() {
	return entryType;
    }

    public void setEntryType(PhdStudyPlanEntryType entryType) {
	this.entryType = entryType;
    }

    public CompetenceCourse getCompetenceCourse() {
	return (this.competenceCourse != null) ? this.competenceCourse.getObject() : null;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
	this.competenceCourse = (competenceCourse != null) ? new DomainReference<CompetenceCourse>(competenceCourse) : null;
    }

    public Boolean getInternalEntry() {
	return internalEntry;
    }

    public void setInternalEntry(Boolean internalEntry) {
	this.internalEntry = internalEntry;
    }

    public PhdStudyPlan getStudyPlan() {
	return (this.studyPlan != null) ? this.studyPlan.getObject() : null;
    }

    public void setStudyPlan(PhdStudyPlan studyPlan) {
	this.studyPlan = (studyPlan != null) ? new DomainReference<PhdStudyPlan>(studyPlan) : null;
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

}
