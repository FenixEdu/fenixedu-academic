package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GuideEntry;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.transactions.IGratuityTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.PaymentTransaction;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.NumberUtils;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideEntryAndPaymentTransactionInManager implements IService {

    public void run(Integer guideEntryID) throws ExcepcaoPersistencia, InvalidChangeServiceException {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        if (!guideEntry.getReimbursementGuideEntries().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        IPaymentTransaction paymentTransaction = sp.getIPersistentPaymentTransaction()
                .readByGuideEntryID(guideEntryID);
        IGuide guide = guideEntry.getGuide();

        double guideEntryValue = guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue();

        if (paymentTransaction != null) {

            if (paymentTransaction instanceof IGratuityTransaction) {

                IGratuityTransaction gratuityTransaction = (IGratuityTransaction) paymentTransaction;
                IGratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();

                gratuitySituation.setRemainingValue(new Double(gratuitySituation.getRemainingValue()
                        .doubleValue()
                        + guideEntryValue));

                sp.getIPersistentGratuitySituation().simpleLockWrite(gratuitySituation);
            }

            sp.getIPersistentPaymentTransaction().deleteByOID(PaymentTransaction.class,
                    paymentTransaction.getIdInternal());

        }

        sp.getIPersistentGuideEntry().delete(guideEntry);

        // update guide's total value
        guide.setTotal(NumberUtils.formatNumber(new Double(guide.getTotal().doubleValue()
                - guideEntryValue), 2));
        sp.getIPersistentGuide().simpleLockWrite(guide);

    }

}
