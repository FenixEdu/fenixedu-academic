package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteWrittenEvaluation extends Service {

    /**
     * @param Integer executionCourseOID
     *            used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseOID, Integer writtenEvaluationOID) throws FenixServiceException, ExcepcaoPersistencia {
        final WrittenEvaluation writtenEvaluationToDelete = (WrittenEvaluation) persistentObject
                .readByOID(WrittenEvaluation.class, writtenEvaluationOID);
        if (writtenEvaluationToDelete == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }

        writtenEvaluationToDelete.delete();
    }

}
