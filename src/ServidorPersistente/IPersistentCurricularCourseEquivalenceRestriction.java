/*
 * Created on 22/Jul/2003
 *
 */
package ServidorPersistente;

import Dominio.ICurricularCourseEquivalenceRestriction;

/**
 * @author dcs-rjao
 *
 * 22/Jul/2003
 */
public interface IPersistentCurricularCourseEquivalenceRestriction extends IPersistentObject {
	
	public void delete(ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition) throws ExcepcaoPersistencia;
}