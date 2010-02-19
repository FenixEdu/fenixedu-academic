package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.PrescriptionBean.PrescriptionEnum;

class PrescriptionRuleMomentTree extends AbstractPrescriptionRule {

    public PrescriptionRuleMomentTree() {
	super();
	setRegistrationStart(ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getPreviousExecutionYear());
	setMinimumEcts(new BigDecimal(55));
	setNumberOfEntriesStudentInSecretary(2);
	setPrescriptionEnum(PrescriptionEnum.MOMENT3);
    }

    public PrescriptionRuleMomentTree(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);

    }

    @Override
    public boolean isOcursInMonth() {
	return false;
    }
}
