package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EquivalencePlanEntry extends EquivalencePlanEntry_Base {

    protected EquivalencePlanEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final List<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
	this();

	init(equivalencePlan, oldCurricularCourses, newCurricularCourse);
    }

    private void init(EquivalencePlan equivalencePlan, List<CurricularCourse> oldCurricularCourses,
	    CurricularCourse newCurricularCourse) {
	checkParameters(equivalencePlan, oldCurricularCourses, newCurricularCourse);

	super.setEquivalencePlan(equivalencePlan);
	super.getOldCurricularCourses().addAll(oldCurricularCourses);
	super.setNewCurricularCourse(newCurricularCourse);

    }

    private void checkParameters(EquivalencePlan equivalencePlan,
	    List<CurricularCourse> oldCurricularCourses, CurricularCourse newCurricularCourse) {
	if (equivalencePlan == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.equivalencePlan.cannot.be.null");
	}

	if (oldCurricularCourses.isEmpty()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.oldCurricularCourses.cannot.be.empty");
	}

	if (newCurricularCourse == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.newCurricularCourse.cannot.be.null");
	}
    }

}
