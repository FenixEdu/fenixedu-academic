/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

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

	/**
	 *  
	 * @param plan
	 * @return IEnrolmentPeriod next enrolment period if any. If doesn't exist other curricular plan then it returns null. 
	 */
	IEnrolmentPeriod readNextEnrolmentPeriodForDegreeCurricularPlan(IDegreeCurricularPlan plan) throws ExcepcaoPersistencia;


}
