package ServidorAplicacao.Filtro;

import DataBeans.InfoStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author David Santos
 *
 */

//public class DegreeAndMasterAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter {
public class DegreeAndMasterAdministrativeOfficeAuthorizationFilter extends Filtro {

  
	public final static DegreeAndMasterAdministrativeOfficeAuthorizationFilter instance = new DegreeAndMasterAdministrativeOfficeAuthorizationFilter();
	
	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
	  return instance;
	}
	
	protected RoleType getRoleType() {
		return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
	}

//	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception {
//		if(!AuthorizationUtils.containsRole(id.getRoles(), RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
//			if(!AuthorizationUtils.containsRole(id.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
//				super.preFiltragem(id, servico, argumentos);
//			}
//		}
//	}

	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception {
		if(AuthorizationUtils.containsRole(id.getRoles(), RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
			if(!this.okToExecute(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE, TipoCurso.MESTRADO, servico, argumentos)) {
				throw new NotAuthorizedException();
			}
		} else if(AuthorizationUtils.containsRole(id.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
			if(!this.okToExecute(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, TipoCurso.LICENCIATURA, servico, argumentos)) {
				throw new NotAuthorizedException();
			}
		} else {
			throw new NotAuthorizedException();
		}
	}

	private boolean okToExecute(RoleType userViewRoleType, int degreeType, IServico servico, Object[] argumentos) {
		if(servico.getNome().equals("GetStudentByNumberAndDegreeType")) {
			if( ((Integer)argumentos[0]).intValue() != degreeType ) {
				return false;
			} else {
				return true;
			}
		} else if(servico.getNome().equals("ReadExecutionDegreesByExecutionYearAndDegreeType")) {
			if(userViewRoleType.equals(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
				return true;
			} else if( ((TipoCurso)argumentos[1]).getTipoCurso().intValue() != degreeType ) {
				return false;
			} else {
				return true;
			}
		} else if(servico.getNome().equals("PrepareEnrolmentContext")) {
			if( ((InfoStudent)argumentos[0]).getDegreeType().getTipoCurso().intValue() != degreeType ) {
				return false;
			} else {
				return true;
			}
		} else if(servico.getNome().equals("ValidateActualEnrolmentWithoutRules")) {
			if( ((InfoEnrolmentContext)argumentos[0]).getInfoStudentActiveCurricularPlan().getInfoStudent().getDegreeType().getTipoCurso().intValue() != degreeType ) {
				return false;
			} else {
				return true;
			}
		} else if(servico.getNome().equals("ConfirmActualEnrolmentWithoutRules")) {
			if( ((InfoEnrolmentContext)argumentos[0]).getInfoStudentActiveCurricularPlan().getInfoStudent().getDegreeType().getTipoCurso().intValue() != degreeType ) {
				return false;
			} else {
				return true;
			}
		} else if(servico.getNome().equals("ReadAllExecutionPeriodsForEnrollment")) {
			return true;
		} else if(servico.getNome().equals("ReadExecutionPeriodByOIDForEnrollment")) {
			return true;
		} else {
			return false;
		}
	}
}