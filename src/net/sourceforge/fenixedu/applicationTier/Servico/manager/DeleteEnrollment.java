/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteEnrollment extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer studentNumber, final DegreeType degreeType, final Integer enrollmentId) {
	for (Registration registration : Registration.readByNumberAndDegreeType(studentNumber, degreeType)) {
	    final Enrolment enrollment = registration.findEnrolmentByEnrolmentID(enrollmentId);
	    if (enrollment != null) {
		for (EnrolmentEvaluation evaluation : enrollment.getEvaluations()) {
		    evaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		}

		final CurriculumGroup parentCurriculumGroup = enrollment.getCurriculumGroup();

		enrollment.delete();

		if (parentCurriculumGroup != null && parentCurriculumGroup.canBeDeleted()) {
		    parentCurriculumGroup.delete();
		}

		return;
	    }
	}
    }

}