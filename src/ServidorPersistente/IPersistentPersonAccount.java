package ServidorPersistente;

import Dominio.IPersonAccount;
import Dominio.IPerson;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentPersonAccount extends IPersistentObject {

    public IPersonAccount readByPerson(IPerson person) throws ExcepcaoPersistencia;
}