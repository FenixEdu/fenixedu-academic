/*
 * Created on Nov 10, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRoleByRoleType extends Service {

    public Role run(RoleType roleType) {
	return Role.getRoleByRoleType(roleType);
    }
}
