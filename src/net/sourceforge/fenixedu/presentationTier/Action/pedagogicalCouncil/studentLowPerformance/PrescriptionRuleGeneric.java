package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleGeneric extends AbstractPrescriptionRule {

    public PrescriptionRuleGeneric() {
    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(119.5);
    }

    @Override
    public int getNumberOfEntriesStudentInSecretary() {
	return 4;
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
	return PrescriptionEnum.ALLPRESCRIPTION;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
	return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
		.getPreviousExecutionYear();
    }

}
