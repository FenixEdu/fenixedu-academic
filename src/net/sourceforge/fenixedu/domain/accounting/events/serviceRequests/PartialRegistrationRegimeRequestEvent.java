package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.PartialRegistrationRegimeRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PartialRegistrationRegimeRequestEvent extends PartialRegistrationRegimeRequestEvent_Base {

	private PartialRegistrationRegimeRequestEvent() {
		super();
	}

	public PartialRegistrationRegimeRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
			final PartialRegistrationRegimeRequest request) {
		this();
		super.init(administrativeOffice, EventType.PARTIAL_REGISTRATION_REGIME_REQUEST, person, request);
	}

	@Override
	public PartialRegistrationRegimeRequest getAcademicServiceRequest() {
		return (PartialRegistrationRegimeRequest) super.getAcademicServiceRequest();
	}

	@Override
	public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
		final LabelFormatter labelFormatter = new LabelFormatter();
		labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
		if (getAcademicServiceRequest().hasExecutionYear()) {
			labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
		}
		return labelFormatter;
	}

	@Override
	public PostingRule getPostingRule() {
		Set<PostingRule> activePostingRules =
				getAdministrativeOffice().getServiceAgreementTemplate().getActivePostingRules(
						getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight());

		return (PostingRule) CollectionUtils.find(activePostingRules, new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				return ((PostingRule) arg0).getEventType().equals(getEventType())
						&& ((PartialRegistrationRegimeRequestPR) arg0).getExecutionYear().equals(
								getAcademicServiceRequest().getExecutionYear());
			}

		});
	}
}
