/*
 * Created on 21/Mar/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoGuideSituation;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CalculateGuideTotal;
import Util.DocumentType;
import Util.GraduationType;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateGuide implements IService {

    /**
     * The actor of this class.
     */
    public CreateGuide() {
    }

    public InfoGuide run(InfoGuide infoGuide, String othersRemarks, Double othersPrice, String remarks,
            SituationOfGuide situationOfGuide, String paymentType) throws FenixServiceException {

        ISuportePersistente sp = null;
        IContributor contributor = null;
        IGuide guide = new Guide();
        IGuideSituation guideSituation = null;
        IPessoa person = null;

        // Check the Guide Situation
        if (situationOfGuide.equals(SituationOfGuide.ANNULLED_TYPE))
            throw new InvalidSituationServiceException();

        InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
        if ((othersPrice != null) && (othersPrice.floatValue() > 0)) {
            infoGuideEntry.setDescription(othersRemarks);
            infoGuideEntry.setPrice(othersPrice);
            infoGuideEntry.setInfoGuide(infoGuide);
            infoGuideEntry.setDocumentType(DocumentType.OTHERS_TYPE);
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
            infoGuideEntry.setQuantity(new Integer(1));
            List entries = infoGuide.getInfoGuideEntries();
            entries.add(infoGuideEntry);
            infoGuide.setInfoGuideEntries(entries);
        }

        // Calculate the Guide Total Price

        infoGuide.setTotal(CalculateGuideTotal.calculate(infoGuide));

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
                guide.setPaymentType(new PaymentType(paymentType));
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
            Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
            List guideEntries = new ArrayList();
            while (iterator.hasNext()) {
                IGuideEntry guideEntry = Cloner.copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) iterator
                        .next());
                sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                guideEntries.add(guideEntry);
                guideEntry.setGuide(guide);
            }

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