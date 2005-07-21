package net.sourceforge.fenixedu.persistenceTier.transactions;

import net.sourceforge.fenixedu.domain.transactions.IReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentReimbursementTransaction extends IPersistentObject {

    public IReimbursementTransaction readByReimbursementGuideEntry(
            Integer reimbursementGuideEntryID) throws ExcepcaoPersistencia;
}