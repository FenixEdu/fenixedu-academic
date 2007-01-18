package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemption;

public class DeletePenaltyExemption extends Service {

    public DeletePenaltyExemption() {
	super();
    }

    public void run(final PenaltyExemption penaltyExemption) {
	penaltyExemption.delete();
    }

}
