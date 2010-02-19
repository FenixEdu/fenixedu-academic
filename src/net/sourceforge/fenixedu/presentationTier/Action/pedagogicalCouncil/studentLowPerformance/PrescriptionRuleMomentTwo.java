package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.PrescriptionBean.PrescriptionEnum;

class PrescriptionRuleMomentTwo extends AbstractPrescriptionRule {

    public PrescriptionRuleMomentTwo() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(40));
	setNumberOfEntriesStudentInSecretary(1);
	setPrescriptionEnum(PrescriptionEnum.MOMENT2);
    }

    public PrescriptionRuleMomentTwo(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }

    @Override
    public boolean isOcursInMonth() {
	return false;
    }
}
