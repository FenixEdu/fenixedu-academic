package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICoordinator;
import Dominio.ICurso;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 04/Fev/2003
 */
public class UserCoordinatorByExecutionDegree implements IService
{
	public UserCoordinatorByExecutionDegree()
	{

	}

	public Boolean run(Integer executionDegreeCode, String teacherUserName, String degree2Compare)
		throws FenixServiceException
	{
		boolean result = false;

		try
		{
			ISuportePersistente sp;
			ICurso degree = null;

			sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = persistentTeacher.readTeacherByUsername(teacherUserName);

			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			ICoordinator coordinator =
				persistentCoordinator.readCoordinatorByTeacherAndExecutionDegreeId(
					teacher,
					executionDegreeCode);
			degree = coordinator.getExecutionDegree().getCurricularPlan().getDegree();
						
			result = degree.getSigla().equals(degree2Compare);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}

		return new Boolean(result);
	}
}
