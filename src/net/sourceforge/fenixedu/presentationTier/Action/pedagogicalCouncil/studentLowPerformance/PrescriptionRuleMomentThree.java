package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentThree extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentThree() {
	super();
    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(40);
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
	return PrescriptionEnum.MOMENT3;
    }

    @Override
    public int getNumberOfEntriesStudentInSecretary() {
	return 2;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
	return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear();
    }

}
