package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.IRole;
import Dominio.Role;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRole;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleOJB extends ObjectFenixOJB	implements IPersistentRole {

	public RoleOJB() {
		super();
	}
	

	public IRole readByRoleType(RoleType roleType) throws ExcepcaoPersistencia {
		try {

			String oqlQuery = "select all from " + Role.class.getName()
					+ " where roleType = $1";

			query.create(oqlQuery);
			query.bind(new Integer(roleType.getValue()));

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) return (IRole) result.get(0);
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAll() throws ExcepcaoPersistencia {
		return queryList(Role.class, null);
	}

}
