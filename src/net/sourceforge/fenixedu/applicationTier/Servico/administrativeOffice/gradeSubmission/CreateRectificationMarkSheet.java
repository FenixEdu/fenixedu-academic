package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class CreateRectificationMarkSheet extends Service {
	
	public MarkSheet run(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation, Grade newGrade, Date evaluationDate, String reason, Employee employee) throws InvalidArgumentsServiceException {
		if(markSheet == null) {
			throw new InvalidArgumentsServiceException();
		}
		CurricularCourse curricularCourse = markSheet.getCurricularCourse();
		return curricularCourse.rectifyEnrolmentEvaluation(markSheet, enrolmentEvaluation, evaluationDate, newGrade, reason, employee);
	}

}
