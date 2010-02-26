package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.domain.student.Registration;

class PrescriptionRuleMomentTree extends PrescriptionRuleGenericMoment {

    public PrescriptionRuleMomentTree() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(55));
	setNumberOfEntriesStudentInSecretary(2);
	setPrescriptionEnum(PrescriptionEnum.MOMENT3);
	setMonth(9);
    }

    public PrescriptionRuleMomentTree(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary, int month) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary, month);

    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return super.isPrescript(registration, ects, numberOfEntriesStudentInSecretary)
		&& registration.isFullRegime(getRegistrationStart());
    }
}
