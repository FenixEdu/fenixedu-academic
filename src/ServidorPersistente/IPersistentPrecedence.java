/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public interface IPersistentPrecedence extends IPersistentObject {


	/**
	 * @param plan
	 * @return
	 */
	List readByDegreeCurricularPlan(IDegreeCurricularPlan plan, PrecedenceScopeToApply scope) throws ExcepcaoPersistencia;

	/**
	 * @param curricularCourse
	 * @return
	 */
	List readByCurricularCourse(ICurricularCourse curricularCourse, PrecedenceScopeToApply scope) throws ExcepcaoPersistencia;

}
