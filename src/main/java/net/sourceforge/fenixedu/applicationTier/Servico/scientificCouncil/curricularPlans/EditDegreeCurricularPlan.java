package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditDegreeCurricularPlan {

    @Atomic
    public static void run(String dcpId, String name, CurricularStage curricularStage,
            DegreeCurricularPlanState degreeCurricularPlanState, GradeScale gradeScale, String executionYearID)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (dcpId == null || name == null || curricularStage == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DegreeCurricularPlan dcpToEdit = FenixFramework.getDomainObject(dcpId);
        if (dcpToEdit == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.no.existing.degreeCurricularPlan");
        }

        final ExecutionYear executionYear =
                (executionYearID == null) ? null : FenixFramework.<ExecutionYear> getDomainObject(executionYearID);

        dcpToEdit.edit(name, curricularStage, degreeCurricularPlanState, gradeScale, executionYear);
    }

}