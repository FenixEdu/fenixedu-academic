package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CreditsPredicates {

    static public final AccessControlPredicate<Credits> DELETE = new AccessControlPredicate<Credits>() {

        @Override
        public boolean evaluate(final Credits credits) {

            boolean authorizedIfConcluded =
                    AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                            AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(
                            credits.getStudentCurricularPlan().getDegree());

            for (final Dismissal dismissal : credits.getDismissalsSet()) {
                if (dismissal.getParentCycleCurriculumGroup() != null && dismissal.getParentCycleCurriculumGroup().isConcluded()
                        && !authorizedIfConcluded) {
                    return false;
                }
            }

            return true;
        }
    };

}
