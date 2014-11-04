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

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;

public class EquivalencePlanRequest extends EquivalencePlanRequest_Base {

    private static final String COURSE_LABEL = "Nome da disciplina do currículo de Bolonha";
    private static final String COURSE_ECTS = "ECTS";
    private static final String EQUIVALENT_COURSE_LABEL = "Nome da(s) disciplina(s) considerada(s) equivalente(s)";
    private static final String GRADE_LABEL = "Classificação";
    private static final String GRADE_SCALE = "Escala";
    private static final String MEC2006 = "MEC 2006"; // remove after when the

    // process is open to all
    // degrees

    protected EquivalencePlanRequest() {
        super();
    }

    public EquivalencePlanRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        setNumberOfEquivalences(bean.getNumberOfEquivalences());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (bean.getExecutionYear() == null) {
            throw new DomainException("error.EquivalencePlanRequest.executionYear.cannot.be.null");
        }
    }

    @Override
    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.EQUIVALENCE_PLAN;
    }

    @Override
    public EventType getEventType() {
        return EventType.EQUIVALENCE_PLAN_REQUEST;
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isNew()) {
            if (!isFree()) {
                new EquivalencePlanRequestEvent(getAdministrativeOffice(), getPerson(), this);
            }
        }
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToCancelOrReject() && getEvent() != null) {
            getEvent().cancel(academicServiceRequestBean.getResponsible());

        } else if (academicServiceRequestBean.isToProcess()) {
            if (isPayable() && !isPayed()) {
                throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
            }
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }
    }

    @Override
    protected void checkRulesToDelete() {
        if (!getEquivalencePlanRevisionRequestsSet().isEmpty()) {
            throw new DomainException("error.AcademicServiceRequest.cannot.be.deleted");
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
        return true;
    }

    @Override
    public boolean isPayedUponCreation() {
        return true;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

}
