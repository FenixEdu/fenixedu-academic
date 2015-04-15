/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.PartialRegistrationRegimeRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

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
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        if (getAcademicServiceRequest().getExecutionYear() != null) {
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
