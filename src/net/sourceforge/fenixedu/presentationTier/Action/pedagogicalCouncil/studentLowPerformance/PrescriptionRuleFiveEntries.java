package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;

class PrescriptionRuleFiveEntries extends PrescriptionRuleGeneric {

    public PrescriptionRuleFiveEntries() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
		.getPreviousExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(179.5));
	setNumberOfEntriesStudentInSecretary(5);
    }

    public PrescriptionRuleFiveEntries(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }
}
