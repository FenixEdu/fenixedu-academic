package ServidorPersistente.transactions;

import java.util.List;

import Dominio.IPersonAccount;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentTransaction extends IPersistentObject {

    public List readAllByPersonAccount(IPersonAccount personAccount) throws ExcepcaoPersistencia;
}