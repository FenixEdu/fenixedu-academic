package ServidorPersistente.transactions;

import java.util.List;

import Dominio.IGratuitySituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentGratuityTransaction extends IPersistentObject {

    public List readAllByGratuitySituation(IGratuitySituation gratuitySituation)
            throws ExcepcaoPersistencia;
}