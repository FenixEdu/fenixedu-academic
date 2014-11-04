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
package org.fenixedu.academic.service.services.teacher.teacherService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.service.filter.DepartmentAdministrativeOfficeAuthorizationFilter;
import org.fenixedu.academic.service.filter.DepartmentMemberAuthorizationFilter;
import org.fenixedu.academic.service.filter.TeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByCourseDTO;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.teacher.TeacherService;

import org.joda.time.Duration;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByCourse {

    protected List run(String departmentId, List<String> executionPeriodsIDs) throws FenixServiceException {

        Department department = FenixFramework.getDomainObject(departmentId);

        // List<CompetenceCourse> competenceCourseList =
        // department.getCompetenceCourses();
        List<CompetenceCourse> competenceCourseList = department.getDepartmentUnit().getCompetenceCourses();

        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
        for (String executionPeriodID : executionPeriodsIDs) {
            executionPeriodList.add(FenixFramework.<ExecutionSemester> getDomainObject(executionPeriodID));
        }

        DistributionTeacherServicesByCourseDTO returnDTO = new DistributionTeacherServicesByCourseDTO();

        Map<String, Boolean> executionCoursesMap = new HashMap<String, Boolean>();

        for (CompetenceCourse cc : competenceCourseList) {
            for (CurricularCourse curricularCourseEntry : cc.getAssociatedCurricularCoursesSet()) {

                for (ExecutionSemester executionPeriodEntry : executionPeriodList) {

                    Set<String> curricularYearsSet = buildCurricularYearsSet(curricularCourseEntry, executionPeriodEntry);

                    for (ExecutionCourse executionCourseEntry : curricularCourseEntry
                            .getExecutionCoursesByExecutionPeriod(executionPeriodEntry)) {

                        if (executionCoursesMap.containsKey(executionCourseEntry.getExternalId())) {
                            returnDTO.addDegreeNameToExecutionCourse(executionCourseEntry.getExternalId(), curricularCourseEntry
                                    .getDegreeCurricularPlan().getDegree().getSigla());
                            returnDTO.addCurricularYearsToExecutionCourse(executionCourseEntry.getExternalId(),
                                    curricularYearsSet);
                            continue;
                        }

                        // performance enhancement
                        int executionCourseFirstTimeEnrollementStudentNumber =
                                executionCourseEntry.getFirstTimeEnrolmentStudentNumber();
                        int totalStudentsNumber = executionCourseEntry.getTotalEnrolmentStudentNumber();
                        int executionCourseSecondTimeEnrollementStudentNumber =
                                totalStudentsNumber - executionCourseFirstTimeEnrollementStudentNumber;

                        int theoreticalShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TEORICA);
                        int praticalShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.PRATICA);
                        int theoPratShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TEORICO_PRATICA);
                        int laboratorialShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.LABORATORIAL);
                        int seminaryShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.SEMINARY);
                        int problemsShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.PROBLEMS);
                        int tutorialOrientationShiftsNumber =
                                executionCourseEntry.getNumberOfShifts(ShiftType.TUTORIAL_ORIENTATION);

                        int fieldWorkShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.FIELD_WORK);
                        int trainingPeriodShiftsNumber = executionCourseEntry.getNumberOfShifts(ShiftType.TRAINING_PERIOD);

                        double theoreticalStudentsNumberPerShift =
                                theoreticalShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / theoreticalShiftsNumber;
                        double praticalStudentsNumberPerShift =
                                praticalShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / praticalShiftsNumber;
                        double theoPratStudentsNumberPerShift =
                                theoPratShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / theoPratShiftsNumber;
                        double laboratorialStudentsNumberPerShift =
                                laboratorialShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / laboratorialShiftsNumber;

                        double seminaryStudentsNumberPerShift =
                                seminaryShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / seminaryShiftsNumber;
                        double problemsStudentsNumberPerShift =
                                problemsShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / problemsShiftsNumber;
                        double tutorialOrientationStudentsNumberPerShift =
                                tutorialOrientationShiftsNumber == 0 ? 0 : (double) totalStudentsNumber
                                        / tutorialOrientationShiftsNumber;
                        double fieldWorkStudentsNumberPerShift =
                                fieldWorkShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / fieldWorkShiftsNumber;
                        double trainingPeriodStudentsNumberPerShift =
                                trainingPeriodShiftsNumber == 0 ? 0 : (double) totalStudentsNumber / trainingPeriodShiftsNumber;

                        String campus =
                                getCampusForCurricularCourseAndExecutionPeriod(curricularCourseEntry, executionPeriodEntry);

                        returnDTO.addExecutionCourse(executionCourseEntry.getExternalId(), executionCourseEntry.getNome(),
                                campus, curricularCourseEntry.getDegreeCurricularPlan().getDegree().getSigla(),
                                curricularYearsSet, executionCourseEntry.getExecutionPeriod().getSemester(),
                                executionCourseFirstTimeEnrollementStudentNumber,
                                executionCourseSecondTimeEnrollementStudentNumber, executionCourseEntry.getTotalShiftsDuration(),
                                executionCourseEntry.getAllShiftUnitHours(ShiftType.TEORICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.PRATICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.LABORATORIAL).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TEORICO_PRATICA).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.SEMINARY).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.PROBLEMS).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TUTORIAL_ORIENTATION).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.FIELD_WORK).doubleValue(), executionCourseEntry
                                        .getAllShiftUnitHours(ShiftType.TRAINING_PERIOD).doubleValue(),
                                theoreticalStudentsNumberPerShift, praticalStudentsNumberPerShift,
                                laboratorialStudentsNumberPerShift, theoPratStudentsNumberPerShift,
                                seminaryStudentsNumberPerShift, problemsStudentsNumberPerShift,
                                tutorialOrientationStudentsNumberPerShift, fieldWorkStudentsNumberPerShift,
                                trainingPeriodStudentsNumberPerShift);

                        fillExecutionCourseDTOWithTeachers(returnDTO, executionCourseEntry, department);

                        executionCoursesMap.put(executionCourseEntry.getExternalId(), true);

                    }
                }
            }
        }

        ArrayList<ExecutionCourseDistributionServiceEntryDTO> returnArraylist =
                new ArrayList<ExecutionCourseDistributionServiceEntryDTO>();

        for (ExecutionCourseDistributionServiceEntryDTO teacherDTO : returnDTO.getExecutionCourseMap().values()) {
            returnArraylist.add(teacherDTO);
        }

        Collections.sort(returnArraylist);

        return returnArraylist;
    }

    private Set<String> buildCurricularYearsSet(CurricularCourse curricularCourseEntry, ExecutionSemester executionPeriodEntry) {

        List<CurricularCourseScope> scopesList = curricularCourseEntry.getActiveScopesInExecutionPeriod(executionPeriodEntry);

        if (scopesList.isEmpty()) {
            scopesList = curricularCourseEntry.getActiveScopesIntersectedByExecutionPeriod(executionPeriodEntry);
        }

        Set<String> curricularYearsSet = new LinkedHashSet<String>();
        for (CurricularCourseScope scopeEntry : scopesList) {
            CurricularYear curricularYear =
                    curricularCourseEntry.getCurricularYearByBranchAndSemester(scopeEntry.getBranch(), scopeEntry
                            .getCurricularSemester().getSemester());
            if (curricularYear != null) {
                curricularYearsSet.add(curricularYear.getYear().toString());
            }
        }
        return curricularYearsSet;
    }

    private void fillExecutionCourseDTOWithTeachers(DistributionTeacherServicesByCourseDTO dto, ExecutionCourse executionCourse,
            Department department) {

        for (Professorship professorShipEntry : executionCourse.getProfessorshipsSet()) {
            Teacher teacher = professorShipEntry.getTeacher();

            if (teacher == null) {
                continue;
            }

            String teacherExternalId = teacher.getExternalId();
            String teacherUsername = teacher.getPerson().getUsername();
            String teacherName = teacher.getPerson().getName();

            DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(decimalFormatSymbols);
            Double teacherRequiredHours =
                    new Double(df.format(TeacherService.getHoursLecturedOnExecutionCourse(teacher, executionCourse)));

            Duration teacherLecturedTime = TeacherService.getLecturedDurationOnExecutionCourse(teacher, executionCourse);

            boolean teacherBelongsToDepartment = false;

            if (teacher.getDepartment() == department) {
                teacherBelongsToDepartment = true;
            }

            dto.addTeacherToExecutionCourse(executionCourse.getExternalId(), teacherExternalId, teacherUsername, teacherName,
                    teacherRequiredHours, teacherLecturedTime, teacherBelongsToDepartment);
        }

    }

    private String getCampusForCurricularCourseAndExecutionPeriod(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester) {
        String campus = "";

        for (ExecutionDegree executionDegreeEntry : curricularCourse.getDegreeCurricularPlan().getExecutionDegreesSet()) {
            if (executionDegreeEntry.getExecutionYear() == executionSemester.getExecutionYear()) {
                campus = executionDegreeEntry.getCampus().getName();
                break;
            }
        }

        return campus;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherServiceDistributionByCourse serviceInstance = new ReadTeacherServiceDistributionByCourse();

    @Atomic
    public static List runReadTeacherServiceDistributionByCourse(String departmentId, List<String> executionPeriodsIDs)
            throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
            return serviceInstance.run(departmentId, executionPeriodsIDs);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(departmentId, executionPeriodsIDs);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentMemberAuthorizationFilter.instance.execute();
                    return serviceInstance.run(departmentId, executionPeriodsIDs);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}