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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByTeachersDTO;
import org.fenixedu.academic.dto.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.CurricularSemester;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.personnelSection.contracts.PersonContractSituation;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.TeacherService;

import org.joda.time.Duration;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByTeachers {

    @Atomic
    public static List run(String departmentId, List<String> executionPeriodsIDs) throws FenixServiceException, ParseException {

        final List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
        for (String executionPeriodID : executionPeriodsIDs) {
            executionPeriodList.add(FenixFramework.<ExecutionSemester> getDomainObject(executionPeriodID));
        }

        final ExecutionSemester startPeriod = ExecutionSemester.readStartExecutionSemesterForCredits();

        ExecutionSemester endPeriod = findEndPeriod(executionPeriodList, startPeriod);

        DistributionTeacherServicesByTeachersDTO returnDTO = new DistributionTeacherServicesByTeachersDTO();

        Department department = FenixFramework.getDomainObject(departmentId);

        for (ExecutionSemester executionPeriodEntry : executionPeriodList) {

            List<Teacher> teachers = department.getAllTeachers(executionPeriodEntry);

            for (Teacher teacher : teachers) {
                ProfessionalCategory professionalCategory =
                        ProfessionalCategory.getCategoryByPeriod(teacher, executionPeriodEntry);
                if (professionalCategory == null) {
                    continue;
                }

                TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionPeriodEntry, teacher);
                Double mandatoryLessonHours =
                        teacherCredits != null ? teacherCredits.getMandatoryLessonHours().doubleValue() : TeacherCredits
                                .calculateMandatoryLessonHours(teacher, executionPeriodEntry);

                if (returnDTO.isTeacherPresent(teacher.getExternalId())) {
                    returnDTO.addHoursToTeacher(teacher.getExternalId(), mandatoryLessonHours);
                } else {
                    Double accumulatedCredits =
                            (startPeriod == null ? 0.0 : TeacherCredits.calculateBalanceOfCreditsUntil(teacher, endPeriod));
                    String category = professionalCategory != null ? professionalCategory.getName().getContent() : null;
                    returnDTO.addTeacher(teacher.getExternalId(), teacher.getPerson().getUsername(), category, teacher
                            .getPerson().getName(), mandatoryLessonHours, accumulatedCredits);
                }

                for (Professorship professorShip : teacher.getProfessorships()) {
                    ExecutionCourse executionCourse = professorShip.getExecutionCourse();

                    if (executionCourse.getExecutionPeriod() != executionPeriodEntry) {
                        continue;
                    }

                    Map<String, String> degreeNameMap = new LinkedHashMap<String, String>();
                    Map<String, Set<String>> degreeCurricularYearsMap = new LinkedHashMap<String, Set<String>>();
                    for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                        Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
                        String degreeExternalId = degree.getExternalId();
                        if (!degreeNameMap.containsKey(degreeExternalId)) {
                            degreeNameMap.put(degreeExternalId, degree.getSigla());
                            degreeCurricularYearsMap.put(degreeExternalId, new LinkedHashSet<String>());
                        }

                        Set<String> curricularYears = new LinkedHashSet<String>();

                        for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {

                            if (curricularCourseScope.isActive(executionPeriodEntry.getEndDate())) {
                                CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
                                curricularYears.add(curricularSemester.getCurricularYear().getYear().toString());
                            }
                        }
                        degreeCurricularYearsMap.get(degreeExternalId).addAll(curricularYears);
                    }

                    Double hoursSpentByTeacher =
                            StrictMath.ceil(TeacherService.getHoursLecturedOnExecutionCourse(teacher, executionCourse));

                    Duration timeSpentByTeacher = TeacherService.getLecturedDurationOnExecutionCourse(teacher, executionCourse);

                    returnDTO.addExecutionCourseToTeacher(teacher.getExternalId(), executionCourse.getExternalId(),
                            executionCourse.getNome(), hoursSpentByTeacher.intValue(), timeSpentByTeacher, degreeNameMap,
                            degreeCurricularYearsMap, executionCourse.getExecutionPeriod().getName());

                }

                for (PersonFunction personFunction : PersonFunction.getManagementFunctions(teacher, executionPeriodEntry)) {
                    returnDTO.addManagementFunctionToTeacher(teacher.getExternalId(), personFunction.getFunction().getName(),
                            personFunction.getCredits());
                }

                Double exemptionCredits =
                        teacherCredits != null ? teacherCredits.getServiceExemptionCredits().doubleValue() : TeacherCredits
                                .calculateServiceExemptionCredits(teacher, executionPeriodEntry);

                if (exemptionCredits > 0.0) {
                    Set<PersonContractSituation> serviceExemptions =
                            PersonContractSituation.getValidTeacherServiceExemptions(teacher, executionPeriodEntry);
                    returnDTO.addExemptionSituationToTeacher(teacher.getExternalId(), serviceExemptions, exemptionCredits);
                }
            }
        }

        ArrayList<TeacherDistributionServiceEntryDTO> returnArraylist = new ArrayList<TeacherDistributionServiceEntryDTO>();

        for (TeacherDistributionServiceEntryDTO teacher : returnDTO.getTeachersMap().values()) {
            returnArraylist.add(teacher);
        }

        Collections.sort(returnArraylist);

        return returnArraylist;
    }

    private static ExecutionSemester findEndPeriod(final List<ExecutionSemester> executionPeriodList,
            final ExecutionSemester startPeriod) {
        ExecutionSemester endPeriod = null;

        if (!executionPeriodList.isEmpty() && startPeriod != null) {
            endPeriod = executionPeriodList.iterator().next();

            for (ExecutionSemester executionPeriodEntry : executionPeriodList) {
                if (executionPeriodEntry.compareTo(endPeriod) < 0) {
                    endPeriod = executionPeriodEntry;
                }
            }

            endPeriod = endPeriod.getPreviousExecutionPeriod() == null ? startPeriod : endPeriod.getPreviousExecutionPeriod();
            if (startPeriod.compareTo(endPeriod) > 0) {
                endPeriod = startPeriod;
            }
        }
        return endPeriod;
    }
}