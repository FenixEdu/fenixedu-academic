/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteEvaluation implements IService {

    /**
     * @param Integer executionCourseID used in filtering
     *        (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseID, Integer evaluationID) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IEvaluation evaluation = (IEvaluation) persistenceSupport.getIPersistentObject()
                .readByOID(Evaluation.class, evaluationID);
        if (evaluation == null) {
            throw new FenixServiceException("error.noEvaluation");
        }
        evaluation.delete();
    }
}
