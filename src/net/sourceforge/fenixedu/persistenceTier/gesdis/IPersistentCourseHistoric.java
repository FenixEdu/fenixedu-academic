/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCourseHistoric extends IPersistentObject {

    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester)
            throws ExcepcaoPersistencia;
}