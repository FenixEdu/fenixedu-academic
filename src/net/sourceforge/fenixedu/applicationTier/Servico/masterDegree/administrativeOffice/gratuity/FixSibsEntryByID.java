package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class FixSibsEntryByID extends Service {

	public void run(Integer sibsEntryId) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentSibsPaymentFileEntry persistentSibsPaymentFileEntry = persistentSupport
				.getIPersistentSibsPaymentFileEntry();

		SibsPaymentFileEntry sibsPaymentFileEntry = (SibsPaymentFileEntry) persistentSibsPaymentFileEntry
				.readByOID(SibsPaymentFileEntry.class, sibsEntryId);

		if (sibsPaymentFileEntry == null) {
			throw new FenixServiceException();
		}

		sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);
	}

}