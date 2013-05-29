package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateCourseGroup {

    @Service
    public static void run(final String degreeCurricularPlanID, final String parentCourseGroupID, final String name,
            final String nameEn, final String beginExecutionPeriodID, final String endExecutionPeriodID)
            throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup = (CourseGroup) AbstractDomainObject.fromExternalId(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = AbstractDomainObject.fromExternalId(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : AbstractDomainObject
                        .<ExecutionSemester> fromExternalId(endExecutionPeriodID);

        degreeCurricularPlan.createCourseGroup(parentCourseGroup, name, nameEn, beginExecutionPeriod, endExecutionPeriod);
    }
}
