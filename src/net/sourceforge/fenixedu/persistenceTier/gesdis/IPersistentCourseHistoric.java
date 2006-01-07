/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCourseHistoric extends IPersistentObject {

    public List readByCurricularCourseAndSemester(CurricularCourse curricularCourse, Integer semester)
            throws ExcepcaoPersistencia;
}