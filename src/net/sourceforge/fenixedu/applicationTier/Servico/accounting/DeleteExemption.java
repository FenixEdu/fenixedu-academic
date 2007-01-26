package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accounting.Exemption;

public class DeleteExemption extends Service {

    public DeleteExemption() {
	super();
    }

    public void run(final Exemption exemption) {
	exemption.delete();
    }

}
