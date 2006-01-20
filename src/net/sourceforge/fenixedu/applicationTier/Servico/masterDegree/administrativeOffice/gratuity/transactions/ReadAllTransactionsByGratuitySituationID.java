package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadAllTransactionsByGratuitySituationID extends Service {

    public List run(Integer gratuitySituationID) throws FenixServiceException, ExcepcaoPersistencia {

        List infoTransactionList = new ArrayList();

        List paymentTransactionList = new ArrayList();

        List reimbursementTransactionList = new ArrayList();

        GratuitySituation gratuitySituation = (GratuitySituation) persistentSupport.getIPersistentGratuitySituation()
                .readByOID(GratuitySituation.class, gratuitySituationID);

        List insuranceTransactionList = persistentSupport.getIPersistentInsuranceTransaction()
                .readAllByExecutionYearAndStudent(
                        gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear().getIdInternal(),
                        gratuitySituation.getStudentCurricularPlan().getStudent().getIdInternal());

        // read insurance transactions
        for (Iterator iter = insuranceTransactionList.iterator(); iter.hasNext();) {
            InsuranceTransaction insuranceTransaction = (InsuranceTransaction) iter.next();
            paymentTransactionList.add(insuranceTransaction);

        }

        List gratuityTransactionList = persistentSupport.getIPersistentGratuityTransaction()
                .readAllByGratuitySituation(gratuitySituation.getIdInternal());

        // read gratuity transactions
        for (Iterator iter = gratuityTransactionList.iterator(); iter.hasNext();) {

            GratuityTransaction gratuityTransaction = (GratuityTransaction) iter.next();
            paymentTransactionList.add(gratuityTransaction);
        }

        IPersistentReimbursementTransaction reimbursementTransactionDAO = persistentSupport
                .getIPersistentReimbursementTransaction();

        // read reimbursement transactions
        for (Iterator iter = paymentTransactionList.iterator(); iter.hasNext();) {

            PaymentTransaction paymentTransaction = (PaymentTransaction) iter.next();

            GuideEntry guideEntry = paymentTransaction.getGuideEntry();

            if ((guideEntry != null)
                    && ((guideEntry.getDocumentType().equals(DocumentType.INSURANCE) || (guideEntry
                            .getDocumentType().equals(DocumentType.GRATUITY))))) {

                List reimbursementGuideEntryList = guideEntry.getReimbursementGuideEntries();

                if (reimbursementGuideEntryList == null) {
                    continue;
                }

                for (Iterator iterator = reimbursementGuideEntryList.iterator(); iterator.hasNext();) {
                    ReimbursementGuideEntry reimbursementGuideEntry = (ReimbursementGuideEntry) iterator
                            .next();

                    // we have to read again because of OJB bug
                    ReimbursementGuideEntry newReimbursementGuideEntry = (ReimbursementGuideEntry) persistentSupport
                            .getIPersistentReimbursementGuideEntry().readByOID(
                                    ReimbursementGuideEntry.class,
                                    reimbursementGuideEntry.getIdInternal());

                    ReimbursementGuide reimbursementGuide = (ReimbursementGuide) persistentSupport
                            .getIPersistentReimbursementGuide().readByOID(
                                    ReimbursementGuide.class,
                                    reimbursementGuideEntry.getKeyReimbursementGuide());

                    if (reimbursementGuide.getActiveReimbursementGuideSituation()
                            .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED) == false) {
                        // reimbursement is not payed, so there is no
                        // transaction
                        continue;

                    }
                    ReimbursementTransaction reimbursementTransaction = reimbursementTransactionDAO
                            .readByReimbursementGuideEntry(newReimbursementGuideEntry.getIdInternal());

                    if (reimbursementTransaction == null) {
                        throw new NonExistingServiceException(
                                "Database is inconsistent because this reimbursement guide entry is supposed to have a reimbursement transaction");
                    }

                    reimbursementTransactionList.add(reimbursementTransaction);
                }
            }

        }

        for (Iterator iter = insuranceTransactionList.iterator(); iter.hasNext();) {
            InsuranceTransaction insuranceTransaction = (InsuranceTransaction) iter.next();
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(insuranceTransaction));
        }

        for (Iterator iter = gratuityTransactionList.iterator(); iter.hasNext();) {
            GratuityTransaction gratuityTransaction = (GratuityTransaction) iter.next();
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(gratuityTransaction));
        }

        for (Iterator iter = reimbursementTransactionList.iterator(); iter.hasNext();) {
            ReimbursementTransaction reimbursementTransaction = (ReimbursementTransaction) iter.next();
            infoTransactionList.add(InfoTransaction.newInfoFromDomain(reimbursementTransaction));
        }

        BeanComparator transactionDateComparator = new BeanComparator("transactionDate");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(transactionDateComparator, true);
        Collections.sort(infoTransactionList, chainComparator);

        return infoTransactionList;

    }
}