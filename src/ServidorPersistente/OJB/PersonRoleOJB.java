package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.PersonRole;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRoleOJB extends ObjectFenixOJB implements IPersistentPersonRole {

	public PersonRoleOJB() {
		super();
	}
	

	public void write(IPersonRole personRoleToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

			IPersonRole personRoleBD = null;

			// If there is nothing to write, simply return.
			if (personRoleToWrite == null)
				return;

			// Read personRole from database.
			personRoleBD = this.readByPersonAndRole(personRoleToWrite.getPerson(), personRoleToWrite.getRole());
			
			// If personRole is not in database, then write it.
			if (personRoleBD == null)
				super.lockWrite(personRoleToWrite);
			// else If the degree is mapped to the database, then write any existing changes.
			else if (
				(personRoleToWrite instanceof PersonRole) && 
				 ((PersonRole) personRoleBD).getIdInternal().equals(((PersonRole) personRoleToWrite).getIdInternal())) {
				super.lockWrite(personRoleToWrite);
				// else Throw an already existing exception
			} else
				throw new ExistingPersistentException();
				
	}



	public IPersonRole readByPersonAndRole(IPessoa person, IRole role) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
        crit.addEqualTo("person.username",person.getUsername());
        crit.addEqualTo("role.roleType",new Integer(role.getRoleType().getValue()));
        return (IPersonRole) queryObject(PersonRole.class,crit);
       
	}

}
