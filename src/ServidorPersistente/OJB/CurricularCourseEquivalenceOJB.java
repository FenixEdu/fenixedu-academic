package ServidorPersistente.OJB;

import Dominio.CurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseEquivalence;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseEquivalenceOJB extends ObjectFenixOJB implements IPersistentCurricularCourseEquivalence{

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularCourseEquivalence.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}
	
	public void delete(ICurricularCourseEquivalence curricularCourseEquivalence) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourseEquivalence);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}
}