/*
 * Created on 10/Nov/2003
 *  
 */

package ServidorAplicacao.Filtro.person;

import DataBeans.person.InfoQualification;
import Dominio.IQualification;
import Dominio.ITeacher;
import Dominio.Qualification;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class QualificationManagerAuthorizationFilter extends Filtro {

	public final static QualificationManagerAuthorizationFilter instance =
		new QualificationManagerAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the
	 *               authorization access to services.
	 */
	public static Filtro getInstance() {
		return instance;
	}

	protected RoleType getRoleType1() {
		return RoleType.TEACHER;
	}
	protected RoleType getRoleType2() {
		return RoleType.GRANT_OWNER_MANAGER;
	}

	public void preFiltragem(
		IUserView id,
		IServico service,
		Object[] arguments)
		throws NotAuthorizedException {

		try {
			//Verify if needed fields are null
			if ((id == null)
				|| (id.getRoles() == null)
				|| ((arguments[0] instanceof Integer) && (arguments[0] == null))
				|| ((arguments[1] instanceof InfoQualification)
					&& (arguments[1] == null))) {
				throw new NotAuthorizedException();
			}

			//Verify if:
			//The qualification as InternalId (Edit scenario) and does't exist
			if (InvalidQualificationEdit(arguments))
				throw new NotAuthorizedException();

			//Verify if:
			// 1: The user ir a Grant Owner Manager and the qualification
			//belongs to a Grant Owner
			// 2: The user ir a Teacher and the qualification is his own
			boolean valido = false;

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleType2())
				&& IsGrantOwner(arguments)) {
				valido = true;
			}

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleType1())
				&& IsTeacher(arguments)
				&& IsHisOwnQualification(arguments)) {
				valido = true;
			}

			if (!valido)
				throw new NotAuthorizedException();
		} catch (RuntimeException e) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param arguments
	 * @return true if the person is a grant owner
	 */
	private boolean IsGrantOwner(Object[] arguments) {
		ISuportePersistente sp = null;
		IPersistentGrantOwner spGO = null;

		try {
			//Is the person a grant owner?
			sp = SuportePersistenteOJB.getInstance();
			spGO = sp.getIPersistentGrantOwner();
			IGrantOwner grantowner = null;

			grantowner =
				spGO.readGrantOwnerByPerson(
					((InfoQualification) arguments[1])
						.getPersonInfo()
						.getIdInternal());

			if (grantowner != null) {
				return true;
			}

		} catch (ExcepcaoPersistencia e) {
			System.out.println(
				"Filter error(ExcepcaoPersistente): " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("Filter error(Unknown): " + e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * @param arguments
	 * @return true if the person is a teacher
	 */
	private boolean IsTeacher(Object[] arguments) {
		ISuportePersistente sp = null;
		IPersistentTeacher spT = null;

		try {
			//Is the person a Teacher?
			sp = SuportePersistenteOJB.getInstance();
			spT = sp.getIPersistentTeacher();
			ITeacher teacher = null;

			teacher =
				spT.readTeacherByUsername(
					((InfoQualification) arguments[1])
						.getPersonInfo()
						.getUsername());

			if (teacher != null)
				return true;

		} catch (ExcepcaoPersistencia e) {
			System.out.println(
				"Filter error(ExcepcaoPersistente): " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("Filter error(Unknown): " + e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * @param arguments
	 * @return
	 */
	private boolean IsHisOwnQualification(Object[] arguments) {
		try {
			if (arguments[0]
				.equals(
					((InfoQualification) arguments[1])
						.getPersonInfo()
						.getIdInternal())) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * @param arguments
	 * @return true if the person is a grant owner
	 */
	private boolean InvalidQualificationEdit(Object[] arguments) {
		ISuportePersistente sp = null;
		IPersistentQualification spQ = null;

		try {
			if (((InfoQualification) arguments[1]).getIdInternal() != null) {
				//Verificar se a qualificacao existe
				sp = SuportePersistenteOJB.getInstance();
				spQ = sp.getIPersistentQualification();
				IQualification infoq = null;
				infoq =
					(IQualification) spQ.readByOID(
						Qualification.class,
						((InfoQualification) arguments[1]).getIdInternal());
				
				if (infoq == null)
					return true;
			}
		} catch (ExcepcaoPersistencia e) {
			System.out.println(
				"Filter error(ExcepcaoPersistente): " + e.getMessage());
			return false;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}