/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import Dominio.Coordinator;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão 12/Dez/2003
 *  
 */
public class ResponsibleCoordinators implements IServico
{

	private static ResponsibleCoordinators service = new ResponsibleCoordinators();

	public static ResponsibleCoordinators getService()
	{

		return service;
	}

	private ResponsibleCoordinators()
	{

	}

	public final String getNome()
	{

		return "ResponsibleCoordinators";
	}

	public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException
	{

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = new CursoExecucao(executionDegreeId);
			executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
			if (executionDegree == null)
			{
				throw new InvalidArgumentsServiceException();
			}

			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			Iterator iterator = executionDegree.getCoordinatorsList().iterator();
			while (iterator.hasNext())
			{
				ICoordinator coordinator = (ICoordinator) iterator.next();
				Integer coordinatorId = coordinator.getIdInternal();
				
				coordinator = (ICoordinator) persistentCoordinator.readByOId(coordinator, true);
				if(coordinator == null){
					return new Boolean(false);
				}
				
				if (coordinatorsIds.contains(coordinatorId))
				{//coordinator is responsible
					coordinator.setResponsible(Boolean.TRUE);
					System.out.println(
							"Alterar responsabilidade(sim): " + coordinator.getTeacher().getTeacherNumber());	
				} else {//coordinator isn't responsible
					coordinator.setResponsible(Boolean.FALSE);
					System.out.println(
							"Alterar responsabilidade(não): " + coordinator.getTeacher().getTeacherNumber());
				}
			}

			return new Boolean(true);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
