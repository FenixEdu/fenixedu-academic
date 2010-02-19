package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;

class PrescriptionRuleFiveBolonhaEntries extends PrescriptionRuleGeneric {
    // Valid until 2009_2010
    public PrescriptionRuleFiveBolonhaEntries() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear()
		.getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(179.5));
	setNumberOfEntriesStudentInSecretary(5);
    }

    public PrescriptionRuleFiveBolonhaEntries(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }

    // MinEcts >=
    @Override
    public boolean isPrescript(BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0
		&& numberOfEntriesStudentInSecretary >= getNumberOfEntriesStudentInSecretary();
    }
}
