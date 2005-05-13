package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.IInsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.Transaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

    public InfoGuide run(InfoGuide infoGuide, String remarks, GuideState situationOfGuide,
            List transactionsIDs) throws FenixServiceException {

        ISuportePersistente sp = null;
        IContributor contributor = null;
        IGuide guide = new Guide();
        IGuideSituation guideSituation = null;
        IPerson person = null;

        // Check the Guide Situation
        if (situationOfGuide.equals(GuideState.ANNULLED))
            throw new InvalidSituationServiceException();

        // Get the Guide Number
        Integer guideNumber = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
        //infoGuideSituation.setDate(calendar.getTime());
        infoGuideSituation.setSituation(situationOfGuide);

        guide = InfoGuide.newDomainFromInfo(infoGuide);
        
        //      FIXME: Remove the : guide.setGuideEntries(null); WHY????
        guide.setGuideEntries(null);
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.getIPersistentGuide().simpleLockWrite(guide);
            if (situationOfGuide.equals(GuideState.PAYED)) {
                guide.setPaymentType(PaymentType.SIBS);
                guide.setPaymentDate(calendar.getTime());
            }

            // Get the Execution Degree
            IExecutionDegree executionDegree = sp.getIPersistentExecutionDegree()
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan()
                                    .getInfoDegree().getSigla(),
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
                            InfoExecutionYear.newDomainFromInfo(infoGuide
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
                    guideEntry.setDocumentType(DocumentType.GRATUITY);
                } else if (transaction instanceof IInsuranceTransaction) {
                    guideEntry.setDocumentType(DocumentType.INSURANCE);
                }

                guideEntry.setPrice(transaction.getValue());
                guideEntry.setQuantity(new Integer(1));
                guideTotal += transaction.getValue().doubleValue();

                guideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
                guideEntry.setDescription("");

                sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                guideEntries.add(guideEntry);
                guideEntry.setGuide(guide);

                transaction.setGuideEntry(guideEntry);
                
                guide.setCreationDate(transaction.getTransactionDate());
                guide.setPaymentDate(transaction.getTransactionDate());
                infoGuideSituation.setDate(transaction.getTransactionDate());
                
            }

            // Guide Total Price
            guide.setTotal(NumberUtils.formatNumber(new Double(guideTotal), 2));

            guide.setGuideEntries(guideEntries);

            // Write the New Guide Situation
            guideSituation = new GuideSituation(infoGuideSituation.getSituation(),infoGuideSituation.getRemarks(),infoGuideSituation.getDate(),guide,infoGuideSituation.getState());
            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);

            guide.setGuideSituations(new ArrayList());

            guide.getGuideSituations().add(guideSituation);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        InfoGuide result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);
        result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());

        return result;
    }

}