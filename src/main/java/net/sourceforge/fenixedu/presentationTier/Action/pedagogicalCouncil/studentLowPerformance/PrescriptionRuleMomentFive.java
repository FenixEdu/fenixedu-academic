package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentFive extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentFive() {
        super();
    }

    @Override
    public BigDecimal getMinimumEcts() {
        return new BigDecimal(55);
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
        return PrescriptionEnum.MOMENT5;
    }

    @Override
    public int getNumberOfEntriesStudentInSecretary() {
        return 3;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
        return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear();
    }

}
