/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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

    private ExecutionPeriod getBeginExecutionPeriod(Integer beginExecutionPeriodID) throws FenixServiceException {
	final ExecutionPeriod beginExecutionPeriod;
	if (beginExecutionPeriodID == null) {
	    final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	    final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
	    beginExecutionPeriod = (nextExecutionYear == null) ? currentExecutionYear.getFirstExecutionPeriod()
		    : nextExecutionYear.getFirstExecutionPeriod();
	} else {
	    beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
	}
	return beginExecutionPeriod;
    }

    private ExecutionPeriod getEndExecutionPeriod(Integer endExecutionPeriodID) {
	return (endExecutionPeriodID == null) ? null : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);
    }
}