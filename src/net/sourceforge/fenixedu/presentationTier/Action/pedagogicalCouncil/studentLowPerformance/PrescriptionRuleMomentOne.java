package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;

class PrescriptionRuleMomentOne extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentOne() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(30));
	setNumberOfEntriesStudentInSecretary(1);
	setPrescriptionEnum(PrescriptionEnum.MOMENT1);
	setMonth(9);
    }

    public PrescriptionRuleMomentOne(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary, int month) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary, month);

    }

}
