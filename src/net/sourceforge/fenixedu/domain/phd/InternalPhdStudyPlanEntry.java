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

		super.setCompetenceCourse(competenceCourse);

		super.init(type, studyPlan);

	}

	@Override
	public void setCompetenceCourse(CompetenceCourse competenceCourse) {
		throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.phd.InternalPhdStudyPlanEntry.cannot.modify.competenceCourse");
	}

	@Override
	public String getCourseDescription() {
		return MessageFormat.format("{0} ({1}) - {2} ects", getCompetenceCourse().getName(), getCompetenceCourse()
				.getDepartmentUnit().getName(), getCompetenceCourse().getEctsCredits());
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
