/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.ISibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntryVO extends VersionedObjectsBase implements
		IPersistentSibsPaymentFileEntry {

	public List readByYearAndStudentNumberAndPaymentType(final Integer year,
			final Integer studentNumber, final SibsPaymentType paymentType) throws ExcepcaoPersistencia {

		List<ISibsPaymentFileEntry> sibsPaymentFileEntries = (List<ISibsPaymentFileEntry>) readAll(SibsPaymentFileEntry.class);
		List<ISibsPaymentFileEntry> result = new ArrayList();

		for (ISibsPaymentFileEntry sibsPaymentFileEntry : sibsPaymentFileEntries) {
			if (sibsPaymentFileEntry.getYear().equals(year)
					&& sibsPaymentFileEntry.getStudentNumber().equals(studentNumber)
					&& sibsPaymentFileEntry.getPaymentType().equals(paymentType)) {
				result.add(sibsPaymentFileEntry);
			}
		}
		return result;
	}

	public List readNonProcessed() throws ExcepcaoPersistencia {

		List<ISibsPaymentFileEntry> sibsPaymentFileEntries = (List<ISibsPaymentFileEntry>) readAll(SibsPaymentFileEntry.class);
		List<ISibsPaymentFileEntry> result = new ArrayList();

		for (ISibsPaymentFileEntry sibsPaymentFileEntry : sibsPaymentFileEntries) {
			if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.PROCESSED_PAYMENT)) {
				result.add(sibsPaymentFileEntry);
			}
		}

		return result;
	}

}