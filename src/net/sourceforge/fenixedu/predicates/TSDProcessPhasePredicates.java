package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class TSDProcessPhasePredicates {

	public static final AccessControlPredicate<TSDProcessPhase> writePredicate = new AccessControlPredicate<TSDProcessPhase>() {

		public boolean evaluate(TSDProcessPhase tsdProcessPhase) {
			TSDProcess tsdProcess = tsdProcessPhase.getTSDProcess();

			Person person = AccessControl.getPerson();

			return (tsdProcess == null) ? true : tsdProcess.getIsMemberOfAutomaticValuationGroup(person)
					|| tsdProcess.getIsMemberOfOmissionConfigurationGroup(person)
					|| tsdProcess.getIsMemberOfPhasesManagementGroup(person)
					|| tsdProcess.getIsMemberOfCompetenceCoursesAndTeachersManagementGroup(person);
		}
	};


	public static final AccessControlPredicate<TSDProcessPhase> omissionPredicate = new AccessControlPredicate<TSDProcessPhase>() {

		public boolean evaluate(TSDProcessPhase tsdProcessPhase) {
			TSDProcess tsdProcess = tsdProcessPhase.getTSDProcess();

			Person person = AccessControl.getPerson();

			return tsdProcess.getIsMemberOfOmissionConfigurationGroup(person);
		}
	};
}
