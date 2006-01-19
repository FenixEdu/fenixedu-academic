package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideEntryAndPaymentTransactionInManager extends Service {

    public void run(Integer guideEntryID) throws ExcepcaoPersistencia, InvalidChangeServiceException {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        GuideEntry guideEntry = (GuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        if (!guideEntry.getReimbursementGuideEntries().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        PaymentTransaction paymentTransaction = guideEntry.getPaymentTransaction();
        Guide guide = guideEntry.getGuide();

        double guideEntryValue = guideEntry.getPrice() * guideEntry.getQuantity();

        if (paymentTransaction != null) {

            if (paymentTransaction instanceof GratuityTransaction) {

                GratuityTransaction gratuityTransaction = (GratuityTransaction) paymentTransaction;
                GratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();

                gratuitySituation.setRemainingValue(gratuitySituation.getRemainingValue()
                        + guideEntryValue);

                gratuityTransaction.removeGratuitySituation();

            }

            paymentTransaction.removePersonAccount();
            paymentTransaction.removeGuideEntry();
            paymentTransaction.removeResponsiblePerson();
            sp.getIPersistentPaymentTransaction().deleteByOID(PaymentTransaction.class,
                    paymentTransaction.getIdInternal());

        }

        if (guideEntry.getReimbursementGuideEntries() != null) {
            for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry
                    .getReimbursementGuideEntries()) {

                reimbursementGuideEntry.removeGuideEntry();
                reimbursementGuideEntry.removeReimbursementGuide();
                reimbursementGuideEntry.removeReimbursementTransaction();
                sp.getIPersistentGuideEntry().deleteByOID(ReimbursementGuideEntry.class,
                        reimbursementGuideEntry.getIdInternal());
            }
        }

        guideEntry.removeGuide();
        sp.getIPersistentGuideEntry().deleteByOID(GuideEntry.class, guideEntry.getIdInternal());

        // update guide's total value
        guide.updateTotalValue();

    }

}
