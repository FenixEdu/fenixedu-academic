package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideSituation;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.GuideEntry;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IPessoa;
import Dominio.transactions.IGratuityTransaction;
import Dominio.transactions.IInsuranceTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.Transaction;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.GraduationType;
import Util.NumberUtils;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.State;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class CreateGuideFromTransactions implements IService {

    /**
     * The actor of this class.
     */
    public CreateGuideFromTransactions() {
    }

    public InfoGuide run(InfoGuide infoGuide, String remarks, SituationOfGuide situationOfGuide,
            List transactionsIDs) throws FenixServiceException {

        ISuportePersistente sp = null;
        IContributor contributor = null;
        IGuide guide = new Guide();
        IGuideSituation guideSituation = null;
        IPessoa person = null;

        // Check the Guide Situation
        if (situationOfGuide.equals(SituationOfGuide.ANNULLED_TYPE))
            throw new InvalidSituationServiceException();

        // Get the Guide Number
        Integer guideNumber = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            guideNumber = sp.getIPersistentGuide().generateGuideNumber(infoGuide.getYear());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        infoGuide.setNumber(guideNumber);

        // Create the new Guide Situation
        InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
        infoGuideSituation.setState(new State(State.ACTIVE));
        infoGuideSituation.setRemarks(remarks);
        infoGuideSituation.setInfoGuide(infoGuide);

        Calendar calendar = Calendar.getInstance();
        infoGuideSituation.setDate(calendar.getTime());
        infoGuideSituation.setSituation(situationOfGuide);

        guide = Cloner.copyInfoGuide2IGuide(infoGuide);
        //      FIXME: Remove the : guide.setGuideEntries(null); WHY????
        guide.setGuideEntries(null);
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.getIPersistentGuide().simpleLockWrite(guide);
            if (situationOfGuide.equals(SituationOfGuide.PAYED_TYPE)) {
                guide.setPaymentType(new PaymentType(PaymentType.ATM_STRING));
                guide.setPaymentDate(calendar.getTime());
            }

            // Get the Execution Degree
            ICursoExecucao executionDegree = sp.getIPersistentExecutionDegree()
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan()
                                    .getInfoDegree().getSigla(),
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
                            Cloner.copyInfoExecutionYear2IExecutionYear(infoGuide
                                    .getInfoExecutionDegree().getInfoExecutionYear()));

            contributor = sp.getIPersistentContributor().readByContributorNumber(
                    infoGuide.getInfoContributor().getContributorNumber());
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(
                    infoGuide.getInfoPerson().getUsername());

            guide.setExecutionDegree(executionDegree);
            guide.setContributor(contributor);
            guide.setPerson(person);

            // Write the new Guide

            // Write the new Entries of the Guide
            Iterator iterator = transactionsIDs.iterator();
            List guideEntries = new ArrayList();
            IPaymentTransaction transaction = null;
            Integer transactionId = null;
            IGuideEntry guideEntry = null;
            double guideTotal = 0;

            while (iterator.hasNext()) {
                transactionId = (Integer) iterator.next();
                transaction = (IPaymentTransaction) sp.getIPersistentTransaction().readByOID(
                        Transaction.class, transactionId, true);

                if (transaction == null) {
                    throw new ExcepcaoInexistente();
                }

                guideEntry = new GuideEntry();

                if (transaction instanceof IGratuityTransaction) {
                    guideEntry.setDocumentType(new DocumentType(DocumentType.GRATUITY));
                } else if (transaction instanceof IInsuranceTransaction) {
                    guideEntry.setDocumentType(new DocumentType(DocumentType.INSURANCE));
                }

                guideEntry.setPrice(transaction.getValue());
                guideEntry.setQuantity(new Integer(1));
                guideTotal += transaction.getValue().doubleValue();

                guideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
                guideEntry.setDescription("");

                sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                guideEntries.add(guideEntry);
                guideEntry.setGuide(guide);

                transaction.setGuideEntry(guideEntry);

            }

            // Guide Total Price
            guide.setTotal(NumberUtils.formatNumber(new Double(guideTotal), 2));

            guide.setGuideEntries(guideEntries);

            // Write the New Guide Situation
            guideSituation = Cloner.copyInfoGuideSituation2IGuideSituation(infoGuideSituation);
            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);
            guideSituation.setGuide(guide);

            guide.setGuideSituations(new ArrayList());

            guide.getGuideSituations().add(guideSituation);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        InfoGuide result = Cloner.copyIGuide2InfoGuide(guide);
        result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());

        return result;
    }

}