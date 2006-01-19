/*
 * Created on Nov 10, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadRoleByRoleType implements IService {

    public Role run(RoleType roleType) throws ExcepcaoPersistencia{
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();        
        return sp.getIPersistentRole().readByRoleType(roleType);              
    }
}
