/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;

/**
 * @author jpvl
 * @author David Santos in Aug 2, 2004
 */

public interface IPersistentRestriction extends IPersistentObject {

    public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse, Class clazz)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}