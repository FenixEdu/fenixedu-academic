/*
 * Created on 11/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB.gesdis;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.gesdis.CourseReport;
import Dominio.gesdis.ICourseReport;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseReportOJB extends PersistentObjectOJB implements IPersistentCourseReport {

    /**
     *  
     */
    public CourseReportOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.gesdis.IPersistentCourseReport#readCourseReportByExecutionCourse(Dominio.IDisciplinaExecucao)
     */
    public ICourseReport readCourseReportByExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionCourse.idInternal", executionCourse.getIdInternal());
        return (ICourseReport) queryObject(CourseReport.class, criteria);
    }

    public void delete(ICourseReport courseReport) throws ExcepcaoPersistencia {
        super.delete(courseReport);
    }
}