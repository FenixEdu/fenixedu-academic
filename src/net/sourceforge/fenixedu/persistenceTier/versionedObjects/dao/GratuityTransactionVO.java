package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class GratuityTransactionVO extends VersionedObjectsBase implements
		IPersistentGratuityTransaction {

	public List readAllByGratuitySituation(Integer gratuitySituationID) throws ExcepcaoPersistencia {
		return (List) readByOID(GratuitySituation.class, gratuitySituationID);
	}

}