/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;

/**
 * @author jpvl
 */
public interface IPersistentRestriction extends IPersistentObject {

	/**
	 * @param curricularCourse
	 * @return
	 */
	List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}
