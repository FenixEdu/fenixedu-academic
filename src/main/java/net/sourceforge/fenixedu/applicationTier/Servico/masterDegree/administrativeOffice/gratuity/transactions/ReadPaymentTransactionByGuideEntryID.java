package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadPaymentTransactionByGuideEntryID {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static InfoPaymentTransaction run(String guideEntryId) throws FenixServiceException {
        GuideEntry guideEntry = FenixFramework.getDomainObject(guideEntryId);
        PaymentTransaction paymentTransaction = guideEntry.getPaymentTransaction();
        if (paymentTransaction == null) {
            throw new ExcepcaoInexistente();
        }

        return InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);
    }

}