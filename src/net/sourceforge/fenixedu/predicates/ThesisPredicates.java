package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ThesisPredicates {

    public static final AccessControlPredicate<Thesis> waitingConfirmation = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(Thesis thesis) {
	    return thesis.isWaitingConfirmation();
	}

    };

    public static final AccessControlPredicate<Thesis> isScientificCouncil = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(Thesis thesis) {
	    return new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL).isMember(AccessControl.getPerson());
	}

    };

    public static final AccessControlPredicate<Thesis> isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(Thesis thesis) {
	    return isScientificCouncil.evaluate(thesis) || thesis.isCoordinatorAndNotOrientator();
	}

    };

    public static final AccessControlPredicate<Thesis> student = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(Thesis thesis) {
	    Person person = AccessControl.getPerson();

	    return person.getStudent() == thesis.getStudent() && thesis.isWaitingConfirmation();
	}

    };

    public static final AccessControlPredicate<Thesis> studentOrAcademicAdministrativeOfficeOrScientificCouncil = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(Thesis thesis) {
	    Person person = AccessControl.getPerson();
	    return (person.getStudent() == thesis.getStudent() && thesis.isWaitingConfirmation())
		    || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || person.hasRole(RoleType.SCIENTIFIC_COUNCIL);
	}

    };

    public static final AccessControlPredicate<Thesis> isScientificCommission = new AccessControlPredicate<Thesis>() {

	public boolean evaluate(final Thesis thesis) {
	    final Enrolment enrolment = thesis.getEnrolment();
	    final ExecutionYear executionYear = enrolment.getExecutionYear();
	    final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
	    return degreeCurricularPlan.isScientificCommissionMember(executionYear);
	}

    };

}
