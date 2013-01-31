package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesStudentExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RegisterStudentInquiryResponseIntention extends FenixService {

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static void run(final Person person, final Boolean dontWantToRespond) {
		final Student student = person.getStudent();
		final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod = getInquiriesStudentExecutionPeriod(student);
		inquiriesStudentExecutionPeriod.setDontWantToRespond(dontWantToRespond);
	}

	private static InquiriesStudentExecutionPeriod getInquiriesStudentExecutionPeriod(final Student student) {
		for (final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod : student
				.getInquiriesStudentExecutionPeriodsSet()) {
			if (inquiriesStudentExecutionPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
				return inquiriesStudentExecutionPeriod;
			}
		}
		final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
		return new InquiriesStudentExecutionPeriod(student, executionSemester);
	}

}