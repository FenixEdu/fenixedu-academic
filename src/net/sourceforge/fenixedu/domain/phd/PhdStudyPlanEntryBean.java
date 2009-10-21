package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;

public class PhdStudyPlanEntryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<Degree> degree;

    private List<DomainReference<CompetenceCourse>> competenceCourses;

    private String courseName;

    private PhdStudyPlanEntryType entryType;

    private DomainReference<PhdStudyPlan> studyPlan;

    private Boolean internalEntry;

    public PhdStudyPlanEntryBean(PhdStudyPlan studyPlan) {
	setStudyPlan(studyPlan);
	setInternalEntry(true);
	setEntryType(PhdStudyPlanEntryType.NORMAL);
	setCompetenceCourses(new ArrayList<CompetenceCourse>());
	setDegree(!studyPlan.getDegree().isEmpty() ? studyPlan.getDegree() : null);

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

    public List<CompetenceCourse> getCompetenceCourses() {
	final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
	for (final DomainReference<CompetenceCourse> each : this.competenceCourses) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setCompetenceCourses(List<CompetenceCourse> competenceCourses) {
	final List<DomainReference<CompetenceCourse>> result = new ArrayList<DomainReference<CompetenceCourse>>();
	for (final CompetenceCourse each : competenceCourses) {
	    result.add(new DomainReference<CompetenceCourse>(each));
	}

	this.competenceCourses = result;
    }

}
