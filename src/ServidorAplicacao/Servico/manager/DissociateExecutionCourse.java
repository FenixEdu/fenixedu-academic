/*
 * Created on 11/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DissociateExecutionCourse implements IService {

    //	dissociate an execution course
    public void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);

            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);

            if (executionCourse == null)
                throw new NonExistingServiceException("message.nonExisting.executionCourse", null);

            List executionCourses = curricularCourse.getAssociatedExecutionCourses();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();

            if (!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
                persistentCurricularCourse.simpleLockWrite(curricularCourse);
                executionCourses.remove(executionCourse);
                curricularCourses.remove(curricularCourse);
                executionCourse.setAssociatedCurricularCourses(curricularCourses);
                curricularCourse.setAssociatedExecutionCourses(executionCourses);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}