/*
 * Created on 22/Jul/2003
 *
 */
package ServidorPersistente;

import Dominio.ICurricularCourseEquivalence;

/**
 * @author dcs-rjao
 *
 * 22/Jul/2003
 */
public interface IPersistentCurricularCourseEquivalence {
	
	public void delete(ICurricularCourseEquivalence curricularCourseEquivalence) throws ExcepcaoPersistencia;
}