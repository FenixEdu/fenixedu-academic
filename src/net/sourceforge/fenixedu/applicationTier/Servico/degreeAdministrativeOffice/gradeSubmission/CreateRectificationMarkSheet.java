package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class CreateRectificationMarkSheet extends Service {
	
	public MarkSheet run(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation, String newGrade, Date evaluationDate, String reason) {
		
		CurricularCourse curricularCourse = markSheet.getCurricularCourse();
		return curricularCourse.rectifyEnrolmentEvaluation(enrolmentEvaluation, evaluationDate, newGrade, reason);
	}

}
