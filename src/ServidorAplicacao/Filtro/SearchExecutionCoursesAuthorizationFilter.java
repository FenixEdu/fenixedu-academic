
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class SearchExecutionCoursesAuthorizationFilter extends Filtro {

	public final static SearchExecutionCoursesAuthorizationFilter instance =
		new SearchExecutionCoursesAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}


	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception {

		if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
			|| (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
			|| (id == null)
			|| (id.getRoles() == null)) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param collection
	 * @return boolean
	 */
	private boolean containsRole(Collection roles) {
		CollectionUtils.intersection(roles, getNeededRoles());

		if (roles.size() != 0){
			return true;			
		} else {
			return false;
		}
	}
	
	
	/**
	 * @return The Needed Roles to Execute The Service
	 */
	private Collection getNeededRoles() {
		List roles = new ArrayList();
		
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
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
	private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
	
		List roles = getRoleList((List) id.getRoles());
		CollectionUtils.intersection(roles, getNeededRoles());

		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

		List roleTemp = new ArrayList();
		roleTemp.add(RoleType.TIME_TABLE_MANAGER);
		if (CollectionUtils.containsAny(roles, roleTemp)) {
			return true;
		} 
		
		roleTemp = new ArrayList();
		roleTemp.add(RoleType.COORDINATOR);
		if (CollectionUtils.containsAny(roles, roleTemp)) {
			
			 ITeacher teacher = null;
			// Read The ExecutionDegree
			try {
								
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arguments[1];
				
				if (infoExecutionDegree == null) {
					return false;
				}
				
				ICursoExecucao executionDegreeTemp = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
				
				ICursoExecucao executionDegree = (ICursoExecucao) sp.getICursoExecucaoPersistente().readByOId(executionDegreeTemp, false);
				
				teacher = sp.getIPersistentTeacher().readTeacherByUsername(id.getUtilizador());
				
				if (executionDegree.getCoordinator().equals(teacher)) {
					return true;
				}
				
			} catch (Exception e) {
				return false;
			}
		}
		return false; 
	}
	
	private List getRoleList(List roles){
		List result = new ArrayList();
		Iterator iterator = roles.iterator();
		while(iterator.hasNext()){
			result.add(((InfoRole) iterator.next()).getRoleType());
		}
		
		return result;
	}

}
