
package ServidorPersistente;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentPersonRole extends IPersistentObject {


	/**
	 * 
	 * @param person
	 * @param role
	 * @return IPersonRole
	 * @throws ExcepcaoPersistencia
	 */
	public IPersonRole readByPersonAndRole(IPessoa person, IRole role) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param personRole
	 * @throws ExcepcaoPersistencia
	 * @throws ExistingPersistentException
	 */
	public void write(IPersonRole personRole) throws ExcepcaoPersistencia, ExistingPersistentException;     
}
