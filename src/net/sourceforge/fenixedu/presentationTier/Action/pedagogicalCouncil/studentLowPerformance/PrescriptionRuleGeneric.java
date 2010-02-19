package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.PrescriptionBean.PrescriptionEnum;

class PrescriptionRuleGeneric extends AbstractPrescriptionRule {

    public PrescriptionRuleGeneric() {
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
		.getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(119.5));
	setNumberOfEntriesStudentInSecretary(4);
	setPrescriptionEnum(PrescriptionEnum.ALLPRESCRIPTION);
    }

    public PrescriptionRuleGeneric(ExecutionYear registrationStart, BigDecimal minimumEcts, int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }
}
