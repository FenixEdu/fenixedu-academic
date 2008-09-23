package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Login;

public class CloseLogin extends FenixService {

    public void run(Login login) {
	login.closeLoginIfNecessary();
    }
}
