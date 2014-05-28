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
/*
 * Created on 2004/04/15
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class EstablishFinalDegreeWorkStudentGroup {

    @Atomic
    public static Boolean run(Person person, final ExecutionDegree executionDegree) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final Registration registration = getRegistrationForExecutionDegree(person, executionDegree);
        if (registration == null) {
            throw new StudentCannotBeACandidateForSelectedDegree("Student.Cannot.Be.A.Candidate.For.Selected.Degree");
        }
        FinalDegreeWorkGroup group = registration.findFinalDegreeWorkGroupForExecutionYear(executionDegree);
        if (group == null) {
            group = new FinalDegreeWorkGroup();
            GroupStudent groupStudent = new GroupStudent();
            groupStudent.setRegistration(registration);
            groupStudent.setFinalDegreeDegreeWorkGroup(group);
            // } else {
            // if (!group.getGroupProposals().isEmpty()) {
            // throw new GroupProposalCandidaciesExistException();
            // }
            // if (group.getGroupStudents().size() > 1) {
            // throw new GroupStudentCandidaciesExistException();
            // }
        }

        if (group.getExecutionDegree() == null || group.getExecutionDegree() != executionDegree) {
            if (executionDegree != null) {
                group.setExecutionDegree(executionDegree);
            }
        }

        return true;
    }

    private static Registration getRegistrationForExecutionDegree(final Person person, final ExecutionDegree executionDegree) {
        final Student student = person.getStudent();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive()) {
                for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    if (degreeCurricularPlan == studentCurricularPlan.getDegreeCurricularPlan()) {
                        return registration;
                    }
                }
            }
        }
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive()) {
                for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    final CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getSecondCycle();
                    if (cycleCurriculumGroup != null
                            && cycleCurriculumGroup.getDegreeCurricularPlanOfDegreeModule() == degreeCurricularPlan) {
                        return registration;
                    }
                }
            }
        }
        return null;
    }

    public static class StudentCannotBeACandidateForSelectedDegree extends FenixServiceException {

        public StudentCannotBeACandidateForSelectedDegree() {
            super();
        }

        public StudentCannotBeACandidateForSelectedDegree(int errorType) {
            super(errorType);
        }

        public StudentCannotBeACandidateForSelectedDegree(String s) {
            super(s);
        }

        public StudentCannotBeACandidateForSelectedDegree(Throwable cause) {
            super(cause);
        }

        public StudentCannotBeACandidateForSelectedDegree(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public class GroupStudentCandidaciesExistException extends FenixServiceException {

        public GroupStudentCandidaciesExistException() {
            super();
        }

        public GroupStudentCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupStudentCandidaciesExistException(String s) {
            super(s);
        }

        public GroupStudentCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupStudentCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class GroupProposalCandidaciesExistException extends FenixServiceException {

        public GroupProposalCandidaciesExistException() {
            super();
        }

        public GroupProposalCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupProposalCandidaciesExistException(String s) {
            super(s);
        }

        public GroupProposalCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupProposalCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}