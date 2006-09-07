package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCourseGroup extends Service {

    public void run(final Integer degreeCurricularPlanID, final Integer parentCourseGroupID,
            final String name, final String nameEn, final Integer beginExecutionPeriodID,
            final Integer endExecutionPeriodID) throws ExcepcaoPersistencia, FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final ExecutionPeriod beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
        	beginExecutionPeriod = currentExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
            } else {
        	beginExecutionPeriod = nextExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
            }
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
        }

        final ExecutionPeriod endExecutionPeriod = (endExecutionPeriodID == null) ? null
                : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);

        degreeCurricularPlan.createCourseGroup(parentCourseGroup, name, nameEn, null, beginExecutionPeriod, endExecutionPeriod);
    }
}
