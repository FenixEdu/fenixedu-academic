/*
 * Created on 10/Nov/2003
 */

package ServidorAplicacao.Filtro.person;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.person.InfoQualification;
import Dominio.IPessoa;
import Dominio.IQualification;
import Dominio.Qualification;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class QualificationManagerAuthorizationFilter extends Filtro
{

	public QualificationManagerAuthorizationFilter()
	{
	}

	//Role Type of teacher
	protected RoleType getRoleTypeTeacher()
	{
		return RoleType.TEACHER;
	}

	//Role Type of Grant Owner Manager
	protected RoleType getRoleTypeGrantOwnerManager()
	{
		return RoleType.GRANT_OWNER_MANAGER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *      pt.utl.ist.berserk.ServiceResponse)
	 */
	public void execute(ServiceRequest request, ServiceResponse response)
		throws Exception
	{
		IUserView id = getRemoteUser(request);
		Object[] arguments = getServiceCallArguments(request);

		try
		{
			//Verify if needed fields are null
			if ((id == null) || (id.getRoles() == null))
			{
				throw new NotAuthorizedException();
			}

			if (!AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())
				&& !AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher()))
				throw new NotAuthorizedException();

			InfoQualification infoQualification = null;

			//New Qualification, second argument is a qualification
			infoQualification = (InfoQualification) arguments[1];
			if (infoQualification == null)
				throw new NotAuthorizedException();

			boolean valid = false;
			//Verify if:
			// 1: The user ir a Grant Owner Manager and the qualification
			// belongs to a Grant Owner
			// 2: The user ir a Teacher and the qualification is his own
			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())
				&& isGrantOwner(infoQualification))
				valid = true;

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())
				&& isOwnQualification(id.getUtilizador(), infoQualification))
				valid = true;

			if (!valid)
				throw new NotAuthorizedException();

		}
		catch (RuntimeException e)
		{
			throw new NotAuthorizedException();
		}
	}

	/**
	 * Verifies if the qualification user is a grant owner
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isGrantOwner(InfoQualification infoQualification)
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentGrantOwner persistentGrantOwner = persistentSuport.getIPersistentGrantOwner();

			//Try to read the grant owner from de database
			IGrantOwner grantOwner =
				persistentGrantOwner.readGrantOwnerByPerson(
					infoQualification.getInfoPerson().getIdInternal());

			return grantOwner != null;
		}
		catch (ExcepcaoPersistencia e)
		{
			System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Filter error(Unknown): " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Verifies if the qualification to be changed is owned by the user that is running the service
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isOwnQualification(String username, InfoQualification infoQualification)
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPessoa person = persistentPerson.lerPessoaPorUsername(username);

			boolean isNew =
				(infoQualification.getIdInternal() == null)
					|| (infoQualification.getIdInternal().equals(new Integer(0)));
			if (isNew)
				return true;

			IPersistentQualification persistentQualification = sp.getIPersistentQualification();
			IQualification qualification =
				(IQualification) persistentQualification.readByOID(
					Qualification.class,
					infoQualification.getIdInternal());

			return qualification.getPerson().equals(person);
		}
		catch (ExcepcaoPersistencia e)
		{
			System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Filter error(Unknown): " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}