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
package org.fenixedu.academic.service.factoryExecutors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.CourseGroupChangeRequest;
import org.fenixedu.academic.domain.serviceRequests.DuplicateRequest;
import org.fenixedu.academic.domain.serviceRequests.EquivalencePlanRequest;
import org.fenixedu.academic.domain.serviceRequests.EquivalencePlanRevisionRequest;
import org.fenixedu.academic.domain.serviceRequests.ExtraExamRequest;
import org.fenixedu.academic.domain.serviceRequests.ExtraordinarySeasonRequest;
import org.fenixedu.academic.domain.serviceRequests.FreeSolicitationAcademicRequest;
import org.fenixedu.academic.domain.serviceRequests.PartialRegistrationRegimeRequest;
import org.fenixedu.academic.domain.serviceRequests.SpecialSeasonRequest;
import org.fenixedu.academic.domain.serviceRequests.StudentReingressionRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.PhotocopyRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;

import pt.ist.fenixframework.Atomic;

public class RegistrationAcademicServiceRequestCreator extends RegistrationAcademicServiceRequestCreateBean implements
        FactoryExecutor {

    public RegistrationAcademicServiceRequestCreator(final Registration registration) {
        super(registration);
    }

    @Override
    @Atomic
    public Object execute() {
        final Object result;
        switch (getAcademicServiceRequestType()) {
        case REINGRESSION:
            result = new StudentReingressionRequest(this);
            break;

        case EQUIVALENCE_PLAN:
            result = new EquivalencePlanRequest(this);
            break;

        case REVISION_EQUIVALENCE_PLAN:
            result = new EquivalencePlanRevisionRequest(this);
            break;

        case COURSE_GROUP_CHANGE_REQUEST:
            this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
            result = new CourseGroupChangeRequest(this);
            break;

        case EXTRA_EXAM_REQUEST:
            this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
            result = new ExtraExamRequest(this);
            break;

        case FREE_SOLICITATION_ACADEMIC_REQUEST:
            result = new FreeSolicitationAcademicRequest(this);
            break;

        case SPECIAL_SEASON_REQUEST:
            result = new SpecialSeasonRequest(this);
            break;

        case EXTRAORDINARY_SEASON_REQUEST:
            result = new ExtraordinarySeasonRequest(this);
            break;

        case PHOTOCOPY_REQUEST:
            this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
            result = new PhotocopyRequest(this);
            break;

        case PARTIAL_REGIME_REQUEST:
            result = new PartialRegistrationRegimeRequest(this);
            break;

        case DUPLICATE_REQUEST:
            result = new DuplicateRequest(this);
            break;

        default:
            throw new DomainException("error.RegistrationAcademicServiceRequestCreator.no.executor");
        }

        return result;
    }

}
