/*
 * Created on 18/Fev/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gesdis;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentStudentCourseReport;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class StudentCourseReportOJB extends PersistentObjectOJB implements
        IPersistentStudentCourseReport {

    /**
     *  
     */
    public StudentCourseReportOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.gesdis.IPersistentCourseReport#readCourseReportByExecutionCourse(Dominio.IDisciplinaExecucao)
     */
    public IStudentCourseReport readByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return (IStudentCourseReport) queryObject(StudentCourseReport.class, criteria);
    }
}