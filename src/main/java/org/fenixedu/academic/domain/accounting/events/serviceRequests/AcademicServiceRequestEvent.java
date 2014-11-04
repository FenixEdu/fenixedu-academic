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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;

abstract public class AcademicServiceRequestEvent extends AcademicServiceRequestEvent_Base {

    protected AcademicServiceRequestEvent() {
        super();
    }

    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person,
            final AcademicServiceRequest academicServiceRequest) {
        super.init(administrativeOffice, eventType, person);
        checkParameters(academicServiceRequest);
        super.setAcademicServiceRequest(academicServiceRequest);
    }

    final protected void checkParameters(final AcademicServiceRequest academicServiceRequest) {
        if (academicServiceRequest == null) {
            throw new DomainException("AcademicServiceRequestEvent.academicServiceRequest.cannot.be.null");
        }
    }

    @Override
    public void setAcademicServiceRequest(final AcademicServiceRequest academicServiceRequest) {
        throw new DomainException("error.events.serviceRequests.AcademicServiceRequestEvent.cannot.modify.academicServiceRequest");
    }

    @Override
    final public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    final protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getWhenOccured());
    }

    final protected ExecutionYear getExecutionYear() {
        return getAcademicServiceRequest().getExecutionYear();
    }

    final public boolean isUrgentRequest() {
        return getAcademicServiceRequest().isUrgentRequest();
    }

    @Override
    protected void disconnect() {
        super.setAcademicServiceRequest(null);
        super.disconnect();
    }

    @Override
    public boolean isAcademicServiceRequestEvent() {
        return true;
    }

}
