package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 19/Nov/2003
 */
public class LoggedCoordinatorCanEdit implements IService {

    public Boolean run(Integer executionDegreeCode, Integer curricularCourseCode, String username)
            throws FenixServiceException {
        Boolean result = new Boolean(false);
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            if (executionDegreeCode == null) {
                throw new FenixServiceException("nullExecutionDegreeCode");
            }
            if (curricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourseCode");
            }
            if (username == null) {
                throw new FenixServiceException("nullUsername");
            }

            ITeacher teacher = persistentTeacher.readTeacherByUsername(username);

            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeCode);

            IExecutionYear executionYear = executionDegree.getExecutionYear();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            ICoordinator coordinator = persistentCoordinator.readCoordinatorByTeacherAndExecutionDegree(
                    teacher, executionDegree);

            // if user is coordinator and is the current coordinator and
            // curricular course is not basic
            // coordinator can edit
            result = new Boolean((coordinator != null)
                    && executionYear.getState().equals(PeriodState.CURRENT)
                    && curricularCourse.getBasic().equals(Boolean.FALSE));
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return result;
    }
}