/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddContextToCourseGroup extends Service {

    public void run(CourseGroup courseGroup, CourseGroup parentCourseGroup, Integer beginExecutionPeriodID,
	    Integer endExecutionPeriodID) throws ExcepcaoPersistencia, FenixServiceException {

	if (courseGroup == null || parentCourseGroup == null) {
	    throw new FenixServiceException("error.noCourseGroup");
	}
	if (courseGroup.isRoot()) {
	    throw new FenixServiceException("error.cannotAddContextToRoot");
	}
	parentCourseGroup.addContext(courseGroup, null, getBeginExecutionPeriod(beginExecutionPeriodID),
		getEndExecutionPeriod(endExecutionPeriodID));
    }

    private ExecutionSemester getBeginExecutionPeriod(final Integer beginExecutionPeriodID) {
	if (beginExecutionPeriodID == null) {
	    return ExecutionSemester.readActualExecutionSemester();
	} else {
	    return rootDomainObject.readExecutionSemesterByOID(beginExecutionPeriodID);
	}
    }

    private ExecutionSemester getEndExecutionPeriod(Integer endExecutionPeriodID) {
	return (endExecutionPeriodID == null) ? null : rootDomainObject.readExecutionSemesterByOID(endExecutionPeriodID);
    }
}