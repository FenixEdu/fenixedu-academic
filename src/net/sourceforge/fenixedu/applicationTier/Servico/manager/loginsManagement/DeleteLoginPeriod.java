package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.LoginPeriod;

public class DeleteLoginPeriod extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(LoginPeriod loginPeriod) {
	if (loginPeriod != null) {
	    loginPeriod.delete();
	}
    }
}