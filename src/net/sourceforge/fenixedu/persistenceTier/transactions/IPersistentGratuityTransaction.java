package net.sourceforge.fenixedu.persistenceTier.transactions;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

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