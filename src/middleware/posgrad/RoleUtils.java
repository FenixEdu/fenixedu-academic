/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.posgrad;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPersonRole;
import Dominio.IRole;
import Dominio.PersonRole;
import Dominio.Pessoa;
import Dominio.Role;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleUtils {


	public static IPersonRole readPersonRole(Pessoa person, RoleType roleType, PersistenceBroker broker) throws Exception {
		
		// Read The Role
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", roleType);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Unknown Role !!!");
		else role = (Role) result.get(0);
		
		
		criteria = new Criteria();
		criteria.addEqualTo("keyRole", role.getIdInternal());
		criteria.addEqualTo("keyPerson", person.getIdInternal());
		
		query = new QueryByCriteria(PersonRole.class, criteria);
		result = (List) broker.getCollectionByQuery(query);
		
		if (result.size() == 0) return null;
		return (IPersonRole) result.get(0);
	}
	
	public static IRole readRole(RoleType roleType, PersistenceBroker broker) throws Exception {
		
		// Read The Role
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", roleType);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		
		if (result.size() == 0){
			throw new Exception("Error Reading Role");
		} else {
			 return (IRole) result.get(0);
		}
		
	}

}
