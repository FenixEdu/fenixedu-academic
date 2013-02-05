package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Login;
import pt.ist.fenixWebFramework.services.Service;

public class CloseLogin extends FenixService {

    @Service
    public static void run(Login login) {
        login.closeLoginIfNecessary();
    }
}