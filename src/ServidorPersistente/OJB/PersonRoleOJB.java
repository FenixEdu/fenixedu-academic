package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

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
		try {
			String oqlQuery = "select all from " + PersonRole.class.getName()
					+ " where person.username = $1"
					+ " and role.roleType = $2";

			query.create(oqlQuery);
			query.bind(person.getUsername());
			query.bind(new Integer(role.getRoleType().getValue()));
			
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) return (IPersonRole) result.get(0);
			return null;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
