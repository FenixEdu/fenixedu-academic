package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.utils.EnrollmentPredicates;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ReadStudentCurricularPlansByPersonAndCriteria extends Service {

    public InfoStudentCurricularPlansWithSelectedEnrollments run(String username,
	    StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criteria) {

	final Predicate predicate = getPredicateBy(criteria);
	final Person person = Person.readPersonByUsername(username);

	final List<StudentCurricularPlan> studentCurricularPlans;

	if (curricularPlanID.isAll()) {
	    studentCurricularPlans = getAllStudentCurricularPlans(person);

	} else if (curricularPlanID.isNewest()) {
	    studentCurricularPlans = Collections.singletonList(getMostRecentStudentCurricularPlan(person));

	} else {
	    studentCurricularPlans = Collections.singletonList(rootDomainObject
		    .readStudentCurricularPlanByOID(curricularPlanID.getId()));
	}

	final InfoStudentCurricularPlansWithSelectedEnrollments result = new InfoStudentCurricularPlansWithSelectedEnrollments();

	for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {

	    final List<InfoEnrolment> infoSelectedEnrollments = new ArrayList<InfoEnrolment>();
	    
	    for (final Enrolment enrolment : (List<Enrolment>) CollectionUtils.select(
		    studentCurricularPlan.getEnrolmentsSet(), predicate)) {
		infoSelectedEnrollments.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }

	    result.addInfoStudentCurricularPlan(InfoStudentCurricularPlan
		    .newInfoFromDomain(studentCurricularPlan), infoSelectedEnrollments);

	}

	return result;
    }

    private StudentCurricularPlan getMostRecentStudentCurricularPlan(final Person person) {
	StudentCurricularPlan mostRecentStudentCurricularPlan = null;
	for (final StudentCurricularPlan studentCurricularPlan : getAllStudentCurricularPlans(person)) {
	    if (mostRecentStudentCurricularPlan == null
		    || mostRecentStudentCurricularPlan.getStartDateYearMonthDay().isBefore(
			    studentCurricularPlan.getStartDateYearMonthDay())) {
		mostRecentStudentCurricularPlan = studentCurricularPlan;
	    }
	}
	return mostRecentStudentCurricularPlan;
    }

    private List<StudentCurricularPlan> getAllStudentCurricularPlans(final Person person) {
	final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (final Registration registration : person.getStudentsSet()) {
	    result.addAll(registration.getStudentCurricularPlans());
	}
	return result;
    }

    private Predicate getPredicateBy(EnrollmentStateSelectionType criteria) {
	if (criteria.equals(EnrollmentStateSelectionType.APPROVED)) {
	    return EnrollmentPredicates.getApprovedPredicate();

	} else if (criteria.equals(EnrollmentStateSelectionType.NONE)) {
	    return EnrollmentPredicates.getNonePredicate();

	} else {
	    return EnrollmentPredicates.getAllPredicate();
	}
    }

}
