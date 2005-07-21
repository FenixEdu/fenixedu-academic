package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ReimbursementTransactionVO extends VersionedObjectsBase implements
        IPersistentReimbursementTransaction {

    public IReimbursementTransaction readByReimbursementGuideEntry(
            final Integer reimbursementGuideEntryID) throws ExcepcaoPersistencia {

		IReimbursementGuideEntry reimbursementGuideEntry = (IReimbursementGuideEntry) 
				readByOID(ReimbursementGuideEntry.class, reimbursementGuideEntryID);		
		return reimbursementGuideEntry.getReimbursementTransaction();
    }
	
}