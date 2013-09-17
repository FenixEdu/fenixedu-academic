package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExamStudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;

public class UnEnrollStudentInWrittenEvaluation extends EnrolStudentInWrittenEvaluation {

    @Override
    public void enrolmentAction(final WrittenEvaluation writtenEvaluation, final Registration registration) {
        writtenEvaluation.unEnrolStudent(registration);
    }

    // Service Invokers migrated from Berserk

    private static final UnEnrollStudentInWrittenEvaluation serviceInstance = new UnEnrollStudentInWrittenEvaluation();

    @Atomic
    public static void runUnEnrollStudentInWrittenEvaluation(String username, String writtenEvaluationOID)
            throws FenixServiceException {
        ExamStudentAuthorizationFilter.instance.execute();
        serviceInstance.run(username, writtenEvaluationOID);
    }

}