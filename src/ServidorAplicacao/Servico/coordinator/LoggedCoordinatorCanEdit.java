package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

/**
 * @author Fernanda Quitério 19/Nov/2003
 */
public class LoggedCoordinatorCanEdit implements IService {

    public Boolean run(Integer executionDegreeCode, Integer curricularCourseCode, String username)
            throws FenixServiceException {
        Boolean result = new Boolean(false);
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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

            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class, executionDegreeCode);

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