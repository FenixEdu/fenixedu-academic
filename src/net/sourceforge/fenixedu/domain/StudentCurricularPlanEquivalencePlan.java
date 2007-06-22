package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
	super();

	init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan oldStudentCurricularPlan) {
	checkParameters(oldStudentCurricularPlan);

	super.setOldStudentCurricularPlan(oldStudentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan oldStudentCurricularPlan) {
	if (oldStudentCurricularPlan == null) {
	    throw new DomainException(
		    "error.StudentCurricularPlanEquivalencePlan.oldStudentCurricularPlan.cannot.be.null");
	}
    }

    public Set<EquivalencePlanEntry> getEntriesToRemove(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> result = new HashSet<EquivalencePlanEntry>();
	for (final EquivalencePlanEntry entry : getEntriesToRemoveSet()) {
	    if (entry.hasAnyDestinationDegreeModuleFor(degreeCurricularPlan)) {
		result.add(entry);
	}
	}
	return result;
    }

    public Set<EquivalencePlanEntry> getEntries(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> result = new HashSet<EquivalencePlanEntry>();
	for (final EquivalencePlanEntry entry : getEntriesSet()) {
	    if (entry.hasAnyDestinationDegreeModuleFor(degreeCurricularPlan)) {
		result.add(entry);
	    }
	}
	return result;
    }

    public Set<EquivalencePlanEntry> getEquivalencePlanEntries(final DegreeCurricularPlan degreeCurricularPlan) {
	final Set<EquivalencePlanEntry> equivalencePlanEntries = new HashSet<EquivalencePlanEntry>();
	equivalencePlanEntries.addAll(degreeCurricularPlan.getEquivalencePlan().getEntriesSet());
	equivalencePlanEntries.removeAll(getEntriesToRemoveSet());
	for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
	    if (equivalencePlanEntry.isFor(degreeCurricularPlan)) {
		equivalencePlanEntries.add(equivalencePlanEntry);
	    }
	}
	return equivalencePlanEntries;
    }
}
