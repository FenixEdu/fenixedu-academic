/*
 * Created on 5/Nov/2003
 *
 * 
 */
package ServidorAplicacao.Filtro;

import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;
import Util.RoleType;

/**
 * @author João Mota
 *
 */
public class CurrentDegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

	public final static CurrentDegreeCoordinatorAuthorizationFilter instance =
		new CurrentDegreeCoordinatorAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType() {
		return RoleType.COORDINATOR;
	}

	public void preFiltragem(IUserView id, Object[] argumentos)
		throws NotAuthorizedException {
		try {
			if ((id == null)
				|| (id.getRoles() == null)
				|| !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
				|| !isCoordinatorOfCurrentExecutionDegree(id, argumentos)) {
				throw new NotAuthorizedException();
			}
		} catch (RuntimeException e) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean isCoordinatorOfCurrentExecutionDegree(IUserView id, Object[] argumentos) {

		ISuportePersistente sp;
		boolean result = false;
		if (argumentos == null) {
			return result;
		}
		if (argumentos[0] == null) {
			return result;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher = persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
			IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(
					new CursoExecucao((Integer) argumentos[0]),
					false);
			IExecutionYear executionYear = executionDegree.getExecutionYear();

			ICoordinator coordinator =
				persistentCoordinator.readCoordinatorByTeacherAndExecutionDegree(
					teacher,
					executionDegree);

			result = (coordinator != null) && executionYear.getState().equals(PeriodState.CURRENT);

		} catch (Exception e) {
			return false;
		}

		return result;
	}

}
