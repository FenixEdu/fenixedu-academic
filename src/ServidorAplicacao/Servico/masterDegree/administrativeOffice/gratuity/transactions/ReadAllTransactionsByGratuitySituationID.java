package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity.transactions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.transactions.InfoTransaction;
import Dominio.GratuitySituation;
import Dominio.IGratuitySituation;
import Dominio.IGuideEntry;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.reimbursementGuide.ReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuideEntry;
import Dominio.transactions.IGratuityTransaction;
import Dominio.transactions.IInsuranceTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.IReimbursementTransaction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.transactions.IPersistentReimbursementTransaction;
import Util.DocumentType;
import Util.ReimbursementGuideState;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadAllTransactionsByGratuitySituationID implements IService {

    /**
     * Constructor
     */
    public ReadAllTransactionsByGratuitySituationID() {
    }

    public List run(Integer gratuitySituationID) throws FenixServiceException {

        List infoTransactionList = new ArrayList();

        try {
            List paymentTransactionList = new ArrayList();

            List reimbursementTransactionList = new ArrayList();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IGratuitySituation gratuitySituation = (IGratuitySituation) sp
                    .getIPersistentGratuitySituation().readByOID(GratuitySituation.class,
                            gratuitySituationID);

            List insuranceTransactionList = sp.getIPersistentInsuranceTransaction()
                    .readAllByExecutionYearAndStudent(
                            gratuitySituation.getGratuityValues().getExecutionDegree()
                                    .getExecutionYear(),
                            gratuitySituation.getStudentCurricularPlan().getStudent());

            // read insurance transactions
            for (Iterator iter = insuranceTransactionList.iterator(); iter.hasNext();) {
                IInsuranceTransaction insuranceTransaction = (IInsuranceTransaction) iter.next();
                paymentTransactionList.add(insuranceTransaction);

            }

            List gratuityTransactionList = sp.getIPersistentGratuityTransaction()
                    .readAllByGratuitySituation(gratuitySituation);

            // read gratuity transactions
            for (Iterator iter = gratuityTransactionList.iterator(); iter.hasNext();) {

                IGratuityTransaction gratuityTransaction = (IGratuityTransaction) iter.next();
                paymentTransactionList.add(gratuityTransaction);
            }

            IPersistentReimbursementTransaction reimbursementTransactionDAO = sp
                    .getIPersistentReimbursementTransaction();

            // read reimbursement transactions
            for (Iterator iter = paymentTransactionList.iterator(); iter.hasNext();) {

                IPaymentTransaction paymentTransaction = (IPaymentTransaction) iter.next();

                IGuideEntry guideEntry = paymentTransaction.getGuideEntry();

                if ((guideEntry != null)
                        && ((guideEntry.getDocumentType().equals(DocumentType.INSURANCE_TYPE) || (guideEntry
                                .getDocumentType().equals(DocumentType.GRATUITY_TYPE))))) {

                    List reimbursementGuideEntryList = guideEntry.getReimbursementGuideEntries();

                    for (Iterator iterator = reimbursementGuideEntryList.iterator(); iterator.hasNext();) {
                        IReimbursementGuideEntry reimbursementGuideEntry = (IReimbursementGuideEntry) iterator
                                .next();

                        // we have to read again because of OJB bug
                        IReimbursementGuideEntry newReimbursementGuideEntry = (IReimbursementGuideEntry) sp
                                .getIPersistentReimbursementGuideEntry().readByOID(
                                        ReimbursementGuideEntry.class,
                                        reimbursementGuideEntry.getIdInternal());

                        IReimbursementGuide reimbursementGuide = (IReimbursementGuide) sp
                                .getIPersistentReimbursementGuide().readByOID(
                                        ReimbursementGuide.class,
                                        ((ReimbursementGuideEntry) reimbursementGuideEntry)
                                                .getKeyReimbursementGuide());

                        if (reimbursementGuide.getActiveReimbursementGuideSituation()
                                .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED) == false) {
                            //reimbursement is not payed, so there is no
                            // transaction
                            continue;

                        }
                        IReimbursementTransaction reimbursementTransaction = reimbursementTransactionDAO
                                .readByReimbursementGuideEntry(newReimbursementGuideEntry);

                        if (reimbursementTransaction == null) {
                            throw new NonExistingServiceException(
                                    "Database is inconsistent because this reimbursement guide entry is supposed to have a reimbursement transaction");
                        }

                        reimbursementTransactionList.add(reimbursementTransaction);
                    }
                }

            }

            for (Iterator iter = insuranceTransactionList.iterator(); iter.hasNext();) {
                IInsuranceTransaction insuranceTransaction = (IInsuranceTransaction) iter.next();
                infoTransactionList.add(InfoTransaction.newInfoFromDomain(insuranceTransaction));
            }

            for (Iterator iter = gratuityTransactionList.iterator(); iter.hasNext();) {
                IGratuityTransaction gratuityTransaction = (IGratuityTransaction) iter.next();
                infoTransactionList.add(InfoTransaction.newInfoFromDomain(gratuityTransaction));
            }

            for (Iterator iter = reimbursementTransactionList.iterator(); iter.hasNext();) {
                IReimbursementTransaction reimbursementTransaction = (IReimbursementTransaction) iter
                        .next();
                infoTransactionList.add(InfoTransaction.newInfoFromDomain(reimbursementTransaction));
            }

            BeanComparator transactionDateComparator = new BeanComparator("transactionDate");
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(transactionDateComparator, true);
            Collections.sort(infoTransactionList, chainComparator);

            return infoTransactionList;
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}