package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.sibs.InfoSibsPaymentFileEntry;
import Dominio.gratuity.masterDegree.ISibsPaymentFileEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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