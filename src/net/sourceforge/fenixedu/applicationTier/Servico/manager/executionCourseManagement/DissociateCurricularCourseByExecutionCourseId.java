package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

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
 * @author Fernanda Quitério 23/Dez/2003
 */

public class DissociateCurricularCourseByExecutionCourseId implements IService {

    public DissociateCurricularCourseByExecutionCourseId() {

    }

    public Object run(Integer executionCourseId, Integer curricularCourseId)
            throws FenixServiceException {

        try {
            //List executionCourseList = null;
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            if (executionCourseId == null) {
                throw new FenixServiceException("nullExecutionCourseId");
            }
            if (curricularCourseId == null) {
                throw new FenixServiceException("nullCurricularCourseId");
            }

            IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(
                    ExecutionCourse.class, executionCourseId, true);
            if (executionCourse == null) {
                throw new NonExistingServiceException("noExecutionCourse");
            }

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);
            if (curricularCourse == null) {
                throw new NonExistingServiceException("noCurricularCourse");
            }

            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            List executionCourses = curricularCourse.getAssociatedExecutionCourses();

            if (!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
                executionCourses.remove(executionCourse);
                curricularCourses.remove(curricularCourse);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;
    }
}
