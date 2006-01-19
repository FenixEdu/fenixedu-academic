package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DeleteWrittenEvaluation implements IService {

    /**
     * @param Integer executionCourseOID
     *            used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseOID, Integer writtenEvaluationOID) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();

        final WrittenEvaluation writtenEvaluationToDelete = (WrittenEvaluation) persistentObject
                .readByOID(WrittenEvaluation.class, writtenEvaluationOID);
        if (writtenEvaluationToDelete == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        writtenEvaluationToDelete.delete();
    }

}
