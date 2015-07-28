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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.ShiftToEnrol;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class ReadShiftsToEnroll {

    protected List run(Registration registration, ExecutionSemester executionSemester) throws FenixServiceException {

        checkStudentRestrictionsForShiftsEnrolments(registration);

        final List<ShiftToEnrol> result = new ArrayList<ShiftToEnrol>();
        for (final Attends attends : registration.readAttendsByExecutionPeriod(executionSemester)) {
            result.add(buildShiftToEnrol(attends));
        }
        return result;
    }

    private void checkStudentRestrictionsForShiftsEnrolments(Registration registration) throws FenixServiceException {
        if (registration == null) {
            throw new FenixServiceException("errors.impossible.operation");
        }

        if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
            if (!registration.getInterruptedStudies()) {
                throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
            }
        }

        if (registration.getFlunked()) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }
    }

    private ShiftToEnrol buildShiftToEnrol(Attends attends) {

        final ShiftToEnrol result = new ShiftToEnrol();

        findShiftTypesFromExecutionCourse(attends, result);
        findShiftsForExecutionCourseShiftTypesFromStudentEnroledShifts(attends, result);

        result.setExecutionCourse(attends.getExecutionCourse());
        result.setEnrolled(attends.getEnrolment() != null);

        return result;
    }

    private void findShiftsForExecutionCourseShiftTypesFromStudentEnroledShifts(Attends attend, ShiftToEnrol result) {
        for (final Shift shift : attend.getRegistration().getShiftsSet()) {
            setShiftInformation(attend, result, shift);
        }
    }

    private void findShiftTypesFromExecutionCourse(Attends attend, ShiftToEnrol result) {
        for (final Shift shift : attend.getExecutionCourse().getAssociatedShifts()) {
            setShiftTypeInformation(result, shift);
        }
    }

    private void setShiftTypeInformation(ShiftToEnrol result, final Shift shift) {

        if (shift.containsType(ShiftType.TEORICA)) {
            result.setTheoricType(ShiftType.TEORICA);

        } else if (shift.containsType(ShiftType.PRATICA)) {
            result.setPraticType(ShiftType.PRATICA);

        } else if (shift.containsType(ShiftType.LABORATORIAL)) {
            result.setLaboratoryType(ShiftType.LABORATORIAL);

        } else if (shift.containsType(ShiftType.TEORICO_PRATICA)) {
            result.setTheoricoPraticType(ShiftType.TEORICO_PRATICA);

        } else if (shift.containsType(ShiftType.FIELD_WORK)) {
            result.setFieldWorkType(ShiftType.FIELD_WORK);

        } else if (shift.containsType(ShiftType.PROBLEMS)) {
            result.setProblemsType(ShiftType.PROBLEMS);

        } else if (shift.containsType(ShiftType.SEMINARY)) {
            result.setSeminaryType(ShiftType.SEMINARY);

        } else if (shift.containsType(ShiftType.TRAINING_PERIOD)) {
            result.setTrainingType(ShiftType.TRAINING_PERIOD);

        } else if (shift.containsType(ShiftType.TUTORIAL_ORIENTATION)) {
            result.setTutorialOrientationType(ShiftType.TUTORIAL_ORIENTATION);
        }
    }

    private void setShiftInformation(Attends attend, ShiftToEnrol result, final Shift shift) {

        if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.TEORICA)) {
            result.setTheoricShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.PRATICA)) {
            result.setPraticShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.LABORATORIAL)) {
            result.setLaboratoryShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.TEORICO_PRATICA)) {
            result.setTheoricoPraticShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.FIELD_WORK)) {
            result.setFieldWorkShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.PROBLEMS)) {
            result.setProblemsShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.SEMINARY)) {
            result.setSeminaryShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse() && shift.containsType(ShiftType.TRAINING_PERIOD)) {
            result.setTrainingShift(shift);

        } else if (shift.getExecutionCourse() == attend.getExecutionCourse()
                && shift.containsType(ShiftType.TUTORIAL_ORIENTATION)) {
            result.setTutorialOrientationShift(shift);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ReadShiftsToEnroll serviceInstance = new ReadShiftsToEnroll();

    @Atomic
    public static List runReadShiftsToEnroll(Registration registration) throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        return serviceInstance.run(registration, ExecutionSemester.readActualExecutionSemester());
    }

    @Atomic
    public static List runReadShiftsToEnroll(Registration registration, ExecutionSemester executionSemester)
            throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration, executionSemester);
        return serviceInstance.run(registration, executionSemester);
    }

}