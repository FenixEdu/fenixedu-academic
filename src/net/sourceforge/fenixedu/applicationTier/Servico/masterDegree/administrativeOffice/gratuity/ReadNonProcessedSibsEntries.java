package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sibs.InfoSibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadNonProcessedSibsEntries extends Service {

	public List run() throws FenixServiceException{
		final List<InfoSibsPaymentFileEntry> result = new ArrayList<InfoSibsPaymentFileEntry>();
        for (final SibsPaymentFileEntry sibsPaymentFileEntry : SibsPaymentFileEntry.readNonProcessed()) {
            result.add(InfoSibsPaymentFileEntry.newInfoFromDomain(sibsPaymentFileEntry));
        }
		return result;
	}
}