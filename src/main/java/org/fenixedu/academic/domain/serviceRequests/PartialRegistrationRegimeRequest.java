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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.student.RegistrationRegime;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;

public class PartialRegistrationRegimeRequest extends PartialRegistrationRegimeRequest_Base {

    private PartialRegistrationRegimeRequest() {
        super();
    }

    public PartialRegistrationRegimeRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);
    }

    @Override
    protected void checkRulesToChangeState(AcademicServiceRequestSituationType situationType) {
        super.checkRulesToChangeState(situationType);
        if (situationType == AcademicServiceRequestSituationType.PROCESSING) {
            RegistrationRegime.getRegistrationRegimeVerifier().checkEctsCredits(getRegistration(), getExecutionYear(),
                    RegistrationRegimeType.PARTIAL_TIME);
        }
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToConclude()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }
    }

    @Override
    protected boolean isPayed() {
        return super.isPayed();
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));

            if (!getRegistration().isPartialRegime(getExecutionYear())) {
                new RegistrationRegime(getRegistration(), getExecutionYear(), RegistrationRegimeType.PARTIAL_TIME);
            }
        }
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.PARTIAL_REGIME_REQUEST;
    }

    @Override
    public EventType getEventType() {
        /*
         * For 2010/2011 partial registration is not charged
         */
        if (getExecutionYear().isAfterOrEquals(ExecutionYear.readExecutionYearByName("2010/2011"))) {
            return null;
        }

        return EventType.PARTIAL_REGISTRATION_REGIME_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
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
    public boolean isToPrint() {
        return false;
    }

}
