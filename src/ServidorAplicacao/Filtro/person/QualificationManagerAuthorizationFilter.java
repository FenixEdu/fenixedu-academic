/*
 * Created on 10/Nov/2003
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
public class QualificationManagerAuthorizationFilter extends Filtro
{

	public final static QualificationManagerAuthorizationFilter instance =
		new QualificationManagerAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
	public static Filtro getInstance()
	{
		return instance;
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

	/**
	 * Runs the filter
	 * 
	 * @param id
	 * @param service
	 * @param arguments
	 */
	public void preFiltragem(IUserView id, IServico service, Object[] arguments)
		throws NotAuthorizedException
	{
		try
		{
			//Verify if needed fields are null
			if ((id == null)
				|| (id.getRoles() == null)
				|| ((arguments[0] instanceof Integer) && (arguments[0] == null))
				|| ((arguments[1] instanceof InfoQualification) && (arguments[1] == null)))
			{
				throw new NotAuthorizedException();
			}

			//Verify if:
			//  The qualification internalId is valid
			if (invalidQualificationToEdit(arguments))
			{
				throw new NotAuthorizedException();
			}

			//Verify if:
			// 1: The user ir a Grant Owner Manager and the qualification belongs to a Grant Owner
			// 2: The user ir a Teacher and the qualification is his own
			boolean valido = false;

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())
				&& isGrantOwner(arguments))
			{
				valido = true;
			}

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())
				&& isTeacher(arguments)
				&& isOwnQualification(arguments))
			{
				valido = true;
			}

			if (!valido)
				throw new NotAuthorizedException();
		} catch (RuntimeException e)
		{
			throw new NotAuthorizedException();
		}
	}

	/**
	 * Verifies if the qualification user ir a grant owner
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isGrantOwner(Object[] arguments)
	{
		ISuportePersistente persistentSuport = null;
		IPersistentGrantOwner persistentGrantOwner = null;

		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentGrantOwner = persistentSuport.getIPersistentGrantOwner();

			//Try to read the grant owner from de database
			IGrantOwner grantowner = null;
			grantowner =
				persistentGrantOwner.readGrantOwnerByPerson(
					((InfoQualification) arguments[1]).getInfoPerson().getIdInternal());

			if (grantowner != null) //The grant owner exists!
			{
				return true;
			}

		} catch (ExcepcaoPersistencia e)
		{
			return false;
		} catch (Exception e)
		{
			return false;
		}
		return false; //The qualification user is not a grant owner.
	}

	/**
	 * Verifies if the qualification user ir a teacher
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isTeacher(Object[] arguments)
	{
		ISuportePersistente persistentSuport = null;
		IPersistentTeacher persistentTeacher = null;

		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentTeacher = persistentSuport.getIPersistentTeacher();

			//Try to read the teacher from de database
			ITeacher teacher = null;
			teacher =
				persistentTeacher.readTeacherByUsername(
					((InfoQualification) arguments[1]).getInfoPerson().getUsername());

			if (teacher != null) //The teacher exists!
			{
				return true;
			}

		} catch (ExcepcaoPersistencia e)
		{
			return false;
		} catch (Exception e)
		{
			return false;
		}
		return false; //The qualification user is not a teacher
	}

	/**
	 * Verifies if the qualification to be changed is owned by the user that is running the service
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isOwnQualification(Object[] arguments)
	{
		try
		{
			if (arguments[0].equals(((InfoQualification) arguments[1]).getInfoPerson().getIdInternal()))
			{
				return true;
			}
		} catch (Exception e)
		{
			return false;
		}
		return false;
	}

	/**
	 * Verifies if the qualification Id is not null in the arguments of the service but doesn't exist in
	 * the database
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean invalidQualificationToEdit(Object[] arguments)
	{
		ISuportePersistente persistentSuport = null;
		IPersistentQualification persistentQualification = null;

		try
		{
			if (((InfoQualification) arguments[1]).getIdInternal() != null)
			{
				//Read Qualification from DataBase
				persistentSuport = SuportePersistenteOJB.getInstance();
				persistentQualification = persistentSuport.getIPersistentQualification();
				IQualification qualification = null;
				qualification =
					(IQualification) persistentQualification.readByOID(
						Qualification.class,
						((InfoQualification) arguments[1]).getIdInternal());

				if (qualification == null) //The qualification doesn't exist in db
					return true;
			}
		} catch (ExcepcaoPersistencia e)
		{
			return true;
		} catch (Exception e)
		{
			return true;
		}
		return false; //The qualification is valid!
	}
}