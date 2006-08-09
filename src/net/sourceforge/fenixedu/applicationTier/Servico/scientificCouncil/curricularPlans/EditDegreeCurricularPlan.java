package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public class EditDegreeCurricularPlan extends Service {

    public void run(Integer dcpId, String name, CurricularStage curricularStage,
            DegreeCurricularPlanState degreeCurricularPlanState, GradeScale gradeScale,
            Integer executionYearID) throws FenixServiceException {

        if (dcpId == null || name == null || curricularStage == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DegreeCurricularPlan dcpToEdit = rootDomainObject.readDegreeCurricularPlanByOID(dcpId);
        if (dcpToEdit == null) {
            throw new FenixServiceException(
                    "error.degreeCurricularPlan.no.existing.degreeCurricularPlan");
        }

        final ExecutionYear executionYear = (executionYearID == null) ? null : rootDomainObject
                .readExecutionYearByOID(executionYearID);

        dcpToEdit.edit(name, curricularStage, degreeCurricularPlanState, gradeScale, executionYear);
    }

}
