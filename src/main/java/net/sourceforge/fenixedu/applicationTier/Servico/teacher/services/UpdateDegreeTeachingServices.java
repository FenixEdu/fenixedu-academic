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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UpdateDegreeTeachingServices {

    protected void run(String professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType) {

        Professorship professorship = FenixFramework.getDomainObject(professorshipID);
        Teacher teacher = professorship.getTeacher();
        ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }

        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.schedule.change"));

        for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
            Shift shift = FenixFramework.getDomainObject(shiftIDTeachingPercentage.getShiftID());
            DegreeTeachingService degreeTeachingService =
                    teacherService.getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage(), roleType);
            } else {
                if (shiftIDTeachingPercentage.getPercentage() == null
                        || (shiftIDTeachingPercentage.getPercentage() != null && shiftIDTeachingPercentage.getPercentage() != 0)) {
                    new DegreeTeachingService(teacherService, professorship, shift, shiftIDTeachingPercentage.getPercentage(),
                            roleType);
                }
            }

            log.append(shift.getPresentationName());
            if (shiftIDTeachingPercentage.getPercentage() != null) {
                log.append("= ");
                log.append(shiftIDTeachingPercentage.getPercentage());
            }
            log.append("% ; ");
        }

        new TeacherServiceLog(teacherService, log.toString());
    }

    // Service Invokers migrated from Berserk

    private static final UpdateDegreeTeachingServices serviceInstance = new UpdateDegreeTeachingServices();

    @Atomic
    public static void runUpdateDegreeTeachingServices(String professorshipID,
            List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages, RoleType roleType) throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(professorshipID, shiftsIDsTeachingPercentages, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}