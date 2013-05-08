/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixWebFramework.services.Service;

public class AddContextToCourseGroup {

    @Service
    public static void run(CourseGroup courseGroup, CourseGroup parentCourseGroup, Integer beginExecutionPeriodID,
            Integer endExecutionPeriodID) throws FenixServiceException {

        if (courseGroup == null || parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }
        if (courseGroup.isRoot()) {
            throw new FenixServiceException("error.cannotAddContextToRoot");
        }
        parentCourseGroup.addContext(courseGroup, null, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }

    private static ExecutionSemester getBeginExecutionPeriod(final Integer beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            return RootDomainObject.getInstance().readExecutionSemesterByOID(beginExecutionPeriodID);
        }
    }

    private static ExecutionSemester getEndExecutionPeriod(Integer endExecutionPeriodID) {
        return (endExecutionPeriodID == null) ? null : RootDomainObject.getInstance().readExecutionSemesterByOID(
                endExecutionPeriodID);
    }
}