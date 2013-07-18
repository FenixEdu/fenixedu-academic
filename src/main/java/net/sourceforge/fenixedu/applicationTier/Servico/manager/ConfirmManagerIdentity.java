/*
 * Created on 2004/07/18
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class ConfirmManagerIdentity {

    public ConfirmManagerIdentity() {
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static Boolean run() {
        // Authentication is taken care of by the filters.
        return new Boolean(true);
    }

}