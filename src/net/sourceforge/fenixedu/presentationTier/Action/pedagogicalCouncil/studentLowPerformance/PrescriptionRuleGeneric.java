package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.domain.student.Registration;

class PrescriptionRuleGeneric extends AbstractPrescriptionRule {

    public PrescriptionRuleGeneric() {
    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0
		&& numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary();
    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(119.5);
    }

    @Override
    protected int getNumberOfEntriesStudentInSecretary() {
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
