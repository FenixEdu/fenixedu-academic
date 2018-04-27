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

import java.util.Arrays;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.ReingressionPeriod;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.StudentReingressionRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import org.joda.time.DateTime;

public class StudentReingressionRequest extends StudentReingressionRequest_Base {

    static final public List<RegistrationStateType> ALLOWED_TYPES = Arrays.asList(

    RegistrationStateType.FLUNKED,

    RegistrationStateType.INTERRUPTED,

    RegistrationStateType.EXTERNAL_ABANDON);

    protected StudentReingressionRequest() {
        super();
    }

    public StudentReingressionRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();

        checkParameters(bean);
        checkRulesToCreate(bean);

        super.init(bean);
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (bean.getExecutionYear() == null) {
            throw new DomainException("error.StudentReingressionRequest.executionYear.cannot.be.null");
        }
    }

    private void checkRulesToCreate(final RegistrationAcademicServiceRequestCreateBean bean) {
        final Registration registration = bean.getRegistration();
        final ExecutionYear executionYear = bean.getExecutionYear();
        final DateTime requestDate = bean.getRequestDate();

        if (!hasValidState(registration)) {
            throw new DomainException("error.StudentReingressionRequest.registration.with.invalid.state");
        }

        if (registration.isRegistrationConclusionProcessed()) {
            throw new DomainException("error.StudentReingressionRequest.registration.has.conclusion.processed");
        }

        if (!isEnrolmentPeriodOpen(registration, executionYear, requestDate)) {
            throw new DomainException("error.StudentReingressionRequest.out.of.enrolment.period");
        }

        if (alreadyHasRequest(registration, executionYear)) {
            throw new DomainException("error.StudentReingressionRequest.already.has.request.to.same.executionYear");
        }

        if (registration.getStudent().isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt(executionYear)) {
            throw new DomainException("error.StudentReingressionRequest.student.has.debts");
        }
    }

    private boolean alreadyHasRequest(final Registration registration, final ExecutionYear executionYear) {
        for (final AcademicServiceRequest request : registration.getAcademicServiceRequests(getClass(), executionYear)) {
            if (!request.finishedUnsuccessfully()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasValidState(final Registration registration) {
        return registration.hasAnyState(ALLOWED_TYPES);
    }

    private boolean isEnrolmentPeriodOpen(final Registration registration, final ExecutionYear executionYear,
            final DateTime requestDate) {
        final DegreeCurricularPlan degreeCurricularPlan = registration.getLastDegreeCurricularPlan();
        return hasOpenEnrolmentPeriod(degreeCurricularPlan, executionYear, requestDate);
    }

    private boolean hasOpenEnrolmentPeriod(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear,
            final DateTime requestDate) {

        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            final ReingressionPeriod period = degreeCurricularPlan.getReingressionPeriod(executionSemester);
            if (period != null && period.containsDate(requestDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.REINGRESSION;
    }

    @Override
    public EventType getEventType() {
        return EventType.STUDENT_REINGRESSION_REQUEST;
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isNew()) {
        } else if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
        }
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToProcess()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());

        } else if (academicServiceRequestBean.isToConclude() && hasExecutionDegree()) {
            final RegistrationState state =
                    RegistrationState.createRegistrationState(getRegistration(), academicServiceRequestBean.getResponsible(),
                            academicServiceRequestBean.getFinalSituationDate(), RegistrationStateType.REGISTERED);

            if (getRegistration().getActiveState() != state) {
                throw new DomainException("StudentReingressionRequest.reingression.must.be.active.state.after.request.conclusion");
            }
        }
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getRegistration().getLastDegreeCurricularPlan();
    }

    private boolean hasExecutionDegree() {
        return getDegreeCurricularPlan().hasExecutionDegreeFor(getExecutionYear());
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

}
