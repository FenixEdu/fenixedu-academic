package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.gratuity.masterDegree.ISibsPaymentFileEntry;
import Dominio.gratuity.masterDegree.SibsPaymentFileEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import Util.gratuity.SibsPaymentStatus;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class FixSibsEntryByID implements IService {

    public FixSibsEntryByID() {

    }

    public void run(Integer sibsEntryId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentSibsPaymentFileEntry persistentSibsPaymentFileEntry = sp
                    .getIPersistentSibsPaymentFileEntry();

            ISibsPaymentFileEntry sibsPaymentFileEntry = (ISibsPaymentFileEntry) persistentSibsPaymentFileEntry
                    .readByOID(SibsPaymentFileEntry.class, sibsEntryId, true);

            if (sibsPaymentFileEntry == null) {
                throw new FenixServiceException();
            }

            sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

}