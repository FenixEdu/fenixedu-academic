/*
 * Created on 13/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Filtro;

import java.util.Collection;
import java.util.List;

import DataBeans.InfoTeacher;
import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class CreditsAuthorizationFilter extends Filtro {

	// the singleton of this class
	public final static CreditsAuthorizationFilter instance =
		new CreditsAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}

	/**
	 * @see ServidorAplicacao.Filtro.Filtro#preFiltragem(IUserView, IServico, Object[])
	 * @throws ServidorAplicacao.NotAuthorizedException if the user doesn't contains role returned by getRoleType() function
	 */
	public void preFiltragem(
		IUserView requester,
		Object[] arguments)
		throws Exception {
		Collection roles = requester.getRoles();
		boolean authorizedRequester = false;
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		// ATTENTION: ifs order matters...
		if (AuthorizationUtils.containsRole(roles, RoleType.CREDITS_MANAGER)) {
			authorizedRequester = true;
		} else if (
			AuthorizationUtils.containsRole(
				roles,
				RoleType.DEPARTMENT_CREDITS_MANAGER)) {
			ITeacher teacherToEdit = readTeacher(arguments[0], sp);

			IPessoaPersistente personDAO = sp.getIPessoaPersistente();
			IPessoa requesterPerson =
				personDAO.lerPessoaPorUsername(requester.getUtilizador());

			List departmentsWithAccessGranted =
				requesterPerson.getManageableDepartmentCredits();
			IPersistentDepartment departmentDAO =
				sp.getIDepartamentoPersistente();
			IDepartment department = departmentDAO.readByTeacher(teacherToEdit);
			if (department == null) {
				System.out.println(
					"----------->"
						+ teacherToEdit.getTeacherNumber()
						+ " doesn't have department!");
			}

			authorizedRequester =
				departmentsWithAccessGranted.contains(department);

		} else if (AuthorizationUtils.containsRole(roles, RoleType.TEACHER)) {
			ITeacher teacherToEdit = readTeacher(arguments[0], sp);
			authorizedRequester =
				teacherToEdit.getPerson().getUsername().equals(
					requester.getUtilizador());

		}

		if (!authorizedRequester) {
			throw new NotAuthorizedException(
				" -----------> User = "
					+ requester.getUtilizador()
					+ "ACCESS NOT GRANTED!");
		}
	}

	/**
	 * @param object
	 * @return
	 */
	private ITeacher readTeacher(Object object, ISuportePersistente sp) {
		Integer teacherOID = null;
		if (object instanceof InfoTeacher) {
			teacherOID = ((InfoTeacher) object).getIdInternal();
		} else if (object instanceof Integer) {
			teacherOID = (Integer) object;
		}
		return (ITeacher) sp.getIPersistentTeacher().readByOId(
			new Teacher(teacherOID),
			false);
	}
}
