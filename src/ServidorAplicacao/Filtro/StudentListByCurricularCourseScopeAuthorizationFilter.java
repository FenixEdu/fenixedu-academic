package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoRole;
import Dominio.CurricularCourseScope;
import Dominio.ICoordinator;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseScopeAuthorizationFilter extends Filtro
{

	public final static StudentListByCurricularCourseScopeAuthorizationFilter instance =
		new StudentListByCurricularCourseScopeAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
	public static Filtro getInstance()
	{
		return instance;
	}

	public void preFiltragem(IUserView id, Object[] argumentos) throws Exception
	{

		if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
			|| (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
			|| (id == null)
			|| (id.getRoles() == null))
		{
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param collection
	 * @return boolean
	 */
	private boolean containsRole(Collection roles)
	{
		CollectionUtils.intersection(roles, getNeededRoles());

		if (roles.size() != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return The Needed Roles to Execute The Service
	 */
	private Collection getNeededRoles()
	{
		List roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.COORDINATOR);
		roles.add(infoRole);

		return roles;
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean hasPrivilege(IUserView id, Object[] arguments)
	{

		List roles = getRoleList((List) id.getRoles());
		CollectionUtils.intersection(roles, getNeededRoles());

		Integer curricularCourseScopeID = (Integer) arguments[1];

		ICurricularCourseScope curricularCourseScope = null;

		// Read The DegreeCurricularPlan
		try
		{
			IPersistentCurricularCourseScope persistentCurricularCourseScope =
				SuportePersistenteOJB.getInstance().getIPersistentCurricularCourseScope();
			ICurricularCourseScope curricularCourseScopeTemp = new CurricularCourseScope();
			curricularCourseScopeTemp.setIdInternal(curricularCourseScopeID);

			curricularCourseScope =
				(ICurricularCourseScope) persistentCurricularCourseScope.readByOId(
					curricularCourseScopeTemp,
					false);
		}
		catch (Exception e)
		{
			return false;
		}

		if (curricularCourseScope == null)
		{
			return false;
		}

		List roleTemp = new ArrayList();
		roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		if (CollectionUtils.containsAny(roles, roleTemp))
		{
			if (curricularCourseScope
				.getCurricularCourse()
				.getDegreeCurricularPlan()
				.getDegree()
				.getTipoCurso()
				.equals(TipoCurso.MESTRADO_OBJ))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		roleTemp = new ArrayList();
		roleTemp.add(RoleType.COORDINATOR);
		if (CollectionUtils.containsAny(roles, roleTemp))
		{

			ITeacher teacher = null;
			// Read The ExecutionDegree
			try
			{
				ICursoExecucaoPersistente persistentExecutionDegree =
					SuportePersistenteOJB.getInstance().getICursoExecucaoPersistente();

				List executionDegrees =
					persistentExecutionDegree.readByDegreeCurricularPlan(
						curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan());
				if (executionDegrees == null)
				{
					return false;
				}
				// IMPORTANT: It's assumed that the coordinator for a Degree is ALWAYS the same
				//modified by Tânia Pousão
				List coodinatorsList =
					SuportePersistenteOJB
						.getInstance()
						.getIPersistentCoordinator()
						.readCoordinatorsByExecutionDegree(
						((ICursoExecucao) executionDegrees.get(0)));
				if (coodinatorsList == null)
				{
					return false;
				}
				ListIterator listIterator = coodinatorsList.listIterator();
				while (listIterator.hasNext())
				{
					ICoordinator coordinator = (ICoordinator) listIterator.next();

					if (id.getUtilizador().equals(coordinator.getTeacher().getPerson().getUsername()))
					{
						return true;
					}
				}

				//                teacher = ((ICursoExecucao) executionDegrees.get(0)).getCoordinator();
				//
				//                if (teacher == null)
				//                {
				//                    return false;
				//                }
				//
				//                if (id.getUtilizador().equals(teacher.getPerson().getUsername()))
				//                {
				//                    return true;
				//                } else
				//                {
				//                    return false;
				//                }
			}
			catch (Exception e)
			{
				return false;
			}
		}
		return false;
	}

	private List getRoleList(List roles)
	{
		List result = new ArrayList();
		Iterator iterator = roles.iterator();
		while (iterator.hasNext())
		{
			result.add(((InfoRole) iterator.next()).getRoleType());
		}

		return result;
	}

}
