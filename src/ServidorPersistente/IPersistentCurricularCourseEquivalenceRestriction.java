/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import Dominio.ICurricularCourseEquivalenceRestriction;

/**
 * @author dcs-rjao
 *
 * 22/Jul/2003
 */
public interface IPersistentCurricularCourseEquivalenceRestriction extends IPersistentObject {
	public  void deleteAll() throws ExcepcaoPersistencia;
	public void delete(ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition) throws ExcepcaoPersistencia;
}