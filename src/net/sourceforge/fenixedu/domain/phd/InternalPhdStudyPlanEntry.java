package net.sourceforge.fenixedu.domain.phd;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InternalPhdStudyPlanEntry extends InternalPhdStudyPlanEntry_Base {

    protected InternalPhdStudyPlanEntry() {
	super();
    }

    public InternalPhdStudyPlanEntry(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, CompetenceCourse competenceCourse) {
	this();
	init(type, studyPlan, competenceCourse);
    }

    protected void init(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, CompetenceCourse competenceCourse) {

	check(competenceCourse, "error.net.sourceforge.fenixedu.domain.phd.enclosing_type.competenceCourse.cannot.be.null");

	checkRulesToCreate(type, studyPlan, competenceCourse);

	super.setCompetenceCourse(competenceCourse);

	super.init(type, studyPlan);

    }

    private void checkRulesToCreate(PhdStudyPlanEntryType type, PhdStudyPlan studyPlan, CompetenceCourse competenceCourse) {
	if (type == PhdStudyPlanEntryType.NORMAL && !studyPlan.getDegree().isEmpty()
		&& !studyPlan.getDegree().getLastActiveDegreeCurricularPlan().getCompetenceCourses().contains(competenceCourse)) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.phd.InternalPhdStudyPlanEntry.competenceCourse.must.belong.to.study.plan.degree");
	}
    }

    @Override
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.phd.InternalPhdStudyPlanEntry.cannot.modify.competenceCourse");
    }

    @Override
    public String getCourseDescription() {
	return MessageFormat.format("{0} ({1})", getCompetenceCourse().getName(), getCompetenceCourse().getDepartmentUnit()
		.getName());
    }

    @Override
    public boolean isInternalEntry() {
	return true;
    }

    @Override
    public boolean isSimilar(PhdStudyPlanEntry entry) {
	if (entry.isInternalEntry()) {
	    return ((InternalPhdStudyPlanEntry) entry).getCompetenceCourse() == getCompetenceCourse();
	}

	return false;
    }

    @Override
    public void delete() {
	super.setCompetenceCourse(null);

	super.delete();
    }

}
