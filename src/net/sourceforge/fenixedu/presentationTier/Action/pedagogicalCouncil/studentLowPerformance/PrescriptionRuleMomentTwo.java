package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentTwo extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentTwo() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(40));
	setNumberOfEntriesStudentInSecretary(1);
	setPrescriptionEnum(PrescriptionEnum.MOMENT2);
	setMonth(7);
    }

    public PrescriptionRuleMomentTwo(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary, int month) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary, month);

    }
}
