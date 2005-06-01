/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.gratuity.masterDegree.ISibsPaymentFile;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import java.util.List;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileVO extends VersionedObjectsBase implements IPersistentSibsPaymentFile {

	public ISibsPaymentFile readByFilename(final String filename) throws ExcepcaoPersistencia {

		List<ISibsPaymentFile> sibsPaymentFiles = (List<ISibsPaymentFile>) readAll(SibsPaymentFile.class);

		for (ISibsPaymentFile sibsPaymentFile : sibsPaymentFiles) {
			if (sibsPaymentFile.getFilename().equals(filename)) {
				return sibsPaymentFile;
			}
		}
		return null;
	}

}