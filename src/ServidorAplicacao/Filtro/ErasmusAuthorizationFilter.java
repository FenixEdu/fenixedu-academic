package ServidorAplicacao.Filtro;

/**
 * This class is responsible for verifying if a given user has the
 * authorization to run a service with certain attributes.
 *
 * @author Angela
 * @version
 **/

import java.util.Collection;

import DataBeans.InfoTeacher;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
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
		IServico service,
		Object[] arguments)
		throws Exception {
			
		Collection roles = requester.getRoles();
		boolean authorizedRequester = false;
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		
		if (AuthorizationUtils.containsRole(roles, RoleType.ERASUMS)) 
			authorizedRequester = true;
		if (!authorizedRequester) {
			throw new NotAuthorizedException(
				" -----------> User = "
					+ requester.getUtilizador()
					+ ": SERVICE "
					+ service.getNome()
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