/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.gesdis;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentStudentCourseReport extends IPersistentObject {

    public IStudentCourseReport readByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;
}