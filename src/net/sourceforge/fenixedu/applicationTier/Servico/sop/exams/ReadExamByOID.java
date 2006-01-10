package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamByOID implements IService {

    public InfoExam run(Integer examID) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final Exam exam = (Exam) persistentSupport.getIPersistentExam().readByOID(Exam.class, examID);
        if (exam == null) {
            throw new FenixServiceException("error.noExam");
        }
        return InfoExam.newInfoFromDomain(exam);
    }
}