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
public interface IPersistentRestrictionByCurricularCourse extends IPersistentRestriction {

	/**
	 * @param curricularCourse
	 * @return
	 */
	public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse,Class clazz) throws ExcepcaoPersistencia;
	public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}
