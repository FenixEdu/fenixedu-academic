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
package org.fenixedu.academic.domain.serviceRequests;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.DuplicateRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import org.fenixedu.academic.util.Money;

public class DuplicateRequest extends DuplicateRequest_Base {

    protected DuplicateRequest() {
        super();
    }

    public DuplicateRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setDescription(bean.getDescription());
        super.setAmountToPay(bean.getAmountToPay());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (StringUtils.isEmpty(bean.getDescription())) {
            throw new DomainException("error.DuplicateRequest.invalid.description");
        }
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public EventType getEventType() {
        return EventType.DUPLICATE_REQUEST;

    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DUPLICATE_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType()) + " - " + super.getDescription();
    }

}
