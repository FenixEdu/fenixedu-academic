package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonRole;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonRole;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRoleOJB extends PersistentObjectOJB implements IPersistentPersonRole {

    public PersonRoleOJB() {
        super();
    }

    public IPersonRole readByPersonAndRole(IPerson person, IRole role) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("role.roleType", role.getRoleType());
        return (IPersonRole) queryObject(PersonRole.class, crit);

    }

}