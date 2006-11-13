package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class ServiceAgreementTemplate extends ServiceAgreementTemplate_Base {

    protected ServiceAgreementTemplate() {
	super();
	super.setOjbConcreteClass(this.getClass().getName());
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setCreationDate(new DateTime());
    }

    @Override
    public List<PostingRule> getPostingRules() {
	return Collections.unmodifiableList(super.getPostingRules());
    }

    @Override
    public Iterator<PostingRule> getPostingRulesIterator() {
	return getPostingRulesSet().iterator();
    }

    @Override
    public Set<PostingRule> getPostingRulesSet() {
	return Collections.unmodifiableSet(super.getPostingRulesSet());
    }

    @Override
    public void removePostingRules(PostingRule postingRules) {
	super.removePostingRules(postingRules);
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
	throw new DomainException(
		"error.accounting.agreement.ServiceAgreementTemplate.cannot.modify.creationDate");
    }

    public Set<PostingRule> getActivePostingRules() {
	return getActivePostingRules(new DateTime());
    }

    private Set<PostingRule> getActivePostingRules(DateTime when) {
	final Set<PostingRule> activePostingRules = new HashSet<PostingRule>();
	for (final PostingRule postingRule : getPostingRules()) {
	    if (postingRule.isActiveForDate(when)) {
		activePostingRules.add(postingRule);
	    }
	}

	return activePostingRules;
    }

    public Set<PostingRule> getActiveVisiblePostingRules() {
	return getActiveVisiblePostingRules(new DateTime());
    }

    public Set<PostingRule> getActiveVisiblePostingRules(DateTime when) {
	final Set<PostingRule> result = new HashSet<PostingRule>();
	for (final PostingRule postingRule : getPostingRules()) {
	    if (postingRule.isActiveForDate(when) && postingRule.isVisible()) {
		result.add(postingRule);
	    }
	}

	return result;
    }

    public PostingRule findPostingRuleByEventTypeAndDate(EventType eventType, DateTime when) {
	final PostingRule postingRule = getPostingRuleByEventTypeAndDate(eventType, when);

	if (postingRule == null) {
	    throw new DomainException(
		    "error.accounting.agreement.ServiceAgreementTemplate.cannot.find.postingRule.for.eventType.and.date");
	}

	return postingRule;

    }

    private PostingRule getPostingRuleByEventTypeAndDate(EventType eventType, DateTime when) {
	for (final PostingRule postingRule : getActivePostingRules(when)) {
	    if (postingRule.getEventType() == eventType) {
		return postingRule;
	    }
	}

	return null;
    }

    public boolean containsPostingRule(final EventType eventType, final DateTime when) {
	return getPostingRuleByEventTypeAndDate(eventType, when) != null;
    }

    public boolean hasServiceAgreementForPerson(Person person) {
	return (getServiceAgreementForPerson(person) != null);
    }

    public ServiceAgreement getServiceAgreementForPerson(Person person) {
	for (final ServiceAgreement serviceAgreement : getServiceAgreements()) {
	    if (serviceAgreement.getPerson() == person) {
		return serviceAgreement;
	    }
	}

	return null;
    }

    public final void delete() {
	throw new DomainException(
		"error.accounting.serviceAgreementTemplates.ServiceAgreementTemplate.cannot.be.deleted");

    }

}
