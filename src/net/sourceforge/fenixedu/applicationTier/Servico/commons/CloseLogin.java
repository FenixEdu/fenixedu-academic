package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Login;

public class CloseLogin extends Service {

    public void run(Login login) {
	login.closeLoginIfNecessary();
    }
}
