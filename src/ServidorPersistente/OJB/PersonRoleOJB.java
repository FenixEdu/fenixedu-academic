package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.PersonRole;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonRole;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRoleOJB extends PersistentObjectOJB implements IPersistentPersonRole {

    public PersonRoleOJB() {
        super();
    }

    public IPersonRole readByPersonAndRole(IPessoa person, IRole role) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("role.roleType", new Integer(role.getRoleType().getValue()));
        return (IPersonRole) queryObject(PersonRole.class, crit);

    }

}