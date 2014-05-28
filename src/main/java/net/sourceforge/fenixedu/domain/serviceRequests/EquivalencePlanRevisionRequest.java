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
package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public class EquivalencePlanRevisionRequest extends EquivalencePlanRevisionRequest_Base {

    protected EquivalencePlanRevisionRequest() {
        super();
    }

    public EquivalencePlanRevisionRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setEquivalencePlanRequest(bean.getEquivalencePlanRequest());
    }

    @Override
    public void setEquivalencePlanRequest(final EquivalencePlanRequest equivalencePlanRequest) {
        throw new DomainException("error.EquivalencePlanRevisionRequest.cannot.modify.equivalencePlanRequest");
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        final EquivalencePlanRequest equivalencePlanRequest = bean.getEquivalencePlanRequest();
        final ExecutionYear executionYear = bean.getExecutionYear();

        if (equivalencePlanRequest == null) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.equivalencePlanRequest.cannot.be.null");
        }

        if (!equivalencePlanRequest.hasConcluded()) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.equivalencePlanRequest.is.not.concluded");
        }

        if (executionYear == null) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.executionYear.cannot.be.null");
        }
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.REVISION_EQUIVALENCE_PLAN;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    protected void disconnect() {
        super.setEquivalencePlanRequest(null);
        super.disconnect();
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
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
    public boolean hasPersonalInfo() {
        return false;
    }

    @Deprecated
    public boolean hasEquivalencePlanRequest() {
        return getEquivalencePlanRequest() != null;
    }

}
