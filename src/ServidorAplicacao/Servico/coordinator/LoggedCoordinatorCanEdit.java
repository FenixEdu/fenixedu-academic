package ServidorAplicacao.Servico.coordinator;

import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

/**
 * @author Fernanda Quitério 19/Nov/2003
 */
public class LoggedCoordinatorCanEdit implements IServico
{

	private static LoggedCoordinatorCanEdit service = new LoggedCoordinatorCanEdit();

	public static LoggedCoordinatorCanEdit getService()
	{

		return service;
	}

	private LoggedCoordinatorCanEdit()
	{

	}

	public final String getNome()
	{

		return "LoggedCoordinatorCanEdit";
	}

	public Boolean run(Integer executionDegreeCode, Integer curricularCourseCode, String username)
		throws FenixServiceException
	{
		Boolean result = new Boolean(false);
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			if (executionDegreeCode == null)
			{
				throw new FenixServiceException("nullExecutionDegreeCode");
			}
			if (curricularCourseCode == null)
			{
				throw new FenixServiceException("nullCurricularCourseCode");
			}
			if (username == null)
			{
				throw new FenixServiceException("nullUsername");
			}

			ITeacher teacher = persistentTeacher.readTeacherByUsernamePB(username);

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeCode);
			executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);

			IExecutionYear executionYear = executionDegree.getExecutionYear();

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

			if (curricularCourse == null)
			{
				throw new NonExistingServiceException();
			}

			ICoordinator coordinator =
				persistentCoordinator.readCoordinatorByTeacherAndExecutionDegree(
					teacher,
					executionDegree);

			result =
				new Boolean(
					(coordinator != null)
						&& executionYear.getState().equals(PeriodState.CURRENT)
						&& curricularCourse.getBasic().equals(Boolean.FALSE));
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return result;
	}
}