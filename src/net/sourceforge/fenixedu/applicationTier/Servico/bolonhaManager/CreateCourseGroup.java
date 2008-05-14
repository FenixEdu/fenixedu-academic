package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class CreateCourseGroup extends Service {

    public void run(final Integer degreeCurricularPlanID, final Integer parentCourseGroupID, final String name,
	    final String nameEn, final Integer beginExecutionPeriodID, final Integer endExecutionPeriodID)
	    throws FenixServiceException {

	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	if (degreeCurricularPlan == null) {
	    throw new FenixServiceException("error.noDegreeCurricularPlan");
	}
	final CourseGroup parentCourseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(parentCourseGroupID);
	if (parentCourseGroup == null) {
	    throw new FenixServiceException("error.noCourseGroup");
	}

	final ExecutionSemester beginExecutionPeriod;
	if (beginExecutionPeriodID == null) {
	    beginExecutionPeriod = ExecutionSemester.readActualExecutionPeriod();
	} else {
	    beginExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(beginExecutionPeriodID);
	}

	final ExecutionSemester endExecutionPeriod = (endExecutionPeriodID == null) ? null : rootDomainObject
		.readExecutionSemesterByOID(endExecutionPeriodID);

	degreeCurricularPlan.createCourseGroup(parentCourseGroup, name, nameEn, beginExecutionPeriod, endExecutionPeriod);
    }
}
