package ServidorPersistente.OJB;

import Dominio.ICurricularCourseEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseEquivalence;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseEquivalenceOJB extends ObjectFenixOJB implements IPersistentCurricularCourseEquivalence{

	
	
	public void delete(ICurricularCourseEquivalence curricularCourseEquivalence) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourseEquivalence);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}
}