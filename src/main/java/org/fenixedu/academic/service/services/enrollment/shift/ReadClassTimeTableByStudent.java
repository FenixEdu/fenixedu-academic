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
package org.fenixedu.academic.service.services.enrollment.shift;

//TODO: DELETE
@Deprecated
public class ReadClassTimeTableByStudent {

//    public List<InfoShowOccupation> run(final Registration registration, final SchoolClass schoolClass,
//            final ExecutionCourse executionCourse) throws FenixServiceException {
//
//        final List<InfoShowOccupation> result = getOccupations(registration, schoolClass, executionCourse, null);
//        return result;
//    }
//
//    public List<InfoShowOccupation> run(final Registration registration, final SchoolClass schoolClass,
//            final ExecutionCourse executionCourse, ExecutionInterval executionInterval) throws FenixServiceException {
//
//        final List<InfoShowOccupation> result = getOccupations(registration, schoolClass, executionCourse, executionInterval);
//        return result;
//    }
//
//    private List<InfoShowOccupation> getOccupations(final Registration registration, final SchoolClass schoolClass,
//            final ExecutionCourse executionCourse, ExecutionInterval executionInterval) throws FenixServiceException {
//        if (registration == null) {
//            throw new FenixServiceException("error.readClassTimeTableByStudent.noStudent");
//        }
//
//        if (schoolClass == null) {
//            throw new FenixServiceException("error.readClassTimeTableByStudent.noSchoolClass");
//        }
//
//        Set<ExecutionCourse> attendingExecutionCourses = null;
//        if (executionInterval != null) {
//            attendingExecutionCourses = new HashSet<ExecutionCourse>();
//            attendingExecutionCourses.addAll(registration.getAttendingExecutionCoursesFor(executionInterval));
//        } else {
//            attendingExecutionCourses = registration.getAttendingExecutionCoursesForCurrentExecutionPeriod();
//        }
//
//        final List<InfoShowOccupation> result = new ArrayList<InfoShowOccupation>();
//        for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
//            if ((executionCourse == null || executionCourse == shift.getDisciplinaExecucao())
//                    && attendingExecutionCourses.contains(shift.getDisciplinaExecucao())) {
//                result.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
//            }
//        }
//        return result;
//    }
//
//    // Service Invokers migrated from Berserk
//
//    private static final ReadClassTimeTableByStudent serviceInstance = new ReadClassTimeTableByStudent();
//
//    @Atomic
//    public static List<InfoShowOccupation> runReadClassTimeTableByStudent(Registration registration, SchoolClass schoolClass,
//            ExecutionCourse executionCourse) throws FenixServiceException, NotAuthorizedException {
//        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
//        return serviceInstance.run(registration, schoolClass, executionCourse);
//    }
//
//    @Atomic
//    public static List<InfoShowOccupation> runReadClassTimeTableByStudent(Registration registration, SchoolClass schoolClass,
//            ExecutionCourse executionCourse, ExecutionInterval executionInterval) throws FenixServiceException,
//            NotAuthorizedException {
//        ClassEnrollmentAuthorizationFilter.instance.execute(registration, executionInterval);
//        return serviceInstance.run(registration, schoolClass, executionCourse, executionInterval);
//    }

}