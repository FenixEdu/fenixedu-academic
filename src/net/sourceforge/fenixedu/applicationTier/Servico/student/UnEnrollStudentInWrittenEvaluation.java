package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.student.Registration;

public class UnEnrollStudentInWrittenEvaluation extends EnrolStudentInWrittenEvaluation {

    @Override
    public void enrolmentAction(final WrittenEvaluation writtenEvaluation, final Registration registration) {
	writtenEvaluation.unEnrolStudent(registration);
    }

}
