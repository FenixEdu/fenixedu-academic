package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.YearMonthDay;

public class DFACandidacy extends DFACandidacy_Base {

    public DFACandidacy(Person person, ExecutionDegree executionDegree) {
	super();
	init(person, executionDegree);

	new PreCandidacySituation(this);

	addCandidacyDocuments(new CandidacyDocument("curriculum.vitae"));
	addCandidacyDocuments(new CandidacyDocument("habilitation.certificate"));
	addCandidacyDocuments(new CandidacyDocument("second.habilitation.certificate"));
	addCandidacyDocuments(new CandidacyDocument("interest.letter"));

    }

    public DFACandidacy(Person person, ExecutionDegree executionDegree, YearMonthDay startDate) {
	this(person, executionDegree);
	if (startDate != null) {
	    this.setStartDate(startDate);
	}

	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
	new DFACandidacyEvent(administrativeOffice, person, this);

	new DegreeCurricularPlanServiceAgreement(person, executionDegree.getDegreeCurricularPlan().getServiceAgreementTemplate());
    }

    @Override
    public String getDescription() {
	return ResourceBundle.getBundle("resources.CandidateResources", LanguageUtils.getLocale())
		.getString("label.dfaCandidacy")
		+ " - "
		+ getExecutionDegree().getDegreeCurricularPlan().getName()
		+ " - "
		+ getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
	return new HashSet<Operation>();
    }

    @Override
    void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isConcluded() {
	return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
		.getCandidacySituationType() == CandidacySituationType.CANCELLED);
    }

    public void cancelEvents() {
	for (Event event : getPerson().getEventsByEventType(EventType.CANDIDACY_ENROLMENT)) {
	    DFACandidacyEvent candidacyEvent = (DFACandidacyEvent) event;
	    if (candidacyEvent.getCandidacy() == this) {
		candidacyEvent.cancel(AccessControl.getPerson().getEmployee());
	    }
	}
    }
}