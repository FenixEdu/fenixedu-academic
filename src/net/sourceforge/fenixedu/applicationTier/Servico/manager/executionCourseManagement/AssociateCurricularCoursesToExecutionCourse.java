package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
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

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse implements IService {

    public void run(Integer executionCourseId, List curricularCourseIds) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();

            if (executionCourseId == null) {
                throw new FenixServiceException("nullExecutionCourseId");
            }

            if (curricularCourseIds != null) {
                IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, executionCourseId, true);

                if (executionCourse == null) {
                    throw new NonExistingServiceException("noExecutionCourse");
                }

                List curricularCourses = executionCourse.getAssociatedCurricularCourses();
                if (curricularCourses == null) {
                    curricularCourses = new ArrayList();
                }
                List curricularCoursesToAssociate = new ArrayList();
                Iterator iter = curricularCourseIds.iterator();
                while (iter.hasNext()) {
                    Integer curricularCourseId = (Integer) iter.next();

                    ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                            .readByOID(CurricularCourse.class, curricularCourseId);
                    if (curricularCourse == null) {
                        throw new NonExistingServiceException("noCurricularCourse");
                    }
                    List executionCourses = curricularCourse.getAssociatedExecutionCourses();
                    if (executionCourses == null) {
                        executionCourses = new ArrayList();
                    }
                    if (!curricularCourses.contains(curricularCourse)
                            && !executionCourses.contains(executionCourse)) {
                        curricularCoursesToAssociate.add(curricularCourse);
                        executionCourses.add(executionCourse);
                        curricularCourse.setAssociatedExecutionCourses(executionCourses);
                    }
                }
                curricularCourses.addAll(curricularCoursesToAssociate);
                executionCourse.setAssociatedCurricularCourses(curricularCourses);
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}