package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 *
 * @author Angela
 * @version
 **/

import java.util.Collection;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

public class ErasmusAuthorizationFilter extends Filtro {

  
	// the singleton of this class
	public final static ErasmusAuthorizationFilter instance = new ErasmusAuthorizationFilter();
	
	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
	  return instance;
	}
	
	
	public void preFiltragem(
		IUserView requester,
		Object[] arguments)
		throws Exception {
			
		Collection roles = requester.getRoles();
		boolean authorizedRequester = false;
		
		
		if (AuthorizationUtils.containsRole(roles, RoleType.ERASUMS)) 
			authorizedRequester = true;
		if (!authorizedRequester) {
			throw new NotAuthorizedException(
				" -----------> User = "
					+ requester.getUtilizador()
					+ "ACCESS NOT GRANTED!");
		}
	}

	
}