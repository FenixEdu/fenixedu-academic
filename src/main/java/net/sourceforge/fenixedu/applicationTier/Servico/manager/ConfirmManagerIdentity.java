/*
 * Created on 2004/07/18
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ConfirmManagerIdentity {

    public ConfirmManagerIdentity() {
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static Boolean run() {
        // Authentication is taken care of by the filters.
        return new Boolean(true);
    }

}