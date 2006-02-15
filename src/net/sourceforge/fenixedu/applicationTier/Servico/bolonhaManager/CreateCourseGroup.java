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
            final String name, final String nameEn) throws ExcepcaoPersistencia, FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup = (CourseGroup) persistentObject.readByOID(
                CourseGroup.class, parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        // TODO: this should be modified to receive ExecutionYear, but for now
        // we just read the '2006/2007'
        final ExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear()
                .readExecutionYearByName("2006/2007");
        if (executionYear == null) {
            throw new FenixServiceException("error.noExecutionYear");
        }
        final ExecutionPeriod beginExecutionPeriod = executionYear.getExecutionPeriodForSemester(Integer
                .valueOf(1));

        degreeCurricularPlan.createCourseGroup(parentCourseGroup, name, nameEn, null, beginExecutionPeriod, null);
    }
    
}
