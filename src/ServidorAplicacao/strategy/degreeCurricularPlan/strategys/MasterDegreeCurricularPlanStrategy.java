package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.List;

import Dominio.IDegreeCurricularPlan;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
 
public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements IDegreeCurricularPlanStrategy {

	public MasterDegreeCurricularPlanStrategy() {
	}


	/**
	 * Checks if the Master Degree Student has finished his scholar part.<br>
	 * All his credits are added and compared to the ones required by his Degree Curricular Plan.
	 * @param A List of Enrolments
	 * @return A boolean indicating if he has fineshed it or not.
	 */
	public boolean checkEndOfScholarship(List enrolmentList){
		boolean result = false;
		
		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();
		Double nedeedCredits = degreeCurricularPlan.getNeededCredits();
		
		
		return result;
	}

}