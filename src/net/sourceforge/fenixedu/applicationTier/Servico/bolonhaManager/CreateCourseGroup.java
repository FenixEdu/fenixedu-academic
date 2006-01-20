/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCourseGroup extends Service {

    public void run(final Integer parentCourseGroupID, final String name) throws ExcepcaoPersistencia,
            FenixServiceException {

        final CourseGroup parentCourseGroup = (CourseGroup) persistentSupport.getIPersistentObject()
                .readByOID(CourseGroup.class, parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final ExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear()
                .readExecutionYearByName("2006/2007");
        final ExecutionPeriod beginExecutionPeriod = executionYear.getExecutionPeriodForSemester(Integer
                .valueOf(1));

        final CourseGroup courseGroup = DomainFactory.makeCourseGroup(name);
        courseGroup.addContext(parentCourseGroup, null, beginExecutionPeriod, null);
    }
}
