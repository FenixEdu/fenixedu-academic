package ServidorPersistente.OJB;

import Dominio.CurricularCourseEquivalenceRestriction;
import Dominio.ICurricularCourseEquivalenceRestriction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseEquivalenceRestriction;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseEquivalenceRestrictionOJB extends ObjectFenixOJB implements IPersistentCurricularCourseEquivalenceRestriction {

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularCourseEquivalenceRestriction.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void delete(ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourseEquivalenceRestricition);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

}