package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;

class PrescriptionRuleFourEntries extends PrescriptionRuleGeneric {

    public PrescriptionRuleFourEntries() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
		.getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(119.5));
	setNumberOfEntriesStudentInSecretary(4);

    }

    public PrescriptionRuleFourEntries(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }
}
