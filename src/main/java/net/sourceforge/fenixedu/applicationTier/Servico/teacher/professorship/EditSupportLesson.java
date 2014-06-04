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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditSupportLesson {

    protected void run(SupportLessonDTO supportLessonDTO, RoleType roleType) {

        Professorship professorship = FenixFramework.getDomainObject(supportLessonDTO.getProfessorshipID());
        ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
        Teacher teacher = professorship.getTeacher();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);

        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }

        final StringBuilder log = new StringBuilder();

        SupportLesson supportLesson = FenixFramework.getDomainObject(supportLessonDTO.getExternalId());
        if (supportLesson == null) {
            supportLesson = new SupportLesson(supportLessonDTO, professorship, roleType);
            log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.teacher.schedule.supportLessons.create"));
        } else {
            supportLesson.update(supportLessonDTO, roleType);
            log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.teacher.schedule.supportLessons.change"));
        }

        log.append(WeekDay.getWeekDay(supportLessonDTO.getWeekDay()).getLabel());
        log.append(" ");
        log.append(supportLessonDTO.getStartTime().getHours());
        log.append(":");
        log.append(supportLessonDTO.getStartTime().getMinutes());
        log.append(" - ");
        log.append(supportLessonDTO.getEndTime().getHours());
        log.append(":");
        log.append(supportLessonDTO.getEndTime().getMinutes());
        log.append(" - ");
        log.append(supportLessonDTO.getPlace());

        new TeacherServiceLog(teacherService, log.toString());
    }

    // Service Invokers migrated from Berserk

    private static final EditSupportLesson serviceInstance = new EditSupportLesson();

    @Atomic
    public static void runEditSupportLesson(SupportLessonDTO supportLessonDTO, RoleType roleType) throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(supportLessonDTO, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(supportLessonDTO, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(supportLessonDTO, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}