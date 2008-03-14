/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCourseGroup extends Service {

    public void run(final Integer courseGroupID, final Integer contextID, final String name, final String nameEn,
	    final Integer beginExecutionPeriodID, final Integer endExecutionPeriodID) throws ExcepcaoPersistencia,
	    FenixServiceException {

	final CourseGroup courseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(courseGroupID);
	if (courseGroup == null) {
	    throw new FenixServiceException("error.noCourseGroup");
	}
	final Context context = rootDomainObject.readContextByOID(contextID);
	if (context == null && !courseGroup.isRoot()) {
	    throw new FenixServiceException("error.noContext");
	}

	courseGroup.edit(name, nameEn, context, getBeginExecutionPeriod(beginExecutionPeriodID),
		getEndExecutionPeriod(endExecutionPeriodID));
    }

    private ExecutionPeriod getBeginExecutionPeriod(final Integer beginExecutionPeriodID) {
	if (beginExecutionPeriodID == null) {
	    return ExecutionPeriod.readActualExecutionPeriod();
	} else {
	    return rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
	}
    }

    private ExecutionPeriod getEndExecutionPeriod(Integer endExecutionPeriodID) {
	return (endExecutionPeriodID == null) ? null : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);
    }
}
