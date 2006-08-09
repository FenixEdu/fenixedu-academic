/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateProject extends Service {

    public void run(Integer executionCourseID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Integer groupingID)
            throws ExcepcaoPersistencia, FenixServiceException {

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final Grouping grouping = (groupingID != null) ? rootDomainObject.readGroupingByOID(groupingID)
                : null;

        new Project(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep,
                grouping, executionCourse);
    }
}
