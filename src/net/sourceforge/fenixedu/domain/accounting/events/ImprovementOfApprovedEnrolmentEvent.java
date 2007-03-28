package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ImprovementOfApprovedEnrolmentEvent extends ImprovementOfApprovedEnrolmentEvent_Base {
    
    protected ImprovementOfApprovedEnrolmentEvent() {
	super();
    }

    public ImprovementOfApprovedEnrolmentEvent(final AdministrativeOffice administrativeOffice,
	    final Person person, final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	this();
	init(administrativeOffice, EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT, person, enrolmentEvaluations);
    }

    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType,
	    final Person person, final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	checkParameters(enrolmentEvaluations);
	getImprovementEnrolmentEvaluations().addAll(enrolmentEvaluations);
	super.init(administrativeOffice, eventType, person);
    }

    private void checkParameters(final Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	if (enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) {
	    throw new DomainException(
		    "error.accounting.events.EnrolmentInSpecialSeasonEvaluationEvent.enrolmentEvaluations.cannot.be.null");
	}
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	
	getDetailedDescription(labelFormatter);
	
	return labelFormatter;
    }

    private void getDetailedDescription(final LabelFormatter labelFormatter) {
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(String.valueOf(getImprovementEnrolmentEvaluationsCount()));
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.markSheet.curricularCourse", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(")");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	getDetailedDescription(labelFormatter);
	
	return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);    
    }

    @Override
    public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }
    
    public boolean hasImprovementOfApprovedEnrolmentPenaltyExemption() {
	return getImprovementOfApprovedEnrolmentPenaltyExemption() != null;
    }

    public ImprovementOfApprovedEnrolmentPenaltyExemption getImprovementOfApprovedEnrolmentPenaltyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof ImprovementOfApprovedEnrolmentPenaltyExemption) {
		return (ImprovementOfApprovedEnrolmentPenaltyExemption) exemption;
	    }
	}

	return null;
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    @Override
    public void removeImprovementEnrolmentEvaluations(EnrolmentEvaluation improvementEnrolmentEvaluations) {
        super.removeImprovementEnrolmentEvaluations(improvementEnrolmentEvaluations);
        
        if (!hasAnyImprovementEnrolmentEvaluations()) {
            this.delete();
        }
    }
    
}
