package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurricularCourseEquivalencePlanEntry extends CurricularCourseEquivalencePlanEntry_Base {

    protected CurricularCourseEquivalencePlanEntry() {
	super();
    }

    public CurricularCourseEquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final List<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
	this();
	init(equivalencePlan, oldCurricularCourses, newCurricularCourse);
    }

    private void init(final EquivalencePlan equivalencePlan,
	    final List<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
	super.init(equivalencePlan);
	checkParameters(oldCurricularCourses, newCurricularCourse);
	super.getOldCurricularCourses().addAll(oldCurricularCourses);
	super.setNewCurricularCourse(newCurricularCourse);
    }

    public void checkParameters(List<CurricularCourse> oldCurricularCourses,
	    CurricularCourse newCurricularCourse) {
	if (oldCurricularCourses.isEmpty()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.oldCurricularCourses.cannot.be.empty");
	}

	if (newCurricularCourse == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.newCurricularCourse.cannot.be.null");
	}

    }

}
