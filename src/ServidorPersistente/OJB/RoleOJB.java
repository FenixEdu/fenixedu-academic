package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IRole;
import Dominio.Role;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRole;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleOJB extends PersistentObjectOJB implements IPersistentRole {

    public RoleOJB() {
        super();
    }

    public IRole readByRoleType(RoleType roleType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("roleType", new Integer(roleType.getValue()));
        return (IRole) queryObject(Role.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Role.class, null);
    }

}