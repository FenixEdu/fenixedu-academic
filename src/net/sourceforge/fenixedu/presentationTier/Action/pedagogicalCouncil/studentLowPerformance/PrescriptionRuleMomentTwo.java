package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentTwo extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentTwo() {
	super();

    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(40);
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
	return PrescriptionEnum.MOMENT2;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
	return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear();
    }

}
