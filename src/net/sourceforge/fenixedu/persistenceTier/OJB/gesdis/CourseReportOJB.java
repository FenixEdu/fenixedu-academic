/*
 * Created on 11/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gesdis;

import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseReportOJB extends PersistentObjectOJB implements IPersistentCourseReport {

    public ICourseReport readCourseReportByExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionCourse.idInternal", executionCourseOID);
        return (ICourseReport) queryObject(CourseReport.class, criteria);
    }
}