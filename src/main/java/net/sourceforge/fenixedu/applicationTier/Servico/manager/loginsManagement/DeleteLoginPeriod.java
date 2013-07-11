package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;


import net.sourceforge.fenixedu.domain.LoginPeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteLoginPeriod {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(LoginPeriod loginPeriod) {
        if (loginPeriod != null) {
            loginPeriod.delete();
        }
    }
}