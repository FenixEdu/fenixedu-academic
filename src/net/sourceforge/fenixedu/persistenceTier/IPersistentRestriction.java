/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author jpvl
 * @author David Santos in Aug 2, 2004
 */

public interface IPersistentRestriction extends IPersistentObject {

    public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse, Class clazz)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}