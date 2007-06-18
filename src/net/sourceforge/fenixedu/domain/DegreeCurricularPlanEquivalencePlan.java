package net.sourceforge.fenixedu.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeCurricularPlanEquivalencePlan extends DegreeCurricularPlanEquivalencePlan_Base {

    public DegreeCurricularPlanEquivalencePlan(final DegreeCurricularPlan degreeCurricularPlan,
	    final DegreeCurricularPlan sourceDegreeCurricularPlan) {
	super();
	init(degreeCurricularPlan, sourceDegreeCurricularPlan);
    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan,
	    DegreeCurricularPlan sourceDegreeCurricularPlan) {
	checkParameters(degreeCurricularPlan, sourceDegreeCurricularPlan);

	super.setDegreeCurricularPlan(degreeCurricularPlan);
	super.setSourceDegreeCurricularPlan(sourceDegreeCurricularPlan);

    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, DegreeCurricularPlan sourceDegreeCurricularPlan) {
	if (degreeCurricularPlan == null) {
	    throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.degreeCurricularPlan.cannot.be.null");
	}

	if (sourceDegreeCurricularPlan == null) {
	    throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.sourceDegreeCurricularPlan.cannot.be.null");
	}

	if (degreeCurricularPlan == sourceDegreeCurricularPlan) {
	    throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.source.and.target.cannot.be.the.same");
	}
    }

    public SortedSet<CurricularCourseEquivalencePlanEntry> getOrderedCurricularCourseEntries() {
	final SortedSet<CurricularCourseEquivalencePlanEntry> entries = new TreeSet<CurricularCourseEquivalencePlanEntry>(CurricularCourseEquivalencePlanEntry.COMPARATOR_BY_OLD_CURRICULAR_COURSE_NAMES_AND_NEW_CURRICULAR_COURSE_NAMES);
	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isCurricularCourseEntry()) {
		entries.add((CurricularCourseEquivalencePlanEntry) equivalencePlanEntry);
	    }
	}
	return entries;
    }

    public SortedSet<CourseGroupEquivalencePlanEntry> getOrderedCourseGroupEntries() {
	try {
	final SortedSet<CourseGroupEquivalencePlanEntry> entries = new TreeSet<CourseGroupEquivalencePlanEntry>(CourseGroupEquivalencePlanEntry.COMPARATOR_BY_OLD_COURSE_GROUP_NAME_AND_NEW_COURSE_GROUP_NAME);
	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isCourseGroupEntry()) {
		entries.add((CourseGroupEquivalencePlanEntry) equivalencePlanEntry);
	    }
	}
	return entries;
	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Error(t);
	}
    }

}
