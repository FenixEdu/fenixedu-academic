package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleFiveEntries extends PrescriptionRuleGeneric {

	public PrescriptionRuleFiveEntries() {
		super();
	}

	@Override
	public BigDecimal getMinimumEcts() {
		return new BigDecimal(179.5);
	}

	@Override
	protected int getNumberOfEntriesStudentInSecretary() {
		return 5;
	}

	@Override
	public PrescriptionEnum getPrescriptionEnum() {
		return PrescriptionEnum.ALLPRESCRIPTION;
	}

	@Override
	public ExecutionYear getRegistrationStart() {
		return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
				.getPreviousExecutionYear().getPreviousExecutionYear();
	}
}
