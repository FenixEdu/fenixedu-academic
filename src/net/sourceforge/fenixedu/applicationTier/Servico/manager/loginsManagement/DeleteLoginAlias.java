package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.LoginAlias;

public class DeleteLoginAlias extends FenixService {

    public void run(LoginAlias loginAlias) {
	if (loginAlias != null) {
	    loginAlias.delete();
	}
    }
}
