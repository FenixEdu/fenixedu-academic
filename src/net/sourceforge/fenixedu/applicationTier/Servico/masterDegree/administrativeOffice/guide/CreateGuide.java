/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.CalculateGuideTotal;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            GuideState situationOfGuide, String paymentType) throws FenixServiceException {

        ISuportePersistente sp = null;
        IGuide guide = new Guide();
        IGuideSituation guideSituation = null;

        // Check the Guide Situation
        if (situationOfGuide.equals(GuideState.ANNULLED))
            throw new InvalidSituationServiceException();

        InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
        if ((othersPrice != null) && (othersPrice.floatValue() > 0)) {
            infoGuideEntry.setDescription(othersRemarks);
            infoGuideEntry.setPrice(othersPrice);
            infoGuideEntry.setInfoGuide(infoGuide);
            infoGuideEntry.setDocumentType(DocumentType.OTHERS);
            infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
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
        infoGuideSituation.setDate(calendar.getTime());
        infoGuideSituation.setSituation(situationOfGuide);

        guide = InfoGuide.newDomainFromInfo(infoGuide);

        guide.setIdInternal(null);
        
        
        //      FIXME: Remove the : guide.setGuideEntries(null); WHY????
        guide.setGuideEntries(null);
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.getIPersistentGuide().simpleLockWrite(guide);
            if (situationOfGuide.equals(GuideState.PAYED)) {
                guide.setPaymentType(PaymentType.valueOf(paymentType));
                guide.setPaymentDate(calendar.getTime());
            }

            // Get the Execution Degree

            // Write the new Guide

            // Write the new Entries of the Guide
            Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
            List guideEntries = new ArrayList();
            while (iterator.hasNext()) {
                IGuideEntry guideEntry = InfoGuideEntry.newDomainFromInfo((InfoGuideEntry) iterator.next());
                sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                guideEntries.add(guideEntry);
                guideEntry.setGuide(guide);
            }
            guide.setGuideEntries(guideEntries);
            


            // Write the New Guide Situation
            
            guideSituation = new GuideSituation(situationOfGuide,remarks,calendar.getTime(),guide,new State(State.ACTIVE));
            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);
            
            guide.setGuideSituations(new ArrayList());
            guide.getGuideSituations().add(guideSituation);
            
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        InfoGuide result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);
        result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());
        result.setInfoGuideSituation(infoGuideSituation);
        result.setInfoGuideSituations(new ArrayList(1));
        result.getInfoGuideSituations().add(infoGuideSituation);

        return result;
    }

}