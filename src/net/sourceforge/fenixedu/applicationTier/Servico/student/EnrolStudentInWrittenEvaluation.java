package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EnrolStudentInWrittenEvaluation extends Service {

    public void run(String username, Integer writtenEvaluationOID) throws FenixServiceException,
            ExcepcaoPersistencia {

        final Registration registration = Registration.readByUsername(username);

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) rootDomainObject.readEvaluationByOID(writtenEvaluationOID);
        if (writtenEvaluation == null || registration == null) {
            throw new InvalidArgumentsServiceException();
        }

        writtenEvaluation.enrolStudent(registration);
    }

}
