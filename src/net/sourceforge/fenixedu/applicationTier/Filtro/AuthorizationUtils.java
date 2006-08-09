/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public abstract class AuthorizationUtils {

    /**
     * @param collection
     * @return boolean
     */
    public static boolean containsRole(Collection<Role> roles, RoleType roleType) {
    	for (final Role otherRole : roles) {
    		if (otherRole.getRoleType() == roleType) {
    			return true;
    		}
    	}
    	return false;
    }

}