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
package net.sourceforge.fenixedu.domain.mobility.outbound;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacyPeriodConfirmationOption extends OutboundMobilityCandidacyPeriodConfirmationOption_Base
        implements Comparable<OutboundMobilityCandidacyPeriodConfirmationOption> {

    public OutboundMobilityCandidacyPeriodConfirmationOption(final OutboundMobilityCandidacyPeriod period,
            final String optionValue, final Boolean availableForCandidates) {
        setRootDomainObject(Bennu.getInstance());
        setOptionValue(optionValue);
        setAvailableForCandidates(availableForCandidates);
        setOutboundMobilityCandidacyPeriod(period);
    }

    @Atomic
    public void delete() {
        setOutboundMobilityCandidacyPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyPeriodConfirmationOption o) {
        return getExternalId().compareTo(o.getExternalId());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission> getSubmissionsThatSelectedOption() {
        return getSubmissionsThatSelectedOptionSet();
    }

    @Deprecated
    public boolean hasAnySubmissionsThatSelectedOption() {
        return !getSubmissionsThatSelectedOptionSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasOptionValue() {
        return getOptionValue() != null;
    }

    @Deprecated
    public boolean hasOutboundMobilityCandidacyPeriod() {
        return getOutboundMobilityCandidacyPeriod() != null;
    }

    @Deprecated
    public boolean hasAvailableForCandidates() {
        return getAvailableForCandidates() != null;
    }

}
