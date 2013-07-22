/*
 * Created on 2004/07/18
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ConfirmManagerIdentity {

    public ConfirmManagerIdentity() {
    }

    @Atomic
    public static Boolean run() {
        check(RolePredicates.MANAGER_PREDICATE);
        // Authentication is taken care of by the filters.
        return new Boolean(true);
    }

}