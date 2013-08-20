/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AddContextToCourseGroup {

    @Service
    public static void run(CourseGroup courseGroup, CourseGroup parentCourseGroup, String beginExecutionPeriodID,
            String endExecutionPeriodID) throws FenixServiceException {

        if (courseGroup == null || parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        if (courseGroup.isRoot()) {
            throw new FenixServiceException("error.cannotAddContextToRoot");
        }
        parentCourseGroup.addContext(courseGroup, null, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }

    private static ExecutionSemester getBeginExecutionPeriod(final String beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            return AbstractDomainObject.fromExternalId(beginExecutionPeriodID);
        }
    }

    private static ExecutionSemester getEndExecutionPeriod(String endExecutionPeriodID) {
        return AbstractDomainObject.fromExternalId(endExecutionPeriodID);
    }
}