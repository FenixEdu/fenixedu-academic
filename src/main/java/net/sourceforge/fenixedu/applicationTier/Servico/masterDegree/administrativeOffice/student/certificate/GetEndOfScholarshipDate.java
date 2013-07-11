package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEndOfScholarshipDate {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static Date run(String studentCurricularPlanID) {
        StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanID);

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
                DegreeCurricularPlanStrategyFactory.getInstance();

        IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy =
                (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                        .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

        return masterDegreeCurricularPlanStrategy.dateOfEndOfScholarship(studentCurricularPlan);

    }

}