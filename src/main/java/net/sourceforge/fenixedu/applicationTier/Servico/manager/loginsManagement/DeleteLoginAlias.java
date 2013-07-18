package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;


import net.sourceforge.fenixedu.domain.LoginAlias;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteLoginAlias {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(LoginAlias loginAlias) {
        if (loginAlias != null) {
            loginAlias.delete();
        }
    }
}