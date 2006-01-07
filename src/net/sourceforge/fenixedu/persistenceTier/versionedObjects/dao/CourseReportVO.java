/*
 * Created on May 10, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class CourseReportVO extends VersionedObjectsBase implements IPersistentCourseReport {

    public CourseReport readCourseReportByExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);

        return (executionCourse != null) ? executionCourse.getCourseReport() : null;
    }

}
