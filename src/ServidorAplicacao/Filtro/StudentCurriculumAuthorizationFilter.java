
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoRole;
import Dominio.ICursoExecucao;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentCurriculumAuthorizationFilter extends Filtro {

	public final static StudentCurriculumAuthorizationFilter instance =
		new StudentCurriculumAuthorizationFilter();

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
			|| (id != null && id.getRoles() != null && !hasProvilege(id, argumentos))
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
		infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);
		 
		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.COORDINATOR);
		roles.add(infoRole);
				
		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);

		return roles;
	}


	/**
	 * @param id
	 * @param argumentos
	 * @return  
	 */
	private boolean hasProvilege(IUserView id, Object[] arguments) {
	
		List roles = getRoleList((List) id.getRoles());
		CollectionUtils.intersection(roles, getNeededRoles());

		Integer studentCurricularPlanID = (Integer) arguments[1];
		

		IStudentCurricularPlan studentCurricularPlan = null;

		// Read The DegreeCurricularPlan
		try {
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente();
			IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
			studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);
			
			studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readByOId(studentCurricularPlanTemp, false);
			
		} catch (Exception e) {
			return false;
		}

		if (studentCurricularPlan == null) {
			return false;
		}

		List roleTemp = new ArrayList();
		roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		if (CollectionUtils.containsAny(roles, roleTemp)) {
			if (studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)){
				return true;
			} else {
				return false;
			}
		} 
		
		roleTemp = new ArrayList();
		roleTemp.add(RoleType.COORDINATOR);
		if (CollectionUtils.containsAny(roles, roleTemp)) {
			
			 ITeacher teacher = null;
			// Read The ExecutionDegree
			try {
				ICursoExecucaoPersistente persistentExecutionDegree = SuportePersistenteOJB.getInstance().getICursoExecucaoPersistente();
			
				List executionDegrees = (List) persistentExecutionDegree.readByDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());
				if (executionDegrees == null){
					return false;
				}
				
				// IMPORTANT: It's assumed that the coordinator for a Degree is ALWAYS the same	
				teacher = ((ICursoExecucao) executionDegrees.get(0)).getCoordinator();
				
				if (teacher == null){
					return false;
				}
				
				if (id.getUtilizador().equals(teacher.getPerson().getUsername())){
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		} 


		roleTemp = new ArrayList();
		roleTemp.add(RoleType.STUDENT);
		if (CollectionUtils.containsAny(roles, roleTemp)) {
			
			try {

				if (id.getUtilizador().equals(studentCurricularPlan.getStudent().getPerson().getUsername())){
					return true;
				} else {
					return false;
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
