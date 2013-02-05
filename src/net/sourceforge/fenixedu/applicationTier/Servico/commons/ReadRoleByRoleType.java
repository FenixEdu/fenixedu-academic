/*
 * Created on Nov 10, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;

public class ReadRoleByRoleType extends FenixService {

    @Service
    public static Role run(RoleType roleType) {
        return Role.getRoleByRoleType(roleType);
    }
}