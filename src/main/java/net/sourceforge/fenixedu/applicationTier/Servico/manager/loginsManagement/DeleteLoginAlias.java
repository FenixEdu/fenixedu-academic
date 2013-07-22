package net.sourceforge.fenixedu.applicationTier.Servico.manager.loginsManagement;


import net.sourceforge.fenixedu.domain.LoginAlias;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteLoginAlias {

    @Atomic
    public static void run(LoginAlias loginAlias) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        if (loginAlias != null) {
            loginAlias.delete();
        }
    }
}