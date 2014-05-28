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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.TutorshipAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertTutorship extends TutorshipManagement {

    protected void run(String executionDegreeID, TutorshipManagementBean bean) throws FenixServiceException {

        final Integer studentNumber = bean.getStudentNumber();
        final Teacher teacher = bean.getTeacher();
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        validateTeacher(teacher, executionDegree);

        Registration registration =
                Registration.readRegisteredRegistrationByNumberAndDegreeType(studentNumber, executionDegree
                        .getDegreeCurricularPlan().getDegreeType());

        validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

        validateTutorship(registration);

        Tutorship.createTutorship(teacher, registration.getActiveStudentCurricularPlan(), bean.getTutorshipEndMonth()
                .getNumberOfMonth(), bean.getTutorshipEndYear());
    }

    public List<TutorshipErrorBean> run(String executionDegreeID, StudentsByEntryYearBean bean) throws FenixServiceException {

        final List<StudentCurricularPlan> students = bean.getStudentsToCreateTutorshipList();
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final Teacher teacher = bean.getTeacher();

        validateTeacher(teacher, executionDegree);

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        for (StudentCurricularPlan studentCurricularPlan : students) {
            Registration registration = studentCurricularPlan.getRegistration();
            Integer studentNumber = registration.getNumber();

            if (!registration.isActive() || !studentCurricularPlan.isLastStudentCurricularPlanFromRegistration()) {
                studentsWithErrors.add(new TutorshipErrorBean("error.tutor.notActiveRegistration", new String[] { Integer
                        .toString(studentNumber) }));
                continue;
            }

            try {
                validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                validateTutorship(registration);

                Tutorship.createTutorship(teacher, studentCurricularPlan, bean.getTutorshipEndMonth().getNumberOfMonth(),
                        bean.getTutorshipEndYear());

            } catch (FenixServiceException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            } catch (DomainException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final InsertTutorship serviceInstance = new InsertTutorship();

    @Atomic
    public static List<TutorshipErrorBean> runInsertTutorship(String executionDegreeID, StudentsByEntryYearBean bean)
            throws FenixServiceException, NotAuthorizedException {
        TutorshipAuthorizationFilter.instance.execute();
        return serviceInstance.run(executionDegreeID, bean);
    }

}
