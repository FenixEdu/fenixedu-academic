package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ReimbursementTransactionVO extends VersionedObjectsBase implements
        IPersistentReimbursementTransaction {

    public ReimbursementTransaction readByReimbursementGuideEntry(
            final Integer reimbursementGuideEntryID) throws ExcepcaoPersistencia {

		ReimbursementGuideEntry reimbursementGuideEntry = (ReimbursementGuideEntry) 
				readByOID(ReimbursementGuideEntry.class, reimbursementGuideEntryID);		
		return reimbursementGuideEntry.getReimbursementTransaction();
    }
	
}