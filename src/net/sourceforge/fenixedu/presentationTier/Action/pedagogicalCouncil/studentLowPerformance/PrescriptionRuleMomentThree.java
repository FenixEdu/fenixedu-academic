package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.domain.student.Registration;

class PrescriptionRuleMomentThree extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentThree() {
	super();
    }

    @Override
    public BigDecimal getMinimumEcts() {
	return new BigDecimal(55);
    }

    @Override
    public PrescriptionEnum getPrescriptionEnum() {
	return PrescriptionEnum.MOMENT3;
    }

    @Override
    public ExecutionYear getRegistrationStart() {
	return ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear();
    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return super.isPrescript(registration, ects, numberOfEntriesStudentInSecretary)
		&& registration.isFullRegime(getRegistrationStart());
    }
}
