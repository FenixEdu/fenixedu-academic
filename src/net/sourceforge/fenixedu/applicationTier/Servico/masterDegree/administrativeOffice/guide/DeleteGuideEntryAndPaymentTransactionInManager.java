package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideEntryAndPaymentTransactionInManager implements IService {

    public void run(Integer guideEntryID) throws ExcepcaoPersistencia, InvalidChangeServiceException {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        if (!guideEntry.getReimbursementGuideEntries().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        IPaymentTransaction paymentTransaction = guideEntry.getPaymentTransaction();
        IGuide guide = guideEntry.getGuide();

        double guideEntryValue = guideEntry.getPrice() * guideEntry.getQuantity();

        if (paymentTransaction != null) {

            if (paymentTransaction instanceof IGratuityTransaction) {

                IGratuityTransaction gratuityTransaction = (IGratuityTransaction) paymentTransaction;
                IGratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();

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
            for (IReimbursementGuideEntry reimbursementGuideEntry : guideEntry
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
