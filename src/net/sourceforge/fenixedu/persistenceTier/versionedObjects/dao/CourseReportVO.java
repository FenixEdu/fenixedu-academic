/*
 * Created on May 10, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class CourseReportVO extends VersionedObjectsBase implements IPersistentCourseReport {

    public ICourseReport readCourseReportByExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);

        return (executionCourse != null) ? executionCourse.getCourseReport() : null;
    }

}
