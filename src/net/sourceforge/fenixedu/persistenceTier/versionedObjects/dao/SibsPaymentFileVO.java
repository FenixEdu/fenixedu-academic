/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileVO extends VersionedObjectsBase implements IPersistentSibsPaymentFile {

	public SibsPaymentFile readByFilename(final String filename) throws ExcepcaoPersistencia {

		List<SibsPaymentFile> sibsPaymentFiles = (List<SibsPaymentFile>) readAll(SibsPaymentFile.class);

		for (SibsPaymentFile sibsPaymentFile : sibsPaymentFiles) {
			if (sibsPaymentFile.getFilename().equals(filename)) {
				return sibsPaymentFile;
			}
		}
		return null;
	}

}