/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateCourseGroup implements IService {

    public void run(final Integer parentCourseGroupID, final String name) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ICourseGroup parentCourseGroup = (ICourseGroup) persistentSupport.getIPersistentObject()
                .readByOID(CourseGroup.class, parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final IExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear()
                .readExecutionYearByName("2006/2007");
        final IExecutionPeriod beginExecutionPeriod = executionYear
                .getExecutionPeriodForSemester(Integer.valueOf(1));

        final ICourseGroup courseGroup = DomainFactory.makeCourseGroup(name);
        courseGroup.addContext(parentCourseGroup, null, beginExecutionPeriod, null);
    }
}
