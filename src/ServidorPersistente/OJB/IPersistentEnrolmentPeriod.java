/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentEnrolmentPeriod extends IPersistentObject{

	/**
	 * @param degreeCurricularPlan
	 * @return
	 */
	IEnrolmentPeriod readActualEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;


}
