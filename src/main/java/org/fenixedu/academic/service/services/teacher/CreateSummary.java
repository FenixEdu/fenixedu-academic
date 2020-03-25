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
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.filter.SummaryManagementToTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;

import pt.ist.fenixframework.Atomic;

public class CreateSummary {

    protected void run(SummariesManagementBean bean) {

        ServiceMonitoring.logService(this.getClass(), bean);

        if (bean.isNewSummary()) {
            Summary summary =
                    new Summary(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(), bean.getSummaryType().equals(
                            SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean.getProfessorship(), bean.getTeacherName(),
                            bean.getTeacher(), bean.getShift(), bean.getLesson(), bean.getSummaryDate(), bean.getSummaryRoom(),
                            bean.getSummaryTime(), bean.getLessonType(), bean.getTaught(), bean.getOnlineLesson());
            Signal.emit(Summary.CREATE_SIGNAL, new DomainObjectEvent<Summary>(summary));
        } else {
            bean.getSummary().edit(bean.getTitle(), bean.getSummaryText(), bean.getStudentsNumber(),
                    bean.getSummaryType().equals(SummariesManagementBean.SummaryType.EXTRA_SUMMARY), bean.getProfessorship(),
                    bean.getTeacherName(), bean.getTeacher(), bean.getShift(), bean.getLesson(), bean.getSummaryDate(),
                    bean.getSummaryRoom(), bean.getSummaryTime(), bean.getLessonType(), bean.getTaught(), bean.getOnlineLesson());
            Signal.emit(Summary.EDIT_SIGNAL, new DomainObjectEvent<Summary>(bean.getSummary()));
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateSummary serviceInstance = new CreateSummary();

    public static void runCreateSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        try {
            atomicCreateSummary(bean);
        } catch (RuntimeException re) {
            if (re.getCause() != null && re.getCause() instanceof DomainException) {
                throw (DomainException) re.getCause();
            }
            throw re;
        }
    }

    public static void runEditSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        try {
            atomicEditSummary(bean);
        } catch (RuntimeException re) {
            if (re.getCause() != null && re.getCause() instanceof DomainException) {
                throw (DomainException) re.getCause();
            }
            throw re;
        }
    }

    @Atomic
    private static void atomicCreateSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(bean);
        serviceInstance.run(bean);
    }

    // Service Invokers migrated from Berserk

    @Atomic
    private static void atomicEditSummary(SummariesManagementBean bean) throws NotAuthorizedException {
        SummaryManagementToTeacherAuthorizationFilter.instance.execute(bean.getSummary(), bean.getProfessorshipLogged());
        serviceInstance.run(bean);
    }

}