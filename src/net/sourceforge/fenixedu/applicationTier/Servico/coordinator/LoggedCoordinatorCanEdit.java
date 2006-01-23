package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Fernanda Quitério 19/Nov/2003
 */
public class LoggedCoordinatorCanEdit extends Service {

    public Boolean run(Integer executionDegreeCode, Integer curricularCourseCode, String username)
            throws FenixServiceException, ExcepcaoPersistencia {
        Boolean result = new Boolean(false);

        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentCoordinator persistentCoordinator = persistentSupport.getIPersistentCoordinator();

        if (executionDegreeCode == null) {
            throw new FenixServiceException("nullExecutionDegreeCode");
        }
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }
        if (username == null) {
            throw new FenixServiceException("nullUsername");
        }

        Teacher teacher = persistentTeacher.readTeacherByUsername(username);

        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, executionDegreeCode);

        ExecutionYear executionYear = executionDegree.getExecutionYear();

        CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
                CurricularCourse.class, curricularCourseCode);

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        Coordinator coordinator = persistentCoordinator.readCoordinatorByTeacherIdAndExecutionDegreeId(
                teacher.getIdInternal(), executionDegree.getIdInternal());

        // if user is coordinator and is the current coordinator and
        // curricular course is not basic
        // coordinator can edit
        result = new Boolean((coordinator != null)
                && executionYear.getState().equals(PeriodState.CURRENT)
                && curricularCourse.getBasic().equals(Boolean.FALSE));

        return result;
    }
}