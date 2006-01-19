package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.Transaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class CreateGuideFromTransactions implements IService {

    public InfoGuide run(InfoGuide infoGuide, String remarks, GuideState situationOfGuide,
            List transactionsIDs) throws FenixServiceException, ExcepcaoPersistencia {

        Guide guide = DomainFactory.makeGuide();
        GuideSituation guideSituation = null;

        // Check the Guide Situation
        if (situationOfGuide.equals(GuideState.ANNULLED))
            throw new InvalidSituationServiceException();

        // Get the Guide Number
        Integer guideNumber = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        guideNumber = sp.getIPersistentGuide().generateGuideNumber(infoGuide.getYear());

        infoGuide.setNumber(guideNumber);

        // Create the new Guide Situation
        InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
        infoGuideSituation.setState(new State(State.ACTIVE));
        infoGuideSituation.setRemarks(remarks);
        infoGuideSituation.setInfoGuide(infoGuide);

        Calendar calendar = Calendar.getInstance();
        infoGuideSituation.setSituation(situationOfGuide);

        guide.setCreationDate(infoGuide.getCreationDate());
        guide.setGuideRequester(infoGuide.getGuideRequester());
        guide.setNumber(infoGuide.getNumber());
        guide.setPaymentDate(infoGuide.getPaymentDate());
        guide.setPaymentType(infoGuide.getPaymentType());
        guide.setRemarks(infoGuide.getRemarks());
        guide.setTotal(infoGuide.getTotal());
        guide.setVersion(infoGuide.getVersion());
        guide.setYear(infoGuide.getYear());

        if (situationOfGuide.equals(GuideState.PAYED)) {
            guide.setPaymentType(PaymentType.SIBS);
            guide.setPaymentDate(calendar.getTime());
        }

        // Get the Execution Degree
        ExecutionDegree executionDegree = sp.getIPersistentExecutionDegree()
                .readByDegreeCurricularPlanAndExecutionYear(
                        infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
                        infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree()
                                .getSigla(),
                        infoGuide.getInfoExecutionDegree().getInfoExecutionYear().getYear());

        Contributor contributor = sp.getIPersistentContributor().readByContributorNumber(
                infoGuide.getInfoContributor().getContributorNumber());
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(
                infoGuide.getInfoPerson().getUsername());

        guide.setExecutionDegree(executionDegree);
        guide.setContributor(contributor);
        guide.setPerson(person);

        // Write the new Guide

        // Write the new Entries of the Guide
        Iterator iterator = transactionsIDs.iterator();
        PaymentTransaction transaction = null;
        Integer transactionId = null;
        GuideEntry guideEntry = null;
        double guideTotal = 0;

        while (iterator.hasNext()) {
            transactionId = (Integer) iterator.next();
            transaction = (PaymentTransaction) sp.getIPersistentTransaction().readByOID(
                    Transaction.class, transactionId);

            if (transaction == null) {
                throw new ExcepcaoInexistente();
            }

            guideEntry = DomainFactory.makeGuideEntry();

            if (transaction instanceof GratuityTransaction) {
                guideEntry.setDocumentType(DocumentType.GRATUITY);
            } else if (transaction instanceof InsuranceTransaction) {
                guideEntry.setDocumentType(DocumentType.INSURANCE);
            }

            guideEntry.setPrice(transaction.getValue());
            guideEntry.setQuantity(new Integer(1));
            guideTotal += transaction.getValue().doubleValue();

            guideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
            guideEntry.setDescription("");

            guideEntry.setGuide(guide);

            transaction.setGuideEntry(guideEntry);

            guide.setCreationDate(transaction.getTransactionDate());
            guide.setPaymentDate(transaction.getTransactionDate());
            infoGuideSituation.setDate(transaction.getTransactionDate());

        }

        // Guide Total Price
        guide.setTotal(NumberUtils.formatNumber(new Double(guideTotal), 2));

        // Write the New Guide Situation
        guideSituation = DomainFactory.makeGuideSituation(infoGuideSituation.getSituation(),
                infoGuideSituation.getRemarks(), infoGuideSituation.getDate(), guide, infoGuideSituation
                        .getState());

        guide.getGuideSituations().add(guideSituation);

        InfoGuide result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);
        result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());

        return result;
    }

}
