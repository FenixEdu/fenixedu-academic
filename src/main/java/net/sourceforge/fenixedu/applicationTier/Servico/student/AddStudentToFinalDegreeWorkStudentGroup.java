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
 * Created on 2004/04/19
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class AddStudentToFinalDegreeWorkStudentGroup {

    @Atomic
    public static Boolean run(String groupOID, String username) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        FinalDegreeWorkGroup group = FenixFramework.getDomainObject(groupOID);
        Registration registration = findSomeRegistration(username);
        if (group == null
                || registration == null
                || group.getGroupStudents() == null
                || CollectionUtils.find(group.getGroupStudents(), new PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(registration)) != null) {
            return false;
        }
        Scheduleing scheduleing = group.getExecutionDegree().getScheduling();

        if (scheduleing == null || scheduleing.getMaximumNumberOfStudents() == null) {
            throw new MaximumNumberOfStudentsUndefinedException();
        } else if (scheduleing.getMinimumCompletedCreditsFirstCycle() == null) {
            throw new MinimumCompletedCreditsFirstCycleUndefinedException();
        } else if (scheduleing.getMinimumCompletedCreditsSecondCycle() == null) {
            throw new MinimumCompletedCreditsSecondCycleUndefinedException();
            // } else if (scheduleing.getMinimumNumberOfCompletedCourses() ==
            // null) {
            // throw new MinimumNumberOfCompletedCoursesUndefinedException();
        } else if (scheduleing.getMaximumNumberOfStudents().intValue() <= group.getGroupStudents().size()) {
            throw new MaximumNumberOfStudentsReachedException(scheduleing.getMaximumNumberOfStudents().toString());
        } else {
            final Integer maximumCurricularYearToCountCompletedCourses =
                    scheduleing.getMaximumCurricularYearToCountCompletedCourses();
            final Integer minimumCompletedCurricularYear = scheduleing.getMinimumCompletedCurricularYear();
            // final Integer minimumNumberOfCompletedCourses =
            // scheduleing.getMinimumNumberOfCompletedCourses();
            final Integer minimumCompletedCreditsFirstCycle = scheduleing.getMinimumCompletedCreditsFirstCycle();
            final Integer minimumCompletedCreditsSecondCycle = scheduleing.getMinimumCompletedCreditsSecondCycle();

            final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
            final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
            final Collection<CurricularCourseScope> degreesActiveCurricularCourseScopes =
                    degreeCurricularPlan.getActiveCurricularCourseScopes();
            final StringBuilder notCompletedCurricularCourses = new StringBuilder();
            final Set<CurricularCourse> notCompletedCurricularCoursesForMinimumCurricularYear = new HashSet<CurricularCourse>();
            final Set<CurricularCourse> completedCurricularCourses = new HashSet<CurricularCourse>();
            // int numberCompletedCurricularCourses = 0;
            for (final CurricularCourseScope curricularCourseScope : degreesActiveCurricularCourseScopes) {
                final CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
                final boolean isCurricularCourseApproved = studentCurricularPlan.isCurricularCourseApproved(curricularCourse);

                final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
                final CurricularYear curricularYear = curricularSemester.getCurricularYear();

                if (minimumCompletedCurricularYear != null && curricularYear.getYear() <= minimumCompletedCurricularYear) {
                    if (!isCurricularCourseApproved) {
                        notCompletedCurricularCoursesForMinimumCurricularYear.add(curricularCourse);
                    }
                }

                if (maximumCurricularYearToCountCompletedCourses == null
                        || curricularYear.getYear().intValue() <= maximumCurricularYearToCountCompletedCourses.intValue()) {
                    if (isCurricularCourseApproved) {
                        completedCurricularCourses.add(curricularCourseScope.getCurricularCourse());
                        // numberCompletedCurricularCourses++;
                    } else {
                        if (notCompletedCurricularCourses.length() > 0) {
                            notCompletedCurricularCourses.append(", ");
                        }
                        notCompletedCurricularCourses.append(curricularCourseScope.getCurricularCourse().getName());
                    }
                }
            }

            if (!notCompletedCurricularCoursesForMinimumCurricularYear.isEmpty()) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (final CurricularCourse curricularCourse : notCompletedCurricularCoursesForMinimumCurricularYear) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(curricularCourse.getName());
                }
                final String[] args = { minimumCompletedCurricularYear.toString(), stringBuilder.toString() };
                throw new NotCompletedCurricularYearException(null, args);
            }

            // if (minimumNumberOfCompletedCourses != null) {
            // int numberCompletedCurricularCourses =
            // completedCurricularCourses.size();
            // if (numberCompletedCurricularCourses <
            // minimumNumberOfCompletedCourses) {
            // final int numberMissingCurricularCourses =
            // minimumNumberOfCompletedCourses -
            // numberCompletedCurricularCourses;
            // final String[] args = {
            // Integer.toString(numberMissingCurricularCourses),
            // notCompletedCurricularCourses.toString()};
            // throw new
            // MinimumNumberOfCompletedCoursesNotReachedException(null, args);
            // }
            // }

            if (minimumCompletedCreditsFirstCycle != null) {
                final Double completedCredits = studentCurricularPlan.getFirstCycle().getAprovedEctsCredits();
                if (minimumCompletedCreditsFirstCycle > completedCredits) {
                    final String[] args = { completedCredits.toString(), minimumCompletedCreditsFirstCycle.toString() };
                    throw new MinimumCompletedCreditsFirstCycleNotReachedException(null, args);
                }
            }

            if (minimumCompletedCreditsSecondCycle != null) {
                final Double completedCredits = studentCurricularPlan.getSecondCycle().getAprovedEctsCredits();
                if (minimumCompletedCreditsSecondCycle > completedCredits) {
                    final String[] args = { completedCredits.toString(), minimumCompletedCreditsSecondCycle.toString() };
                    throw new MinimumCompletedCreditsSecondCycleNotReachedException(null, args);
                }
            }
        }

        GroupStudent groupStudent = new GroupStudent();
        groupStudent.setRegistration(registration);
        groupStudent.setFinalDegreeDegreeWorkGroup(group);
        return true;
    }

    private static Registration findSomeRegistration(final String username) {
        User user = User.findByUsername(username);
        if (user != null) {
            final Person person = user.getPerson();
            if (person != null) {
                final Student student = person.getStudent();
                final TreeSet<Registration> registrations = new TreeSet<Registration>(new Comparator<Registration>() {

                    @Override
                    public int compare(final Registration r1, final Registration r2) {
                        final DegreeType dt1 = r1.getDegreeType();
                        final DegreeType dt2 = r2.getDegreeType();
                        return 0 - dt1.compareTo(dt2);
                    }

                }) {

                    @Override
                    public boolean add(final Registration r) {
                        final DegreeType degreeType = r.getDegreeType();
                        return isValidDegreeType(degreeType) && super.add(r);
                    }

                    private boolean isValidDegreeType(DegreeType degreeType) {
                        return degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                                || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                                || degreeType == DegreeType.BOLONHA_DEGREE;
                    }

                };
                registrations.addAll(student.getRegistrationsSet());
                return registrations.isEmpty() ? null : registrations.first();
            }
        }
        return null;
    }

    public static class MaximumNumberOfStudentsUndefinedException extends FenixServiceException {
        public MaximumNumberOfStudentsUndefinedException() {
            super();
        }

        public MaximumNumberOfStudentsUndefinedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsUndefinedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsUndefinedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MaximumNumberOfStudentsReachedException extends FenixServiceException {
        public MaximumNumberOfStudentsReachedException() {
            super();
        }

        public MaximumNumberOfStudentsReachedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsReachedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsReachedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MinimumNumberOfCompletedCoursesUndefinedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesUndefinedException() {
            super();
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String s) {
            super(s);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MinimumCompletedCreditsFirstCycleUndefinedException extends FenixServiceException {
        public MinimumCompletedCreditsFirstCycleUndefinedException() {
            super();
        }

        public MinimumCompletedCreditsFirstCycleUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumCompletedCreditsFirstCycleUndefinedException(String s) {
            super(s);
        }

        public MinimumCompletedCreditsFirstCycleUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumCompletedCreditsFirstCycleUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MinimumCompletedCreditsSecondCycleUndefinedException extends FenixServiceException {
        public MinimumCompletedCreditsSecondCycleUndefinedException() {
            super();
        }

        public MinimumCompletedCreditsSecondCycleUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumCompletedCreditsSecondCycleUndefinedException(String s) {
            super(s);
        }

        public MinimumCompletedCreditsSecondCycleUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumCompletedCreditsSecondCycleUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class NotCompletedCurricularYearException extends FenixServiceException {
        public NotCompletedCurricularYearException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class MinimumNumberOfCompletedCoursesNotReachedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesNotReachedException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class MinimumCompletedCreditsFirstCycleNotReachedException extends FenixServiceException {
        public MinimumCompletedCreditsFirstCycleNotReachedException(String s, String[] args) {
            super(s, args);
        }
    }

    public static class MinimumCompletedCreditsSecondCycleNotReachedException extends FenixServiceException {
        public MinimumCompletedCreditsSecondCycleNotReachedException(String s, String[] args) {
            super(s, args);
        }
    }

    private static class PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT implements Predicate {
        Registration registration = null;

        @Override
        public boolean evaluate(Object arg0) {
            GroupStudent groupStudent = (GroupStudent) arg0;
            return registration.getExternalId().equals(groupStudent.getRegistration().getExternalId());
        }

        public PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(Registration registration) {
            super();
            this.registration = registration;
        }
    }

}