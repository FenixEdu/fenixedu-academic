/*
 * Created on 9/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
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
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class AssociateExecutionCourseToCurricularCourse implements IService {

    public void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId)
            throws FenixServiceException {

        try {

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse;

            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);

            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

            List executionCourses = curricularCourse.getAssociatedExecutionCourses();
            if (executionCourses == null) {
                executionCourses = new ArrayList();
            }
            Iterator iter = executionCourses.iterator();

            while (iter.hasNext()) {
                Integer associatedExecutionPeriodId = ((IExecutionCourse) iter.next())
                        .getExecutionPeriod().getIdInternal();
                if (associatedExecutionPeriodId.equals(executionPeriodId))
                    throw new ExistingServiceException("message.unavailable.execution.period", null);
            }

            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
            }
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            if (!executionCourses.contains(executionCourse)
                    && !curricularCourses.contains(curricularCourse)) {
                persistentCurricularCourse.simpleLockWrite(curricularCourse);
                executionCourses.add(executionCourse);
                curricularCourses.add(curricularCourse);
                curricularCourse.setAssociatedExecutionCourses(executionCourses);
                executionCourse.setAssociatedCurricularCourses(curricularCourses);

            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}