package middleware.databaseClean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Employee;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.Teacher;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class DeleteRoles {
	
	public DeleteRoles(){}
	
	public static void main(String args[]) throws Exception{
			
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		Pessoa person = null;
		// This will delete roles from persons wich are in Table Person
		// and are not in tables ass_FUNCIONARIO and STUDENT
		
		// Read All the Persons
		
		Criteria criteria = new Criteria();
		Query query = null;
		query = new QueryByCriteria(Pessoa.class,criteria);
		List personList = (List) broker.getCollectionByQuery(query);	
		int studantRoles = 0;
		int employeeRoles = 0;
		int teacherRoles = 0;
		int personRoles = 0;
		
		boolean student = false;
		
		broker.clearCache();
		broker.beginTransaction();
		
		System.out.println("Checking Roles for " + personList.size() + " persons ...");
		Iterator iterator = personList.iterator();
		
		try {

		
			while(iterator.hasNext()){
				person = (Pessoa) iterator.next();
				
				// Check if the person is an Employee

				criteria = new Criteria();
				query = null;
				
				criteria.addEqualTo("keyPerson", person.getIdInternal());
				query = new QueryByCriteria(Employee.class,criteria);
				List result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() == 0){
					// Delete Employee Role if any
					IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.EMPLOYEE, broker);
					
					if (personRole != null){
						removeRole(person, RoleType.EMPLOYEE);
						employeeRoles++;
					}
				}
				
				// Check if the person is a Studant

				criteria = new Criteria();
				query = null;
				
				criteria.addEqualTo("personKey", person.getIdInternal());
				query = new QueryByCriteria(Student.class,criteria);
				result = (List) broker.getCollectionByQuery(query);	
				
				if (result.size() == 0){
					// Delete Studant Role if any
					IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.STUDENT, broker);
					if (personRole != null){
						removeRole(person, RoleType.STUDENT);
						studantRoles++;
						student = true;
					}
				}

				// Check if the person is a Teacher

				criteria = new Criteria();
				query = null;
				
				criteria.addEqualTo("keyPerson", person.getIdInternal());
				query = new QueryByCriteria(Teacher.class,criteria);
				result = (List) broker.getCollectionByQuery(query);
				
				if (result.size() == 0){
					// Delete Teacher Role if any
					IPersonRole personRole = RoleFunctions.readPersonRole(person, RoleType.TEACHER, broker);
					if (personRole != null){
						removeRole(person, RoleType.TEACHER);
						teacherRoles++;
					}
				}

				// If the person has only one Role and he is not a Student Then we delete
				// the Person Role 
				if ((person.getPersonRoles().size() == 1) && !student){
					removeRole(person, RoleType.PERSON);
					personRoles++;
				}
			}
		} catch(Exception e){
			throw new Exception("Error in Person " + person.getNome() + " (ID: " + person.getNumeroDocumentoIdentificacao() + ")");
		}
		broker.commitTransaction();
		System.out.println("Deleted " + employeeRoles + " Employee roles");
		System.out.println("Deleted " + studantRoles + " Studant roles");
		System.out.println("Deleted " + teacherRoles + " Teacher roles");
		System.out.println("Deleted " + personRoles + " Person roles");
	}
	
	private static void removeRole(IPessoa person, RoleType roleType){
		
		Iterator iterator = person.getPersonRoles().iterator();
		List roles = new ArrayList();
		while(iterator.hasNext()){

			IRole role = (IRole) iterator.next();
			if (!role.getRoleType().equals(roleType)){
				roles.add(role);				
			}
		}
		person.setPersonRoles(roles);
	}

}
