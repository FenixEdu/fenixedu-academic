/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonAccount;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;
import net.sourceforge.fenixedu.util.CalculateGuideTotal;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.transactions.TransactionType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class EditGuideInformation implements IService {

    /**
     * The actor of this class.
     */
    public EditGuideInformation() {
    }

    public InfoGuide run(InfoGuide infoGuide, String[] quantityList, Integer contributorNumber,
            String othersRemarks, Integer othersQuantity, Double othersPrice) throws Exception {

        ISuportePersistente sp = null;

        // This will be the flag that indicates if a change has been made to the
        // Guide
        // No need to anything if there's no change ...
        boolean change = false;

        IContributor contributor = null;
        IGuide guide = new Guide();
        IGuideEntry othersGuideEntry = null;

        // Safety check to see if the Guide can be changed
        this.chekIfChangeable(infoGuide);

        // Read The Guide
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(),
                    infoGuide.getYear(), infoGuide.getVersion());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // check if it's needed to change the Contributor

        if ((contributorNumber != null)
                && (!infoGuide.getInfoContributor().getContributorNumber().equals(contributorNumber))) {
            change = true;
        } else {
            contributorNumber = infoGuide.getInfoContributor().getContributorNumber();
        }

        // Read the Contributor
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributor));

        // Check the quantities of the Guide Entries
        // The items without a quantity or with a 0 quantity will be deleted if
        // the guide is NON PAYED or
        // they won't appear in the new guide version if the guide has been
        // payed

        Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
        List newInfoGuideEntries = new ArrayList();
        List guideEntriesToRemove = new ArrayList();

        int quantityListIndex = 0;
        while (iterator.hasNext()) {
            InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
            if ((quantityList[quantityListIndex] == null)
                    || (quantityList[quantityListIndex].length() == 0)
                    || (quantityList[quantityListIndex].equals("0"))) {
                // Add to items to remove
                guideEntriesToRemove.add(infoGuideEntry);
                change = true;
            } else {
                if (!infoGuideEntry.getQuantity().equals(new Integer(quantityList[quantityListIndex])))
                    change = true;
                infoGuideEntry.setQuantity(new Integer(quantityList[quantityListIndex]));
                newInfoGuideEntries.add(infoGuideEntry);
            }
            quantityListIndex++;
        }

        // Check if a Others Guide Entry will be Added
        if ((othersPrice != null) && (othersQuantity != null) && (!othersPrice.equals(new Double(0)))
                && (!othersQuantity.equals(new Integer(0)))) {
            change = true;
            othersGuideEntry = new GuideEntry();
            othersGuideEntry.setDescription(othersRemarks);
            othersGuideEntry.setDocumentType(DocumentType.OTHERS);
            // TODO : In the future it's possible to be a Major Degree
            othersGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
            othersGuideEntry.setPrice(othersPrice);
            othersGuideEntry.setQuantity(othersQuantity);
        }

        if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.NON_PAYED)) {
            // If there's a change ...
            if (change) {

                // fill in the last field in the Others Guide Entry if necessary
                if (othersGuideEntry != null) {
                    othersGuideEntry.setGuide(guide);
                }
                // Remove the Guide entries wich have been deleted
                Iterator entryIterator = guideEntriesToRemove.iterator();
                while (entryIterator.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
                    try {
                        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
                        IGuideEntry guideEntry = sp.getIPersistentGuideEntry()
                                .readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide,
                                        infoGuideEntry.getGraduationType(),
                                        infoGuideEntry.getDocumentType(),
                                        infoGuideEntry.getDescription());
                        sp.getIPersistentGuideEntry().delete(guideEntry);
                    } catch (ExcepcaoPersistencia ex) {
                        FenixServiceException newEx = new FenixServiceException(
                                "Persistence layer error");
                        newEx.fillInStackTrace();
                        throw newEx;
                    }
                }

                // Update the remaing guide entries
                entryIterator = newInfoGuideEntries.iterator();
                while (entryIterator.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
                    try {
                        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
                        IGuideEntry guideEntry = sp.getIPersistentGuideEntry()
                                .readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide,
                                        infoGuideEntry.getGraduationType(),
                                        infoGuideEntry.getDocumentType(),
                                        infoGuideEntry.getDescription());
                        guideEntry.setQuantity(infoGuideEntry.getQuantity());
                    } catch (ExcepcaoPersistencia ex) {
                        FenixServiceException newEx = new FenixServiceException(
                                "Persistence layer error");
                        newEx.fillInStackTrace();
                        throw newEx;
                    }
                }
                sp.getIPersistentGuide().simpleLockWrite(guide);
                guide.setContributor(contributor);

            }

        } else if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.PAYED)) {
            // If there's a change ...
            if (change) {

                // Create a new Guide Version
                IGuide newGuideVersion = null;
                try {
                    sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
                    sp.getIPersistentGuide().simpleLockWrite(newGuideVersion);
                    newGuideVersion = this.createNewGuideVersion(infoGuide);

                    // fill in the last field in the Others Guide Entry if
                    // necessary
                    if (othersGuideEntry != null) {
                        othersGuideEntry.setGuide(newGuideVersion);
                    }

                    // Create The new Situation
                    IGuideSituation guideSituation = new GuideSituation();

                    sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);
                    guideSituation.setDate(infoGuide.getInfoGuideSituation().getDate());
                    guideSituation.setGuide(newGuideVersion);
                    guideSituation.setRemarks(infoGuide.getRemarks());
                    guideSituation.setSituation(infoGuide.getInfoGuideSituation().getSituation());
                    guideSituation.setState(new State(State.ACTIVE));

                    // Write the new Guide Version

                    //					// Make sure that everything is written before reading
                    // ...
                    //					sp.confirmarTransaccao();
                    //					sp.iniciarTransaccao();

                    //For Transactions Creation
                    IPersistentTransaction persistentTransaction = sp.getIPersistentTransaction();
                    IPaymentTransaction paymentTransaction = null;
                    IGratuitySituation gratuitySituation = null;
                    IPersistentPersonAccount persistentPersonAccount = sp.getIPersistentPersonAccount();
                    IPersonAccount personAccount = persistentPersonAccount.readByPerson(guide
                            .getPerson());
                    IPersistentGratuitySituation persistentGratuitySituation = sp
                            .getIPersistentGratuitySituation();

                    // Write the Guide Entries
                    Iterator guideEntryIterator = newInfoGuideEntries.iterator();
                    while (guideEntryIterator.hasNext()) {

                        IGuideEntry guideEntry = InfoGuideEntry.newDomainFromInfo((InfoGuideEntry) guideEntryIterator
                                        .next());

                        // Reset id internal to allow persistence to write a new
                        // version
                        sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                        guideEntry.setIdInternal(null);
                        guideEntry.setGuide(newGuideVersion);

                        IPerson studentPerson = guide.getPerson();
                        IStudent student = sp.getIPersistentStudent().readByUsername(
                                studentPerson.getUsername());
                        IExecutionDegree executionDegree = guide.getExecutionDegree();

                        //Write Gratuity Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY)) {

                            executionDegree = guide.getExecutionDegree();
                            gratuitySituation = persistentGratuitySituation
                                    .readGratuitySituationByExecutionDegreeAndStudent(executionDegree,
                                            student);

                            paymentTransaction = new GratuityTransaction(guideEntry.getPrice(),
                                    new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry
                                            .getDescription(), infoGuide.getPaymentType(),
                                    TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, guide
                                            .getPerson(), personAccount, guideEntry, gratuitySituation);

                            persistentTransaction.lockWrite(paymentTransaction);

                            //Update GratuitySituation
                            persistentGratuitySituation.lockWrite(gratuitySituation);

                            Double remainingValue = gratuitySituation.getRemainingValue();

                            gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                                    + paymentTransaction.getValue().doubleValue()));

                        }

                        //Write Insurance Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE)) {
                            paymentTransaction = new InsuranceTransaction(guideEntry.getPrice(),
                                    new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry
                                            .getDescription(), infoGuide.getPaymentType(),
                                    TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, guide.getPerson(),
                                    personAccount, guideEntry, executionDegree.getExecutionYear(),
                                    student);

                            persistentTransaction.lockWrite(paymentTransaction);
                        }

                    }

                    // Update the version number for the next Database Access
                    infoGuide.setVersion(newGuideVersion.getVersion());

                    // CREATE TRANSACTIONS!!!!!!!!!!!!!!!!!!!!!(Gratuity or
                    // Insurance)

                    //UPDATE remainigValue ou GRATUITY_SITUATION!!!!!

                } catch (ExcepcaoPersistencia ex) {
                    FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                    newEx.fillInStackTrace();
                    throw newEx;
                }
            }

        }
        // If there's no change

        if (!change) {
            throw new NoChangeMadeServiceException();
        }
        IGuide newGuide = null;
        InfoGuide result = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // write the Others Guide Entry if necessary
            if (othersGuideEntry != null) {
                sp.getIPersistentGuideEntry().simpleLockWrite(othersGuideEntry);
            }
            // Make sure that everything is written before reading ...
            sp.confirmarTransaccao();
            sp.iniciarTransaccao();

            newGuide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(),
                    infoGuide.getYear(), infoGuide.getVersion());
            // Update the Guide Total
            InfoGuide infoGuideTemp = new InfoGuide();
            infoGuideTemp.setInfoGuideEntries(newInfoGuideEntries);

            result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(newGuide);

            result.setTotal(CalculateGuideTotal.calculate(result));

            sp.getIPersistentGuide().simpleLockWrite(newGuide);
            newGuide.setTotal(result.getTotal());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        return result;

    }

    private void chekIfChangeable(InfoGuide infoGuide) throws FenixServiceException {
        ISuportePersistente sp = null;
        List guides = null;

        // Annuled Guides cannot be changed
        if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.ANNULLED))
            throw new InvalidChangeServiceException("Situation of Guide Is Annulled");

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            guides = sp.getIPersistentGuide().readByNumberAndYear(infoGuide.getNumber(),
                    infoGuide.getYear());

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", e);
            throw newEx;
        }

        // If it's not the latest version ...
        if (guides.size() != infoGuide.getVersion().intValue())
            throw new InvalidChangeServiceException("Not the Latest Version");
    }

    private IGuide createNewGuideVersion(InfoGuide infoGuide) throws FenixServiceException {
        IGuide guide = new Guide();
        ISuportePersistente sp = null;
        IContributor contributor = null;
        IPerson person = null;
        IExecutionDegree executionDegree = null;

        // Read the needed information from the DataBase
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(
                    infoGuide.getInfoPerson().getUsername());
            contributor = sp.getIPersistentContributor().readByContributorNumber(
                    infoGuide.getInfoContributor().getContributorNumber());
            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                    infoGuide.getInfoExecutionDegree().getInfoExecutionYear().getYear());

            executionDegree = sp.getIPersistentExecutionDegree()
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan()
                                    .getInfoDegree().getSigla(),
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
                            executionYear);

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // Set the fields
        guide.setContributor(contributor);
        guide.setPerson(person);
        guide.setExecutionDegree(executionDegree);

        guide.setCreationDate(Calendar.getInstance().getTime());
        guide.setGuideRequester(infoGuide.getGuideRequester());
        guide.setNumber(infoGuide.getNumber());
        guide.setYear(infoGuide.getYear());
        guide.setVersion(new Integer(infoGuide.getVersion().intValue() + 1));
        guide.setPaymentDate(infoGuide.getPaymentDate());
        guide.setPaymentType(infoGuide.getPaymentType());
        guide.setRemarks(null);

        // The total will be calculated afterwards
        guide.setTotal(new Double(0));

        return guide;
    }

}

