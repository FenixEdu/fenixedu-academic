/*
 * Created on 18/Fev/2004
 */
package ServidorPersistente.OJB.gesdis;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.gesdis.IStudentCourseReport;
import Dominio.gesdis.StudentCourseReport;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.gesdis.IPersistentStudentCourseReport;

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