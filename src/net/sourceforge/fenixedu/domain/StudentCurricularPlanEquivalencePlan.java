package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
	super();

	init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan studentCurricularPlan) {
	checkParameters(studentCurricularPlan);

	super.setStudentCurricularPlan(studentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.StudentCurricularPlanEquivalencePlan.studentCurricularPlan.cannot.be.null");
	}
    }
    
    public Set<EquivalencePlanEntry> getAllEquivalencePlanEntries() {
	final Set<EquivalencePlanEntry> result = new HashSet<EquivalencePlanEntry>();
	result.addAll(getEntriesSet());
	result.addAll(getStudentCurricularPlan().getDegreeCurricularPlan().getEquivalencePlan().getEntriesSet());
	result.removeAll(getEntriesToRemoveSet());
	return result;
    }

}
