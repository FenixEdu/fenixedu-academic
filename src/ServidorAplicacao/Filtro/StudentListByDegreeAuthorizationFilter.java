
package ServidorAplicacao.Filtro;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;
import Util.TipoCurso;



/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByDegreeAuthorizationFilter extends Filtro {

	public final static StudentListByDegreeAuthorizationFilter autorizacao = new StudentListByDegreeAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static StudentListByDegreeAuthorizationFilter getInstance() {
	  return autorizacao;
	}


	/**
	 * 
	 * @throws ServidorAplicacao.NotAuthorizedException if the user doesn't have the required privileges to run the service
	 */
	public void preFiltragem(IUserView userView, IServico service, Object[] arguments) throws Exception {
		if (userView.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)){
			this.masterDegreeAdministrativeOfficeFilter(userView, arguments);				
		} else if (userView.hasRoleType(RoleType.COORDINATOR)){
			this.coordinatorFilter(userView, arguments);
		}
		throw new NotAuthorizedException();
	}

	/**
	 * 
	 * @param userView
	 * @param arguments
	 */
	private void masterDegreeAdministrativeOfficeFilter(IUserView userView, Object arguments[]) throws Exception {
		
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arguments[0];
		
		if(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)) {
			;
		}
		throw new NotAuthorizedException();
	}
	
	/**
	 * 
	 * @param userView
	 * @param arguments
	 */
	private void coordinatorFilter(IUserView userView, Object arguments[]){
		
	}


}
