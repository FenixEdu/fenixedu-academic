/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public abstract class ServiceAgreementTemplate extends ServiceAgreementTemplate_Base {

    protected ServiceAgreementTemplate() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        super.setCreationDate(new DateTime());
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
        throw new DomainException("error.accounting.agreement.ServiceAgreementTemplate.cannot.modify.creationDate");
    }

    public Set<PostingRule> getActivePostingRules() {
        return getActivePostingRules(new DateTime());
    }

    public Set<PostingRule> getActivePostingRules(DateTime when) {
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

    public PostingRule findPostingRuleByEventType(EventType eventType) {
        return findPostingRuleByEventTypeAndDate(eventType, new DateTime());
    }

    public PostingRule findPostingRuleByEventTypeAndDate(EventType eventType, DateTime when) {
        final PostingRule postingRule = getPostingRuleByEventTypeAndDate(eventType, when);

        if (postingRule == null) {
            throw new DomainException(
                    "error.accounting.agreement.ServiceAgreementTemplate.cannot.find.postingRule.for.eventType.and.date.desc",
                    when.toDateTime().toString("dd-MM-yyyy HH:mm"), getEnumerationResourcesString(eventType.getQualifiedName()));
        }

        return postingRule;

    }

    public String getEnumerationResourcesString(String name) {
        return BundleUtil.getString(Bundle.ENUMERATION, name);
    }

    public PostingRule findPostingRuleBy(EventType eventType, DateTime startDate, DateTime endDate) {
        final List<PostingRule> activePostingRulesInPeriod = new ArrayList<PostingRule>();
        for (final PostingRule postingRule : getPostingRulesSet()) {
            if (postingRule.isActiveForPeriod(startDate, endDate) && postingRule.getEventType() == eventType) {
                activePostingRulesInPeriod.add(postingRule);
            }
        }

        return activePostingRulesInPeriod.isEmpty() ? null : Collections.max(activePostingRulesInPeriod,
                PostingRule.COMPARATOR_BY_START_DATE);

    }

    public PostingRule getPostingRuleByEventTypeAndDate(EventType eventType, DateTime when) {
        for (final PostingRule postingRule : getActivePostingRules(when)) {
            if (postingRule.getEventType() == eventType) {
                return postingRule;
            }
        }
        return null;
    }

    public boolean hasPostingRuleFor(final EventType eventType) {
        return hasPostingRuleFor(eventType, new DateTime());
    }

    public boolean hasPostingRuleFor(final EventType eventType, final DateTime when) {
        return getPostingRuleByEventTypeAndDate(eventType, when) != null;
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
        if (!getServiceAgreementsSet().isEmpty()) {
            throw new DomainException("error.accounting.serviceAgreementTemplates.ServiceAgreementTemplate.cannot.be.deleted");
        }
        if (!getPostingRulesSet().isEmpty()) {
            throw new DomainException("error.accounting.serviceAgreementTemplates.ServiceAgreementTemplate.cannot.be.deleted");
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public ServiceAgreementTemplatePaymentPlan getDefaultPaymentPlan(final ExecutionYear executionYear) {
        for (final ServiceAgreementTemplatePaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan.getExecutionYear() == executionYear && paymentPlan.isDefault()) {
                return paymentPlan;
            }
        }
        return null;
    }

    public boolean hasDefaultPaymentPlan(final ExecutionYear executionYear) {
        return getDefaultPaymentPlan(executionYear) != null;
    }

    public boolean hasActivePostingRuleFor(final EventType eventType) {
        return getPostingRuleByEventTypeAndDate(eventType, new DateTime()) != null;
    }

    public Set<PostingRule> getAllPostingRulesFor(final EventType eventType) {
        final Set<PostingRule> result = new HashSet<PostingRule>();

        for (final PostingRule postingRule : super.getPostingRulesSet()) {
            if (postingRule.getEventType() == eventType) {
                result.add(postingRule);
            }
        }

        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ServiceAgreement> getServiceAgreements() {
        return getServiceAgreementsSet();
    }

    @Deprecated
    public boolean hasAnyServiceAgreements() {
        return !getServiceAgreementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplatePaymentPlan> getPaymentPlans() {
        return getPaymentPlansSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPlans() {
        return !getPaymentPlansSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.PostingRule> getPostingRules() {
        return getPostingRulesSet();
    }

    @Deprecated
    public boolean hasAnyPostingRules() {
        return !getPostingRulesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

}
