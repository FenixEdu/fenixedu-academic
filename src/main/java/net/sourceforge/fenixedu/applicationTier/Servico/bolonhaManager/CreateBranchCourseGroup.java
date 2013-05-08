package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixWebFramework.services.Service;

public class CreateBranchCourseGroup {

    @Service
    public static void run(final Integer degreeCurricularPlanID, final Integer parentCourseGroupID, final String name,
            final String nameEn, final BranchType branchType, final Integer beginExecutionPeriodID,
            final Integer endExecutionPeriodID) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan =
                RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup =
                (CourseGroup) RootDomainObject.getInstance().readDegreeModuleByOID(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = RootDomainObject.getInstance().readExecutionSemesterByOID(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : RootDomainObject.getInstance().readExecutionSemesterByOID(
                        endExecutionPeriodID);

        degreeCurricularPlan.createBranchCourseGroup(parentCourseGroup, name, nameEn, branchType, beginExecutionPeriod,
                endExecutionPeriod);
    }
}
