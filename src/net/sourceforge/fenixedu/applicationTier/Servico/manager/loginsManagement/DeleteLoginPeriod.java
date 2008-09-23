package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.LoginPeriod;

public class DeleteLoginPeriod extends FenixService {

    public void run(LoginPeriod loginPeriod) {
	if (loginPeriod != null) {
	    loginPeriod.delete();
	}
    }
}
