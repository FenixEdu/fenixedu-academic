package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.CalculateGuideTotal;
import net.sourceforge.fenixedu.util.State;

public class EditGuideInformation extends Service {

    public InfoGuide run(InfoGuide infoGuide, String[] quantityList, Integer contributorNumber,
            String othersRemarks, Integer othersQuantity, Double othersPrice)
            throws ExcepcaoPersistencia, FenixServiceException {

        // This will be the flag that indicates if a change has been made to the
        // Guide
        // No need to anything if there's no change ...
        boolean change = false;

        // Safety check to see if the Guide can be changed
        this.chekIfChangeable(infoGuide);

        // Read The Guide
        Guide guide = Guide.readByNumberAndYearAndVersion(infoGuide.getNumber(), infoGuide.getYear(),
                infoGuide.getVersion());

        // check if it's needed to change the Contributor
        String contributorNumberString = (contributorNumber == null) ? null : contributorNumber.toString();
        if ((contributorNumberString != null)
                && (!infoGuide.getInfoContributor().getContributorNumber().equals(contributorNumberString))) {
            change = true;
        } else {
            contributorNumberString = infoGuide.getInfoContributor().getContributorNumber();
        }

        final Party contributor = Party.readByContributorNumber(contributorNumberString);

        infoGuide.setInfoContributor(InfoContributor.newInfoFromDomain(contributor));

        // Check the quantities of the Guide Entries
        // The items without a quantity or with a 0 quantity will be deleted if
        // the guide is NON PAYED or
        // they won't appear in the new guide version if the guide has been
        // payed

        List<InfoGuideEntry> newInfoGuideEntries = new ArrayList<InfoGuideEntry>();
        List<InfoGuideEntry> guideEntriesToRemove = new ArrayList<InfoGuideEntry>();

        int quantityListIndex = 0;
        for (InfoGuideEntry infoGuideEntry : infoGuide.getInfoGuideEntries()) {
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
        GuideEntry othersGuideEntry = null;
        if ((othersPrice != null) && (othersQuantity != null) && (!othersPrice.equals(new Double(0)))
                && (!othersQuantity.equals(new Integer(0)))) {
            change = true;
            othersGuideEntry = new GuideEntry();
            othersGuideEntry.setDescription(othersRemarks);
            othersGuideEntry.setDocumentType(DocumentType.OTHERS);
            // TODO : In the future it's possible to be a Major Degree
            othersGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
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
                for (InfoGuideEntry infoGuideEntry : guideEntriesToRemove) {
                    GuideEntry guideEntry = rootDomainObject.readGuideEntryByOID(infoGuideEntry
                            .getIdInternal());
                    guideEntry.delete();
                }

                // Update the remaing guide entries
                for (InfoGuideEntry infoGuideEntry : newInfoGuideEntries) {
                    GuideEntry guideEntry = guide.getEntry(infoGuideEntry.getGraduationType(),
                            infoGuideEntry.getDocumentType(), infoGuideEntry.getDescription());
                    guideEntry.setQuantity(infoGuideEntry.getQuantity());
                }
                guide.setContributorParty(contributor);
            }

        } else if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.PAYED)) {
            // If there's a change ...
            if (change) {
                // Create a new Guide Version
                Guide newGuideVersion = this.createNewGuideVersion(infoGuide);

                // fill in the last field in the Others Guide Entry if
                // necessary
                if (othersGuideEntry != null) {
                    othersGuideEntry.setGuide(newGuideVersion);
                }

                // Create The new Situation
                GuideSituation guideSituation = new GuideSituation();
                guideSituation.setDate(infoGuide.getInfoGuideSituation().getDate());
                guideSituation.setGuide(newGuideVersion);
                guideSituation.setRemarks(infoGuide.getRemarks());
                guideSituation.setSituation(infoGuide.getInfoGuideSituation().getSituation());
                guideSituation.setState(new State(State.ACTIVE));

                PaymentTransaction paymentTransaction = null;
                GratuitySituation gratuitySituation = null;
                PersonAccount personAccount = guide.getPerson().getAssociatedPersonAccount();

                if (personAccount == null) {
                    personAccount = new PersonAccount(guide.getPerson());
                }

                // Write the Guide Entries
                for (InfoGuideEntry infoGuideEntry : (List<InfoGuideEntry>) newInfoGuideEntries) {
                    GuideEntry guideEntry = new GuideEntry();
                    infoGuideEntry.copyToDomain(infoGuideEntry, guideEntry);

                    // Reset id internal to allow persistence to write a new
                    // version
                    guideEntry.setGuide(newGuideVersion);

                    Person studentPerson = guide.getPerson();
                    Registration registration = Registration.readByUsername(studentPerson.getUsername());
                    ExecutionDegree executionDegree = guide.getExecutionDegree();

                    // Write Gratuity Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY)) {
                        executionDegree = guide.getExecutionDegree();
                        gratuitySituation = registration
                                .readGratuitySituationByExecutionDegree(executionDegree);

                        paymentTransaction = new GratuityTransaction(
                                guideEntry.getPrice(), new Timestamp(Calendar.getInstance()
                                        .getTimeInMillis()), guideEntry.getDescription(), infoGuide
                                        .getPaymentType(), TransactionType.GRATUITY_ADHOC_PAYMENT,
                                Boolean.FALSE, guide.getPerson(), personAccount, guideEntry,
                                gratuitySituation);

                        
                        gratuitySituation.updateValues();
                    }

                    // Write Insurance Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE)) {
                        paymentTransaction = new InsuranceTransaction(guideEntry
                                .getPrice(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                guideEntry.getDescription(), infoGuide.getPaymentType(),
                                TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, guide.getPerson(),
                                personAccount, guideEntry, executionDegree.getExecutionYear(), registration);
                    }
                }

                // Update the version number for the next Database Access
                infoGuide.setVersion(newGuideVersion.getVersion());

                // CREATE TRANSACTIONS!!!!!!!!!!!!!!!!!!!!!(Gratuity or
                // Insurance)

                // UPDATE remainigValue ou GRATUITY_SITUATION!!!!!
            }
        }

        // If there's no change
        if (!change) {
            throw new NoChangeMadeServiceException();
        }
        Guide newGuide = null;
        InfoGuide result = null;

        newGuide = Guide.readByNumberAndYearAndVersion(infoGuide.getNumber(), infoGuide.getYear(),
                infoGuide.getVersion());
        // Update the Guide Total
        InfoGuide infoGuideTemp = new InfoGuide();
        infoGuideTemp.setInfoGuideEntries(newInfoGuideEntries);

        result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(newGuide);

        result.setTotal(CalculateGuideTotal.calculate(result));

        newGuide.setTotal(result.getTotal());

        return result;
    }

    private void chekIfChangeable(InfoGuide infoGuide) throws FenixServiceException,
            ExcepcaoPersistencia {
        // Annuled Guides cannot be changed
        if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.ANNULLED))
            throw new InvalidChangeServiceException("Situation of Guide Is Annulled");

        List<Guide> guides = Guide.readByNumberAndYear(infoGuide.getNumber(), infoGuide.getYear());

        // If it's not the latest version ...
        if (guides.size() != infoGuide.getVersion().intValue())
            throw new InvalidChangeServiceException("Not the Latest Version");
    }

    private Guide createNewGuideVersion(InfoGuide infoGuide) throws ExcepcaoPersistencia {
        // Read the needed information from the DataBase
        Person person = Person.readPersonByUsername(infoGuide.getInfoPerson().getUsername());
        Party contributor = Party.readByContributorNumber(infoGuide.getInfoContributor().getContributorNumber());

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(
                infoGuide.getInfoExecutionDegree().getIdInternal());
        Guide guide = new Guide();

        // Set the fields
        guide.setContributorParty(contributor);
        guide.setPerson(person);
        guide.setExecutionDegree(executionDegree);

        guide.setCreationDate(Calendar.getInstance().getTime());
        guide.setGuideRequester(infoGuide.getGuideRequester());
        guide.setNumber(infoGuide.getNumber());
        guide.setYear(infoGuide.getYear());
        guide.setVersion(infoGuide.getVersion() + 1);
        guide.setPaymentDate(infoGuide.getPaymentDate());
        guide.setPaymentType(infoGuide.getPaymentType());
        guide.setRemarks(null);

        // The total will be calculated afterwards
        guide.setTotal(0.0);

        return guide;
    }

}
