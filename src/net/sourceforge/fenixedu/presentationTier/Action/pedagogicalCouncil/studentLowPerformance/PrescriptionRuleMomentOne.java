package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentOne extends AbstractPrescriptionRule {

    public PrescriptionRuleMomentOne() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(30));
	setNumberOfEntriesStudentInSecretary(1);
	setPrescriptionEnum(PrescriptionEnum.MOMENT1);
    }

    public PrescriptionRuleMomentOne(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }

    @Override
    public boolean isOcursInMonth() {
	return false;
    }

}
