package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sibs.InfoSibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.ISibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadNonProcessedSibsEntries implements IService {

    public ReadNonProcessedSibsEntries() {

    }

    public List run() throws FenixServiceException {

        List infoSibsFileEntries = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSibsPaymentFileEntry persistentSibsPaymentFileEntry = sp
                    .getIPersistentSibsPaymentFileEntry();

            List sibsFileEntries = persistentSibsPaymentFileEntry.readNonProcessed();

            for (Iterator iter = sibsFileEntries.iterator(); iter.hasNext();) {
                ISibsPaymentFileEntry sibsPaymentFileEntry = (ISibsPaymentFileEntry) iter.next();
                infoSibsFileEntries
                        .add(InfoSibsPaymentFileEntry.newInfoFromDomain(sibsPaymentFileEntry));
            }

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoSibsFileEntries;

    }

}