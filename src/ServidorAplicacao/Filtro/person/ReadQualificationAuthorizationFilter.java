/*
 * Created on 21/Nov/2003
 */
package ServidorAplicacao.Filtro.person;

import DataBeans.person.InfoQualification;
import DataBeans.util.Cloner;
import Dominio.IQualification;
import Dominio.ITeacher;
import Dominio.Qualification;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
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

public class ReadQualificationAuthorizationFilter extends Filtro
{

	public final static ReadQualificationAuthorizationFilter instance =
		new ReadQualificationAuthorizationFilter();

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
			if ((id == null) || (id.getRoles() == null))
			{
				throw new NotAuthorizedException();
			}

			InfoQualification infoqualification = null;
			infoqualification = getInfoQualification((Integer) arguments[0]);

			if (infoqualification == null)
				throw new NotAuthorizedException();

			//Verify if:
			// 1: The user ir a Grant Owner Manager and the qualification belongs to a Grant Owner
			// 2: The user ir a Teacher and the qualification is his own
			boolean valid = false;

			if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
				&& isGrantOwner(infoqualification))
			{
				valid = true;
			}

			if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())
				&& isTeacher(infoqualification))
			{
				valid = true;
			}

			if (!valid)
				throw new NotAuthorizedException();
		} catch (RuntimeException e)
		{
			throw new NotAuthorizedException();
		}
	}

	/**
	 * Verifies if the qualification user ir a teacher
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isTeacher(InfoQualification infoqualification)
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
				persistentTeacher.readTeacherByUsername(infoqualification.getInfoPerson().getUsername());

			if (teacher != null) //The teacher exists!
			{
				return true;
			}
			return false; //The qualification user is not a teacher

		} catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Verifies if the qualification user ir a grant owner
	 * 
	 * @param arguments
	 * @return true or false
	 */
	private boolean isGrantOwner(InfoQualification infoqualification)
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
					infoqualification.getInfoPerson().getIdInternal());

			if (grantowner != null) //The grant owner exists!
			{
				return true;
			}
			return false;

		} catch (Exception e)
		{
			return false; //The qualification user is not a grant owner.
		}
	}

	/**
	 * Returns the qualification form the database
	 * 
	 * @param arguments
	 * @return infoqualification
	 */
	private InfoQualification getInfoQualification(Integer qualificationKey)
	{
		ISuportePersistente persistentSuport = null;
		IPersistentQualification persistentQualification = null;

		try
		{
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentQualification = persistentSuport.getIPersistentQualification();

			//Try to read the qualification from the database
			IQualification qualification = null;
			qualification =
				(IQualification) persistentQualification.readByOID(
					Qualification.class,
					qualificationKey);

			if (qualification == null)
				return null;
			else
				return Cloner.copyIQualification2InfoQualification(qualification);

		} catch (Exception e)
		{
			return null;
		}
	}
}