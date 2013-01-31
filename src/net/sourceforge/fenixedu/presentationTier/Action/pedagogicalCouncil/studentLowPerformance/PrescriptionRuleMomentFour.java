package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentFour extends PrescriptionRuleGenericMoment {

	public PrescriptionRuleMomentFour() {
		super();

	}

	@Override
	public BigDecimal getMinimumEcts() {
		return new BigDecimal(45);
	}

	@Override
	public PrescriptionEnum getPrescriptionEnum() {
		return PrescriptionEnum.MOMENT4;
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
