/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;

/**
 * @author jpvl
 */
public interface IPersistentPrecedence extends IPersistentObject {

    /**
     * @param plan
     * @return
     */
    List readByDegreeCurricularPlan(IDegreeCurricularPlan plan) throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @return
     */
    List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}