package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentOne extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentOne() {
	super();
    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(30);
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
	return PrescriptionEnum.MOMENT1;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
	return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear();
    }

}
