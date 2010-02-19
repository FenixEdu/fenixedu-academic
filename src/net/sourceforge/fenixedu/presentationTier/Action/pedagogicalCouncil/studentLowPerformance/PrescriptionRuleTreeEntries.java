package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;

class PrescriptionRuleTreeEntries extends PrescriptionRuleGeneric {

    public PrescriptionRuleTreeEntries() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(59.5));
	setNumberOfEntriesStudentInSecretary(3);
    }

    public PrescriptionRuleTreeEntries(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }

}
