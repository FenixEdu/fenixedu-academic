package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.IRole;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentPersonRole extends IPersistentObject {

    /**
     * 
     * @param person
     * @param role
     * @return IPersonRole
     * @throws ExcepcaoPersistencia
     */
    public IPersonRole readByPersonAndRole(IPerson person, IRole role) throws ExcepcaoPersistencia;

}