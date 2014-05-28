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
package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

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

    @Deprecated
    public boolean hasAcademicServiceRequest() {
        return getAcademicServiceRequest() != null;
    }

}
