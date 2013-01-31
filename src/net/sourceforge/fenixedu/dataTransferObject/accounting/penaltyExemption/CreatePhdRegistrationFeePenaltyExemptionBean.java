package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFee;

public class CreatePhdRegistrationFeePenaltyExemptionBean extends CreatePenaltyExemptionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public CreatePhdRegistrationFeePenaltyExemptionBean(final PhdRegistrationFee event) {
		super(event);
	}

	@Override
	public PhdRegistrationFee getEvent() {
		return (PhdRegistrationFee) super.getEvent();
	}
}
