package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;

public class CreateRectificationOldMarkSheet extends FenixService {

    public MarkSheet run(EnrolmentEvaluation enrolmentEvaluation, MarkSheetType markSheetType, Grade newGrade,
	    Date evaluationDate, String reason, Employee employee) throws InvalidArgumentsServiceException {
	if (enrolmentEvaluation == null) {
	    throw new InvalidArgumentsServiceException();
	}
	return enrolmentEvaluation.getEnrolment().getCurricularCourse().rectifyOldEnrolmentEvaluation(enrolmentEvaluation,
		markSheetType, evaluationDate, newGrade, reason, employee);
    }
}
