/*
 * Created on 21/Nov/2003
 */


import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
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

				//Verify if:
				// 1: The user ir a Grant Owner Manager and the qualification belongs to a Grant Owner
				// 2: The user ir a Teacher and the qualification is his own
				boolean valid = false;
            
				if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager())))
				{
					valid = true;
				}

				if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher()))
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
}