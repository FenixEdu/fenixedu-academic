/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GuideSituation;
import Dominio.ICursoExecucao;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IPersonAccount;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.transactions.GratuityTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.InsuranceTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentPersonAccount;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.transactions.IPersistentInsuranceTransaction;
import ServidorPersistente.transactions.IPersistentTransaction;
import Util.DocumentType;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.State;
import Util.TipoCurso;
import Util.transactions.TransactionType;

public class ChangeGuideSituation implements IService {

    /**
     * The actor of this class.
     */
    public ChangeGuideSituation() {
    }

    public void run(Integer guideNumber, Integer guideYear, Integer guideVersion, Date paymentDate,
            String remarks, String situationOfGuideString, String paymentType, IUserView userView)
            throws ExcepcaoInexistente, FenixServiceException, ExistingPersistentException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        IGuide guide = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(guideNumber, guideYear,
                    guideVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (guide == null) {
            throw new ExcepcaoInexistente("Unknown Guide !!");
        }
        sp.getIPersistentGuide().simpleLockWrite(guide);
        SituationOfGuide situationOfGuide = new SituationOfGuide(situationOfGuideString);

        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPessoa employeePerson = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());

        // Get the active Situation
        //        IGuideSituation guideSituation = new GuideSituation();
        //        List guideSituations = guide.getGuideSituations();
        //        Iterator iterator = guideSituations.iterator();
        //        while (iterator.hasNext()) {
        //            IGuideSituation guideSituationTemp = (IGuideSituation)
        // iterator.next();
        //            if (guideSituationTemp.getState().equals(new State(State.ACTIVE)))
        //                guideSituation = guideSituationTemp;
        //        }
        IGuideSituation guideSituation = new GuideSituation();
        for (Iterator iter = guide.getGuideSituations().iterator(); iter.hasNext();) {
            IGuideSituation guideSituationTemp = (IGuideSituation) iter.next();
            if (guideSituationTemp.getState().equals(new State(State.ACTIVE)))
                guideSituation = guideSituationTemp;

        }

        // check if the change is valid
        if (verifyChangeValidation(guideSituation, situationOfGuide) == false) {
            throw new NonValidChangeServiceException();
        }

        sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);
        if (situationOfGuide.equals(guideSituation.getSituation())) {
            guideSituation.setRemarks(remarks);
            if (guideSituation.getSituation().equals(SituationOfGuide.PAYED_TYPE)) {
                guide.setPaymentDate(paymentDate);
                guide.setPaymentType(new PaymentType(paymentType));
            }
            guide.getGuideSituations().add(guideSituation);
        } else {
            // Create The New Situation

            guideSituation.setState(new State(State.INACTIVE));

            IGuideSituation newGuideSituation = new GuideSituation();
            try {
                sp.getIPersistentGuideSituation().simpleLockWrite(newGuideSituation);
                Calendar date = Calendar.getInstance();
                newGuideSituation.setDate(date.getTime());
                newGuideSituation.setGuide(guide);
                newGuideSituation.setRemarks(remarks);
                newGuideSituation.setSituation(situationOfGuide);
                newGuideSituation.setState(new State(State.ACTIVE));

                if (situationOfGuide.equals(SituationOfGuide.PAYED_TYPE)) {
                    sp.getIPersistentGuide().simpleLockWrite(guide);
                    guide.setPaymentDate(paymentDate);
                    guide.setPaymentType(new PaymentType(paymentType));

                    //For Transactions Creation
                    IPersistentTransaction persistentTransaction = sp.getIPersistentTransaction();
                    IPaymentTransaction paymentTransaction = null;
                    IGratuitySituation gratuitySituation = null;
                    IPersistentPersonAccount persistentPersonAccount = sp.getIPersistentPersonAccount();
                    IPersonAccount personAccount = persistentPersonAccount.readByPerson(guide
                            .getPerson());
                    IPersistentGratuitySituation persistentGratuitySituation = sp
                            .getIPersistentGratuitySituation();

                    // Iterate Guide Entries to create Transactions
                    IGuideEntry guideEntry = null;
                    Iterator guideEntryIterator = guide.getGuideEntries().iterator();
                    while (guideEntryIterator.hasNext()) {

                        guideEntry = (IGuideEntry) guideEntryIterator.next();

                        IPessoa studentPerson = guide.getPerson();

                        IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(
                                studentPerson, TipoCurso.MESTRADO_OBJ);

                        ICursoExecucao executionDegree = guide.getExecutionDegree();

                        //Write Gratuity Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY_TYPE)) {

                            executionDegree = guide.getExecutionDegree();
                            gratuitySituation = persistentGratuitySituation
                                    .readGratuitySituationByExecutionDegreeAndStudent(executionDegree,
                                            student);
                            Double value = new Double(guideEntry.getPrice().doubleValue()
                                    * guideEntry.getQuantity().intValue());

                            paymentTransaction = new GratuityTransaction(value, new Timestamp(Calendar
                                    .getInstance().getTimeInMillis()), guideEntry.getDescription(),
                                    guide.getPaymentType(), TransactionType.GRATUITY_ADHOC_PAYMENT,
                                    Boolean.FALSE, employeePerson, personAccount, guideEntry,
                                    gratuitySituation);

                            persistentTransaction.lockWrite(paymentTransaction);

                            //Update GratuitySituation
                            persistentGratuitySituation.lockWrite(gratuitySituation);

                            Double remainingValue = gratuitySituation.getRemainingValue();

                            gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                                    - paymentTransaction.getValue().doubleValue()));

                        }

                        //Write Insurance Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE_TYPE)) {

                            IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                                    .getIPersistentInsuranceTransaction();

                            List insuranceTransactionList = insuranceTransactionDAO
                                    .readAllNonReimbursedByExecutionYearAndStudent(executionDegree
                                            .getExecutionYear(), student);

                            if (insuranceTransactionList.isEmpty() == false) {
                                throw new ExistingServiceException(
                                        "error.message.transaction.insuranceTransactionAlreadyExists");
                            }

                            paymentTransaction = new InsuranceTransaction(guideEntry.getPrice(),
                                    new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry
                                            .getDescription(), guide.getPaymentType(),
                                    TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, guide.getPerson(),
                                    personAccount, guideEntry, executionDegree.getExecutionYear(),
                                    student);

                            persistentTransaction.lockWrite(paymentTransaction);
                        }

                    }

                }

                // Write the new Situation

            } catch (ExcepcaoPersistencia ex) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                throw newEx;
            }
        }

    }

    private boolean verifyChangeValidation(IGuideSituation activeGuideSituation,
            SituationOfGuide situationOfGuide) {
        if (activeGuideSituation.equals(SituationOfGuide.ANNULLED_TYPE))
            return false;

        if ((activeGuideSituation.getSituation().equals(SituationOfGuide.PAYED_TYPE))
                && (situationOfGuide.equals(SituationOfGuide.NON_PAYED_TYPE)))
            return false;

        return true;
    }

}