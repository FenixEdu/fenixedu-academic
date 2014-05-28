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
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

import org.apache.commons.lang.StringUtils;

public class FreeSolicitationAcademicRequest extends FreeSolicitationAcademicRequest_Base {

    protected FreeSolicitationAcademicRequest() {
        super();
    }

    public FreeSolicitationAcademicRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setSubject(bean.getSubject());
        super.setPurpose(bean.getPurpose());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (StringUtils.isEmpty(bean.getSubject())) {
            throw new DomainException("error.FreeSolicitationAcademicRequest.invalid.subject");
        }
    }

    @Override
    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

    }

    @Override
    public void setSubject(String subject) {
        throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.subject");
    }

    @Override
    public void setPurpose(String purpose) {
        throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.purpose");
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.FREE_SOLICITATION_ACADEMIC_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
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
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasPurpose() {
        return getPurpose() != null;
    }

}
