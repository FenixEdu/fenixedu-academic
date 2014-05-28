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

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

import org.apache.commons.lang.StringUtils;

public class ExtraExamRequest extends ExtraExamRequest_Base {

    private static List<StudentStatuteType> acceptedStatutes = Arrays.asList(StudentStatuteType.ASSOCIATIVE_LEADER);

    protected ExtraExamRequest() {
        super();
    }

    public ExtraExamRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setEnrolment(bean.getEnrolment());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        final Registration registration = bean.getRegistration();
        final ExecutionYear executionYear = bean.getExecutionYear();
        final Enrolment enrolment = bean.getEnrolment();

        if (executionYear == null) {
            throw new DomainException("error.ExtraExamRequest.executionYear.cannot.be.null");
        }

        if (!registration.hasEnrolments(enrolment)) {
            throw new DomainException("error.ExtraExamRequest.registration.doesnot.have.enrolment");
        }

        if (!studentHasValidStatutes(registration, enrolment)) {
            throw new DomainException("error.ExtraExamRequest.registration.doesnot.have.valid.statutes");
        }

        if (registrationAlreadyHasRequest(registration, enrolment, executionYear)) {
            throw new DomainException("error.ExtraExamRequest.registration.already.has.same.request", enrolment.getName()
                    .getContent(), executionYear.getYear());
        }
    }

    private boolean registrationAlreadyHasRequest(final Registration registration, final Enrolment enrolment,
            final ExecutionYear executionYear) {
        for (final AcademicServiceRequest request : registration.getAcademicServiceRequests(this.getClass())) {
            final ExtraExamRequest extraExamRequest = (ExtraExamRequest) request;
            if (extraExamRequest.hasEnrolment(enrolment) && extraExamRequest.isFor(executionYear)) {
                return true;
            }
        }
        return false;
    }

    private boolean studentHasValidStatutes(final Registration registration, final Enrolment enrolment) {
        final Student student = registration.getStudent();
        for (final StudentStatuteBean bean : student.getStatutes(enrolment.getExecutionPeriod())) {
            if (acceptedStatutes.contains(bean.getStatuteType())) {
                return true;
            }
        }
        for (final StudentStatuteBean bean : student.getStatutes(enrolment.getExecutionPeriod().getPreviousExecutionPeriod())) {
            if (acceptedStatutes.contains(bean.getStatuteType())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnrolment(final Enrolment enrolment) {
        return hasEnrolment() && getEnrolment().equals(enrolment);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
        throw new DomainException("error.ExtraExamRequest.cannot.modify.enrolment");
    }

    public String getEnrolmentName() {
        return hasEnrolment() ? getEnrolment().getName().getContent() : StringUtils.EMPTY;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.EXTRA_EXAM_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    protected void disconnect() {
        super.setEnrolment(null);
        super.disconnect();
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
        }
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }

        if (academicServiceRequestBean.isToCancelOrReject()) {
            setEnrolment(null);
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
    public boolean hasEnrolment() {
        return getEnrolment() != null;
    }

}
