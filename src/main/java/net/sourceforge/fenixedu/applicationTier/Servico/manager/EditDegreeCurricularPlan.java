package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanEditor;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditDegreeCurricularPlan {

    @Atomic
    public static void run(InfoDegreeCurricularPlanEditor infoDcp) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        if (infoDcp.getExternalId() == null || infoDcp.getName() == null || infoDcp.getInitialDate() == null
                || infoDcp.getDegreeDuration() == null || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DegreeCurricularPlan dcpToEdit = FenixFramework.getDomainObject(infoDcp.getExternalId());
        if (dcpToEdit == null) {
            throw new FenixServiceException("message.nonExistingDegreeCurricularPlan");
        }

        try {
            dcpToEdit.edit(infoDcp.getName(), infoDcp.getState(), infoDcp.getInitialDate(), infoDcp.getEndDate(),
                    infoDcp.getDegreeDuration(), infoDcp.getMinimalYearForOptionalCourses(), infoDcp.getNeededCredits(),
                    infoDcp.getMarkType(), infoDcp.getNumerusClausus(), infoDcp.getAnotation(), infoDcp.getGradeScale());
        } catch (DomainException e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

}