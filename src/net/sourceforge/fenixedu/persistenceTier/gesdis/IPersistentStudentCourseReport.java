/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentStudentCourseReport extends IPersistentObject {

    public StudentCourseReport readByCurricularCourse(CurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;
}