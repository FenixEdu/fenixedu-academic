package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;

public class PhdStudyPlanEntryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Degree degree;

    private List<CompetenceCourse> competenceCourses;

    private String courseName;

    private PhdStudyPlanEntryType entryType;

    private PhdStudyPlan studyPlan;

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
        return this.studyPlan;
    }

    public void setStudyPlan(PhdStudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<CompetenceCourse> getCompetenceCourses() {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse each : this.competenceCourses) {
            result.add(each);
        }

        return result;
    }

    public void setCompetenceCourses(List<CompetenceCourse> competenceCourses) {
        final List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
        for (final CompetenceCourse each : competenceCourses) {
            result.add(each);
        }

        this.competenceCourses = result;
    }

}
