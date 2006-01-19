/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author jdnf and mrsp
 */
public class EditCourseInformation implements IService {

    public void run(Integer courseReportID, InfoCourseReport infoCourseReport, String newReport) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentCourseReport persistentCourseReport = persistentSupport
                .getIPersistentCourseReport();

        final CourseReport courseReport;
        if (courseReportID != 0) {
            courseReport = (CourseReport) persistentCourseReport.readByOID(CourseReport.class, courseReportID);
        } else {
            final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, infoCourseReport.getInfoExecutionCourse().getIdInternal());

            courseReport = executionCourse.createCourseReport(newReport);
        }

        if (courseReport == null)
            throw new FenixServiceException();

        courseReport.edit(newReport);
    }

}
