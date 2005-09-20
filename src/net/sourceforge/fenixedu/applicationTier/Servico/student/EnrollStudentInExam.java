package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 */

public class EnrollStudentInExam implements IService {

    public Boolean run(String username, Integer examId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IStudent student = persistentStudent.readByUsername(username);
        IPersistentExam persistentExam = sp.getIPersistentExam();
        IPersistentWrittenEvaluationEnrolment persistentWrittenEvaluationEnrolment = sp.getIPersistentWrittenEvaluationEnrolment();
        IExam exam = (IExam) persistentExam.readByOID(Exam.class, examId, true);
        if (exam == null || student == null) {
            throw new InvalidArgumentsServiceException();
        }

        IWrittenEvaluationEnrolment writtenEvaluationEnrolment = persistentWrittenEvaluationEnrolment.readBy(exam.getIdInternal(),
                student.getIdInternal());
        if (writtenEvaluationEnrolment != null) {
            throw new ExistingServiceException();
        }
        writtenEvaluationEnrolment = DomainFactory.makeWrittenEvaluationEnrolment();
        writtenEvaluationEnrolment.setWrittenEvaluation(exam);
        writtenEvaluationEnrolment.setStudent(student);

        return new Boolean(true);
    }

}
