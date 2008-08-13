/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jdnf and mrsp
 */
public class EditCourseInformation extends Service {

    public void run(Integer courseReportID, InfoCourseReport infoCourseReport, String newReport) throws
            FenixServiceException {
        final CourseReport courseReport;
        if (courseReportID != 0) {
            courseReport = rootDomainObject.readCourseReportByOID(courseReportID);
        } else {
            final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoCourseReport.getInfoExecutionCourse().getIdInternal());

            courseReport = executionCourse.createCourseReport(newReport);
        }

        if (courseReport == null)
            throw new FenixServiceException();

        courseReport.edit(newReport);
    }

}
