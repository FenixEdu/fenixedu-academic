/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Servico.coordinator;
import java.util.ArrayList;
import java.util.List;

import Dominio.Coordinator;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
/**
 * @author João Mota 30/Oct/2003
 *  
 */
public class AddCoordinator implements IServico
{
	private static AddCoordinator service = new AddCoordinator();
	public static AddCoordinator getService()
	{
		return service;
	}
	private AddCoordinator()
	{
	}
	public final String getNome()
	{
		return "AddCoordinator";
	}
	public Boolean run(Integer executionDegreeId, Integer teacherNumber) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
			if (teacher == null)
			{
				throw new NonExistingServiceException();
			}
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = new CursoExecucao(executionDegreeId);
			executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree,
					false);
			if (executionDegree == null)
			{
				throw new InvalidArgumentsServiceException();
			}
			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			ICoordinator coordinator = persistentCoordinator.readCoordinatorByTeacherAndExecutionDegree(
					teacher, executionDegree);
			if (coordinator == null)
			{
				coordinator = new Coordinator();
				persistentCoordinator.simpleLockWrite(coordinator);
				coordinator.setExecutionDegree(executionDegree);
				coordinator.setTeacher(teacher);
				coordinator.setResponsible(new Boolean(false));
				//verify if the teacher already was coordinator
				List executionDegreesTeacherList = persistentCoordinator
						.readExecutionDegreesByTeacher(teacher);
				if (executionDegreesTeacherList == null || executionDegreesTeacherList.size() <= 0)
				{
					//Role Coordinator
					IPersistentRole persistentRole = sp.getIPersistentRole();
					IRole role = persistentRole.readByRoleType(RoleType.COORDINATOR);
														
					IPessoa person = teacher.getPerson();
					IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
					persistentPerson.simpleLockWrite(person);
					
					if(person.getPersonRoles() == null) {
						person.setPersonRoles(new ArrayList());
					}					
					if(!person.getPersonRoles().contains(role)) {
						person.getPersonRoles().add(role);
					}
//					IPersonRole personRole = new PersonRole();
//					IPersistentPersonRole persistentPersonRole = sp.getIPersistentPersonRole();
//					persistentPersonRole.simpleLockWrite(personRole);
//					personRole.setPerson(person);
//					personRole.setRole(sp.getIPersistentRole().readByRoleType(RoleType.COORDINATOR));
				}
			} else
			{
				throw new ExistingServiceException();
			}
			return new Boolean(true);
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
