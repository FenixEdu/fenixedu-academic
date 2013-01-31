package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class CreateRectificationMarkSheet {

	@Service
	public static MarkSheet run(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation, Grade newGrade,
			Date evaluationDate, String reason, Person person) throws InvalidArgumentsServiceException {
		if (markSheet == null) {
			throw new InvalidArgumentsServiceException();
		}
		CurricularCourse curricularCourse = markSheet.getCurricularCourse();
		return curricularCourse.rectifyEnrolmentEvaluation(markSheet, enrolmentEvaluation, evaluationDate, newGrade, reason,
				person);
	}

}
