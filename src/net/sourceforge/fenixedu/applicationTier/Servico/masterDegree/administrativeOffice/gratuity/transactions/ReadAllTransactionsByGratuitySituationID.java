package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ReadAllTransactionsByGratuitySituationID extends Service {

    public List run(Integer gratuitySituationID) throws FenixServiceException, ExcepcaoPersistencia {
        GratuitySituation gratuitySituation = rootDomainObject
                .readGratuitySituationByOID(gratuitySituationID);
        List<InsuranceTransaction> insuranceTransactionList = gratuitySituation
                .getStudentCurricularPlan().getRegistration().readAllInsuranceTransactionByExecutionYear(
                        gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear());

        List<PaymentTransaction> paymentTransactionList = new ArrayList<PaymentTransaction>();
        paymentTransactionList.addAll(insuranceTransactionList);
        paymentTransactionList.addAll(gratuitySituation.getTransactionList());

        List<ReimbursementTransaction> reimbursementTransactionList = new ArrayList<ReimbursementTransaction>();
        for (PaymentTransaction paymentTransaction : paymentTransactionList) {
            GuideEntry guideEntry = paymentTransaction.getGuideEntry();

            if ((guideEntry != null)
                    && ((guideEntry.getDocumentType().equals(DocumentType.INSURANCE) || (guideEntry
                            .getDocumentType().equals(DocumentType.GRATUITY))))) {

                for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry
                        .getReimbursementGuideEntries()) {
                    ReimbursementGuide reimbursementGuide = reimbursementGuideEntry
                            .getReimbursementGuide();

                    if (!reimbursementGuide.getActiveReimbursementGuideSituation()
                            .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED)) {
                        // reimbursement is not payed, so there is notransaction
                        continue;
                    }

                    ReimbursementTransaction reimbursementTransaction = reimbursementGuideEntry
                            .getReimbursementTransaction();
                    if (reimbursementTransaction == null) {
                        throw new NonExistingServiceException(
                                "Database is inconsistent because this reimbursement guide entry is supposed to have a reimbursement transaction");
                    }
                    reimbursementTransactionList.add(reimbursementTransaction);
                }
            }

        }

        List<InfoTransaction> infoTransactionList = new ArrayList<InfoTransaction>();
        for (InsuranceTransaction insuranceTransaction : insuranceTransactionList) {
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(insuranceTransaction));
        }
        for (GratuityTransaction gratuityTransaction : gratuitySituation.getTransactionList()) {
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(gratuityTransaction));
        }
        for (ReimbursementTransaction reimbursementTransaction : reimbursementTransactionList) {
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(reimbursementTransaction));
        }

        BeanComparator transactionDateComparator = new BeanComparator("transactionDate");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(transactionDateComparator, true);
        Collections.sort(infoTransactionList, chainComparator);

        return infoTransactionList;
    }

}
