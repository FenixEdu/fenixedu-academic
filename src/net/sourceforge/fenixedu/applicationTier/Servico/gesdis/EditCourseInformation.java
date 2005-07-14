/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jdnf and mrsp
 */
public class EditCourseInformation implements IService {

    public void run(Integer courseReportID, String newReport) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentCourseReport persistentCourseReport = persistentSupport
                .getIPersistentCourseReport();

        final ICourseReport courseReport = (ICourseReport) persistentCourseReport.readByOID(
                CourseReport.class, courseReportID);
        if (courseReport == null)
            throw new InvalidArgumentsServiceException();

        courseReport.edit(newReport);
    }
}