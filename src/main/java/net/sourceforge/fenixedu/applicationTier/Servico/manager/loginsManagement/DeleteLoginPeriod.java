package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.LoginPeriod;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteLoginPeriod {

    @Atomic
    public static void run(LoginPeriod loginPeriod) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        if (loginPeriod != null) {
            loginPeriod.delete();
        }
    }
}