package ServidorPersistente.transactions;

import Dominio.transactions.IPaymentTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentPaymentTransaction extends IPersistentObject {
    
    public IPaymentTransaction readByGuideEntryID(Integer guideEntryID) throws ExcepcaoPersistencia;
    
}