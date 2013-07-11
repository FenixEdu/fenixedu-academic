package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalPhdStudyPlanEntry extends ExternalPhdStudyPlanEntry_Base {

    protected ExternalPhdStudyPlanEntry() {
        super();
    }

    public ExternalPhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, String courseName) {
        this();
        init(type, studyPlan, courseName);
    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, String courseName) {

        String[] args = {};
        if (courseName == null || courseName.isEmpty()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.ExternalPhdStudyPlanEntry.courseName.cannot.be.null", args);
        }

        super.setCourseName(courseName);

        super.init(type, studyPlan);

    }

    @Override
    public void setCourseName(String courseName) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ExternalPhdStudyPlanEntry.cannot.modify.courseName");
    }

    @Override
    public boolean isExternalEntry() {
        return true;
    }

    @Override
    public boolean isSimilar(PhdStudyPlanEntry entry) {
        if (entry.isExternalEntry()) {
            final ExternalPhdStudyPlanEntry externalPhdStudyPlanEntry = (ExternalPhdStudyPlanEntry) entry;
            return getCourseName().equals(externalPhdStudyPlanEntry.getCourseName());
        }

        return false;

    }

    @Override
    public String getCourseDescription() {
        return getCourseName();
    }

    @Deprecated
    public boolean hasCourseName() {
        return getCourseName() != null;
    }

}
