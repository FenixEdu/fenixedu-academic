/*
 * Created on 11/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DissociateExecutionCourse implements IService {

    //	dissociate an execution course
    public void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException {

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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