package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.Role;

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
    public PersonRole readByPersonAndRole(Person person, Role role) throws ExcepcaoPersistencia;

}