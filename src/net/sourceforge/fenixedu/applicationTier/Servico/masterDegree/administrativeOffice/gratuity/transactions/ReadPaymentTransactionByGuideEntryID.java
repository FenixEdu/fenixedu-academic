package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPaymentTransactionByGuideEntryID extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static InfoPaymentTransaction run(Integer guideEntryId) throws FenixServiceException {
		GuideEntry guideEntry = rootDomainObject.readGuideEntryByOID(guideEntryId);
		PaymentTransaction paymentTransaction = guideEntry.getPaymentTransaction();
		if (paymentTransaction == null) {
			throw new ExcepcaoInexistente();
		}

		return InfoPaymentTransaction.newInfoFromDomain(paymentTransaction);
	}

}