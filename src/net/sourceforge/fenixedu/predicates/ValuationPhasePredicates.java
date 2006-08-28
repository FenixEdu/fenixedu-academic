package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.AccessControlPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class ValuationPhasePredicates {

	public static final AccessControlPredicate<ValuationPhase> writePredicate = new AccessControlPredicate<ValuationPhase>() {

		public boolean evaluate(ValuationPhase valuationPhase) {
			TeacherServiceDistribution teacherServiceDistribution = valuationPhase.getTeacherServiceDistribution();

			Person person = AccessControl.getUserView().getPerson();

			return teacherServiceDistribution.getIsMemberOfAutomaticValuationGroup(person)
					|| teacherServiceDistribution.getIsMemberOfOmissionConfigurationGroup(person)
					|| teacherServiceDistribution.getIsMemberOfPhasesManagementGroup(person)
					|| teacherServiceDistribution.getIsMemberOfValuationCompetenceCoursesAndTeachersManagementGroup(person);
		}
	};


	public static final AccessControlPredicate<ValuationPhase> omissionPredicate = new AccessControlPredicate<ValuationPhase>() {

		public boolean evaluate(ValuationPhase valuationPhase) {
			TeacherServiceDistribution teacherServiceDistribution = valuationPhase.getTeacherServiceDistribution();

			Person person = AccessControl.getUserView().getPerson();

			return teacherServiceDistribution.getIsMemberOfOmissionConfigurationGroup(person);
		}
	};
}
