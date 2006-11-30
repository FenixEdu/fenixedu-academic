package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;

public class DeleteGratuityExemption extends Service {

    public DeleteGratuityExemption() {
	super();
    }

    public void run(final GratuityExemption gratuityExemption) {
	gratuityExemption.delete();
    }

}
