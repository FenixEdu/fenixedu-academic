package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IMasterDegreeCurricularPlanStrategy extends IDegreeCurricularPlanStrategy {

	/**
	 * Checks if the Master Degree Student has finished his scholar part.<br>
	 * All his credits are added and compared to the ones required by his Degree Curricular Plan.
	 * @param The Student's Curricular Plan
	 * @return A boolean indicating if he has fineshed it or not.
	 */
	public boolean checkEndOfScholarship(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

}