package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;

public class CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean extends
	CreatePenaltyExemptionBean implements Serializable {

    public CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean(
	    final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent) {
	super(improvementOfApprovedEnrolmentEvent);
    }

}
