/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public abstract class AuthorizationUtils {

    /**
     * @param collection
     * @return boolean
     */
    public static boolean containsRole(Collection roles, RoleType role) {
        Iterator rolesIterator = roles.iterator();
        while (rolesIterator.hasNext()) {
            InfoRole infoRole = (InfoRole) rolesIterator.next();
            if (infoRole.getRoleType().equals(role))
                return true;
        }
        return false;
    }
}