package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.List;

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
import net.sourceforge.fenixedu.util.NumberUtils;
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

			sp.getIPersistentPersonAccount().simpleLockWrite(paymentTransaction.getPersonAccount());
			paymentTransaction.getPersonAccount().getPaymentTransactions().remove(paymentTransaction);
            sp.getIPersistentPaymentTransaction().deleteByOID(PaymentTransaction.class,
                    paymentTransaction.getIdInternal());

        }

		if(guideEntry.getReimbursementGuideEntries()!=null){
			for(IReimbursementGuideEntry reimbursementGuideEntry : (List<IReimbursementGuideEntry>)guideEntry.getReimbursementGuideEntries()){
				sp.getIPersistentReimbursementGuide().simpleLockWrite(reimbursementGuideEntry.getReimbursementGuide());
				reimbursementGuideEntry.getReimbursementGuide().getReimbursementGuideEntries().remove(reimbursementGuideEntry);
				sp.getIPersistentGuideEntry().deleteByOID(ReimbursementGuideEntry.class,reimbursementGuideEntry.getIdInternal());
			}
		}
		
		guideEntry.getGuide().getGuideEntries().remove(guideEntry);
        sp.getIPersistentGuideEntry().deleteByOID(GuideEntry.class,guideEntry.getIdInternal());
		
		

        // update guide's total value
        guide.setTotal(NumberUtils.formatNumber(new Double(guide.getTotal().doubleValue()
                - guideEntryValue), 2));
        sp.getIPersistentGuide().simpleLockWrite(guide);

    }

}
