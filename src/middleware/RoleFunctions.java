/*
 * Created on 20/Mai/2003
 */
package middleware;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.PersonRole;
import Dominio.Role;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleFunctions {


	public RoleFunctions(){}


	public static void giveRole(IPessoa person, RoleType role, PersistenceBroker broker) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", role);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		
		Role roleBD = null;
		
		if (result.size() == 0)
			throw new Exception("Unknown Role !!!");
		else roleBD = (Role) result.get(0);
		 
//		PersonRole newRole = new PersonRole();
//		newRole.setPerson(person);
//		newRole.setRole(roleBD);
		
		person.getPersonRoles().add(roleBD);
		broker.store(person);

//		broker.store(newRole);
	}

	public static void giveEmployeeRole(IPessoa person, PersistenceBroker broker) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.EMPLOYEE);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(person);
		newRole.setRole(role);

		broker.store(newRole);

System.out.println(newRole.getRole().getRoleType());

	}
	
		
	public static void givePersonRole(IPessoa person, PersistenceBroker broker) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.PERSON);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(person);
		newRole.setRole(role);
		
		broker.store(newRole);
	}

	public static void giveStudentRole(IStudent student , PersistenceBroker broker) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.STUDENT);
		
		Query query = new QueryByCriteria(Role.class, criteria);
		List result = (List)broker.getCollectionByQuery(query);
		
		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else role = (Role) result.get(0);
		 
		IPersonRole newRole = new PersonRole();
		newRole.setPerson(student.getPerson());
		newRole.setRole(role);
		
		broker.store(newRole);
	}

	public static IPersonRole readPersonRole(IPessoa person, RoleType roleType, PersistenceBroker broker) throws Exception {
		
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



}
