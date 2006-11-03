/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.CalculateGuideTotal;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateGuide extends Service {

    public InfoGuide run(InfoGuide infoGuide, String othersRemarks, Double othersPrice, String remarks,
	    GuideState situationOfGuide, String paymentType) throws FenixServiceException,
	    ExcepcaoPersistencia {

	GuideSituation guideSituation = null;

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
	Integer guideNumber = Guide.generateGuideNumber();
	infoGuide.setNumber(guideNumber);

	// Create the new Guide Situation
	InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
	infoGuideSituation.setState(new State(State.ACTIVE));
	infoGuideSituation.setRemarks(remarks);
	infoGuideSituation.setInfoGuide(infoGuide);

	Calendar calendar = Calendar.getInstance();
	infoGuideSituation.setDate(calendar.getTime());
	infoGuideSituation.setSituation(situationOfGuide);

	Person person = (Person) rootDomainObject.readPartyByOID(infoGuide.getInfoPerson()
		.getIdInternal());
	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoGuide
		.getInfoExecutionDegree().getIdInternal());
	final Party contributor = infoGuide.getInfoContributor() != null ? rootDomainObject
		.readPartyByOID(infoGuide.getInfoContributor().getIdInternal()) : person;

	Guide guide = new Guide();
	guide.setExecutionDegree(executionDegree);
	guide.setContributorParty(contributor);
	guide.setPerson(person);
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
	    guide.setPaymentType(PaymentType.valueOf(paymentType));
	    guide.setPaymentDate(calendar.getTime());
	}

	// Write the new Entries of the Guide
	for (InfoGuideEntry infoGuideEntryIter : (List<InfoGuideEntry>) infoGuide.getInfoGuideEntries()) {
	    GuideEntry guideEntry = new GuideEntry();

	    guideEntry.setDescription(infoGuideEntryIter.getDescription());
	    guideEntry.setDocumentType(infoGuideEntryIter.getDocumentType());
	    guideEntry.setGraduationType(infoGuideEntryIter.getGraduationType());
	    guideEntry.setPrice(infoGuideEntryIter.getPrice());
	    guideEntry.setQuantity(infoGuideEntryIter.getQuantity());

	    guide.addGuideEntries(guideEntry);

	}

	// Write the New Guide Situation
	guideSituation = new GuideSituation(situationOfGuide, remarks, calendar.getTime(), guide,
		new State(State.ACTIVE));

	InfoGuide result = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);
	result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());
	result.setInfoGuideSituation(infoGuideSituation);
	result.setInfoGuideSituations(new ArrayList(1));
	result.getInfoGuideSituations().add(infoGuideSituation);

	return result;
    }

}
