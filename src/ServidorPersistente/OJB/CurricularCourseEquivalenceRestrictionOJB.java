package ServidorPersistente.OJB;

import Dominio.ICurricularCourseEquivalenceRestriction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseEquivalenceRestriction;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseEquivalenceRestrictionOJB extends ObjectFenixOJB implements IPersistentCurricularCourseEquivalenceRestriction {

	

	public void delete(ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourseEquivalenceRestricition);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

}