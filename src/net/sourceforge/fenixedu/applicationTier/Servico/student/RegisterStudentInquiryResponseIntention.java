package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesStudentExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.PeriodState;

public class RegisterStudentInquiryResponseIntention extends Service {

    public void run(final Person person, final Boolean dontWantToRespond) {
	final Student student = person.getStudent();
	final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod = getInquiriesStudentExecutionPeriod(student);
	inquiriesStudentExecutionPeriod.setDontWantToRespond(dontWantToRespond);
    }

    private InquiriesStudentExecutionPeriod getInquiriesStudentExecutionPeriod(final Student student) {
	for (final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod : student.getInquiriesStudentExecutionPeriodsSet()) {
	    if (inquiriesStudentExecutionPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		return inquiriesStudentExecutionPeriod;
	    }
	}
	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionPeriod();
	return new InquiriesStudentExecutionPeriod(student, executionSemester);
    }

}