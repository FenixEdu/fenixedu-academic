package ServidorAplicacao.Servico.coordinator;

import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
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

	public Boolean run(Integer executionDegreeCode, IUserView userView) throws FenixServiceException
	{
		Boolean result = Boolean.FALSE;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

			if (executionDegreeCode == null)
			{
				throw new FenixServiceException("nullExecutionDegreeCode");
			}
			if (userView == null)
			{
				throw new FenixServiceException("nullUserView");
			}

			ITeacher teacher = persistentTeacher.readTeacherByUsernamePB(userView.getUtilizador());

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeCode);
			executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);

			IExecutionYear executionYear = executionDegree.getExecutionYear();

			ICoordinator coordinator =
				persistentCoordinator.readCoordinatorByTeacherAndExecutionDegree(
					teacher,
					executionDegree);

			result =
				new Boolean(
					(coordinator != null) && executionYear.getState().equals(PeriodState.CURRENT));
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return result;
	}
}