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
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

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
        if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
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
        if (hasAnyEquivalencePlanRevisionRequests()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRevisionRequest> getEquivalencePlanRevisionRequests() {
        return getEquivalencePlanRevisionRequestsSet();
    }

    @Deprecated
    public boolean hasAnyEquivalencePlanRevisionRequests() {
        return !getEquivalencePlanRevisionRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasNumberOfEquivalences() {
        return getNumberOfEquivalences() != null;
    }

}
