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
package org.fenixedu.academic.ui.struts.action.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@StrutsFunctionality(app = StudentViewApp.class, descriptionKey = "link.student.statistics", path = "statistics",
        titleKey = "link.title.statistics")
@Mapping(module = "student", path = "/showStudentStatistics", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "showStudentStatisticsHome", path = "/student/statistics/home.jsp"),
        @Forward(name = "showExecutionCourseStatistics", path = "/student/statistics/executionCourse.jsp") })
public class ShowStudentStatisticsDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showStudentStatisticsHome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getLoggedPerson(request).getStudent();

        if (student != null) {
            Collection<Enrolment> approvedEnrolments = new HashSet<Enrolment>();
            for (Registration registration : getValidRegistrations(student)) {
                approvedEnrolments.addAll(registration.getApprovedEnrolments());
            }
            request.setAttribute("progress", computeStudentProgress(student));
            request.setAttribute("curricularCoursesOvertime", computeCurricularCoursesOvertimeStatistics(approvedEnrolments));
        }
        return mapping.findForward("showStudentStatisticsHome");
    }

    public ActionForward showExecutionCourseStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getLoggedPerson(request).getStudent();

        ExecutionCourse executionCourse = getDomainObject(request, "executionCourseId");
        if (student != null && executionCourse != null) {
            request.setAttribute("executionCourse", executionCourse);
            request.setAttribute("executionCourseStatistics", computeStudentExecutionCourseStatistics(student, executionCourse));
            request.setAttribute("curricularCourseOvertimeStatistics", computeCurricularCourseOvertimeStatistics(student
                    .getAttends(executionCourse).getEnrolment().getCurricularCourse()));
        }
        return mapping.findForward("showExecutionCourseStatistics");
    }

    private List<Registration> getValidRegistrations(Student student) {
        List<Registration> result = new ArrayList<Registration>();
        for (Registration registration : student.getRegistrationsSet()) {
            if (!registration.isCanceled()) {
                result.add(registration);
            }
        }
        return result;
    }

    private JsonObject computeCurricularCoursesOvertimeStatistics(Collection<Enrolment> approvedEnrolments) {
        JsonObject curricularCoursesOvertime = new JsonObject();
        int lowestStartYear = Integer.MAX_VALUE;
        int highestStartYear = Integer.MIN_VALUE;
        JsonArray curricularCoursesJsonArray = new JsonArray();
        for (Enrolment enrolment : approvedEnrolments) {
            CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            JsonObject curricularCourseJsonObject = new JsonObject();
            curricularCourseJsonObject.addProperty("name", curricularCourse.getNameI18N().getContent());
            JsonArray entriesArray = new JsonArray();
            Map<Integer, CurricularCourseYearStatistics> entries = new HashMap<Integer, CurricularCourseYearStatistics>();
            for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                AcademicInterval academicInterval = executionCourse.getAcademicInterval();
                int startYear = academicInterval.getStart().getYear();
                CurricularCourseYearStatistics curricularCourseYearStatistics =
                        computeExecutionCourseJsonArray(executionCourse, entries.get(startYear));
                if (curricularCourseYearStatistics == null) {
                    continue;
                }
                entries.put(startYear, curricularCourseYearStatistics);
                if (lowestStartYear > startYear) {
                    lowestStartYear = startYear;
                }
                if (highestStartYear < startYear) {
                    highestStartYear = startYear;
                }
            }
            for (int year : entries.keySet()) {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(new JsonPrimitive(year));
                jsonArray.add(new JsonPrimitive(entries.get(year).getApproved()));
                jsonArray.add(new JsonPrimitive(entries.get(year).getFlunked()));
                jsonArray.add(new JsonPrimitive(entries.get(year).getNotEvaluated()));
                entriesArray.add(jsonArray);
            }
            curricularCourseJsonObject.add("years", entriesArray);
            curricularCoursesJsonArray.add(curricularCourseJsonObject);
        }
        curricularCoursesOvertime.addProperty("start-year", lowestStartYear);
        curricularCoursesOvertime.addProperty("end-year", highestStartYear);
        curricularCoursesOvertime.add("entries", curricularCoursesJsonArray);
        return curricularCoursesOvertime;
    }

    public static class CurricularCourseYearStatistics {

        private int approved;
        private int flunked;
        private int notEvaluated;

        public CurricularCourseYearStatistics() {
            this(0, 0, 0);
        }

        public CurricularCourseYearStatistics(int approved, int flunked, int notEvaluated) {
            this.approved = approved;
            this.flunked = flunked;
            this.notEvaluated = notEvaluated;
        }

        public void inc(int approved, int flunked, int notEvaluated) {
            this.approved += approved;
            this.flunked += flunked;
            this.notEvaluated += notEvaluated;
        }

        public int getApproved() {
            return approved;
        }

        public int getFlunked() {
            return flunked;
        }

        public int getNotEvaluated() {
            return notEvaluated;
        }

    }

    private CurricularCourseYearStatistics computeExecutionCourseJsonArray(ExecutionCourse executionCourse,
            CurricularCourseYearStatistics curricularCourseYearStatistics) {
        int approvedStudents = 0;
        int flunkedStudents = 0;
        int notEvaluatedStudents = 0;
        for (Attends attends : executionCourse.getAttendsSet()) {
            if (attends.getEnrolment() != null) {
                Enrolment enrolment = attends.getEnrolment();
                if (enrolment.isApproved()) {
                    approvedStudents++;
                } else if (enrolment.isFlunked()) {
                    flunkedStudents++;
                } else {
                    notEvaluatedStudents++;
                }
            }
        }
        if (approvedStudents + flunkedStudents + notEvaluatedStudents == 0) {
            return null;
        } else {
            if (curricularCourseYearStatistics == null) {
                return new CurricularCourseYearStatistics(approvedStudents, flunkedStudents, notEvaluatedStudents);
            } else {
                curricularCourseYearStatistics.inc(approvedStudents, flunkedStudents, notEvaluatedStudents);
                return curricularCourseYearStatistics;
            }
        }
    }

    private JsonObject computeStudentInfo(Student student) {
        JsonObject studentInfoJsonObject = new JsonObject();
        studentInfoJsonObject.addProperty("id", student.getNumber());
        studentInfoJsonObject.addProperty("name", student.getName());
        return studentInfoJsonObject;
    }

    private JsonElement computeStudentExecutionCourseStatistics(Student student, ExecutionCourse executionCourse) {
        JsonObject executionCourseJsonObject = new JsonObject();
        executionCourseJsonObject.addProperty("acronym", executionCourse.getSigla());
        executionCourseJsonObject.addProperty("name", executionCourse.getNameI18N().getContent());
        executionCourseJsonObject.add("student", computeStudentInfo(student));
        JsonArray evaluationsArray = computeExecutionCourseEvaluations(student, executionCourse);
        evaluationsArray.add(computeFinalGrades(executionCourse));

        executionCourseJsonObject.add("evaluations", evaluationsArray);
        return executionCourseJsonObject;
    }

    private JsonArray computeExecutionCourseEvaluations(Student student, ExecutionCourse executionCourse) {
        JsonArray evaluationsArray = new JsonArray();
        for (WrittenEvaluation writtenEvaluation : executionCourse.getAssociatedWrittenEvaluations()) {
            JsonObject writtenEvaluationJsonObject = computeWrittenEvaluation(writtenEvaluation);
            if (writtenEvaluationJsonObject != null) {
                evaluationsArray.add(writtenEvaluationJsonObject);
            }
        }
        return evaluationsArray;
    }

    private JsonObject computeWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        if (writtenEvaluation.getMarksSet().size() > 0) {
            JsonObject writtenEvaluationJsonObject = new JsonObject();
            writtenEvaluationJsonObject.addProperty("name", writtenEvaluation.getPresentationName());
            writtenEvaluationJsonObject.addProperty("grade-scale", writtenEvaluation.getGradeScale().name());
            populateMinAndMaxGrade(writtenEvaluation.getGradeScale(), writtenEvaluationJsonObject);
            writtenEvaluationJsonObject.add("grades", computeWrittenEvaluationGrades(writtenEvaluation));
            return writtenEvaluationJsonObject;
        } else {
            return null;
        }
    }

    private void populateMinAndMaxGrade(GradeScale scale, JsonObject evaluationJsonObject) {
        String minGrade = "0";
        String minRequiredGrade = "10";
        String maxGrade = "20";
        switch (scale) {
        case TYPE5:
            minGrade = "0";
            minRequiredGrade = "3";
            maxGrade = "5";
            break;
        case TYPEAP:
            minGrade = "RE";
            minRequiredGrade = "AP";
            maxGrade = "AP";
            break;
        default:
            break;
        }
        evaluationJsonObject.addProperty("minRequiredGrade", minRequiredGrade);
        evaluationJsonObject.addProperty("minGrade", minGrade);
        evaluationJsonObject.addProperty("maxGrade", maxGrade);
    }

    private JsonArray computeWrittenEvaluationGrades(WrittenEvaluation writtenEvaluation) {
        JsonArray gradesArray = new JsonArray();
        for (Mark mark : writtenEvaluation.getMarksSet()) {
            gradesArray.add(computeMark(mark));
        }
        return gradesArray;
    }

    private JsonElement computeMark(Mark mark) {
        Student student = mark.getAttend().getRegistration().getStudent();
        JsonObject markJsonObject = new JsonObject();
        markJsonObject.addProperty("id", student.getNumber());
        markJsonObject.addProperty("name", student.getName());
        markJsonObject.addProperty("grade", mark.getMark());
        return markJsonObject;
    }

    private JsonElement computeCurricularCourseOvertimeStatistics(CurricularCourse curricularCourse) {
        JsonObject jsonObject = new JsonObject();
        JsonArray entries = new JsonArray();
        List<ExecutionCourse> sortedExecutionCoursesSet = curricularCourse.getAssociatedExecutionCoursesSet()
                .stream()
                .sorted(Comparator.comparing(ExecutionCourse::getExecutionPeriod).reversed())
                .collect(Collectors.toList());
        for (ExecutionCourse executionCourse : sortedExecutionCoursesSet) {
            if (executionCourse.getExecutionPeriod().isBefore(ExecutionSemester.readActualExecutionSemester())
                    && executionCourse.getEnrolmentCount() > 0) {
                JsonElement executionCourseStatistics = computeExecutionCourseStatistics(executionCourse);
                if (executionCourseStatistics != null) {
                    entries.add(executionCourseStatistics);
                }
            }
        }
        jsonObject.add("entries", entries);
        JsonArray domainJsonArray = new JsonArray();
        domainJsonArray.add(new JsonPrimitive("positive-grades"));
        domainJsonArray.add(new JsonPrimitive("negative-grades"));
        domainJsonArray.add(new JsonPrimitive("not-evaluated-grades"));
        jsonObject.add("domain", domainJsonArray);
        return jsonObject;
    }

    private JsonElement computeExecutionCourseStatistics(ExecutionCourse executionCourse) {
        JsonObject executionCourseJsonObject = new JsonObject();
        executionCourseJsonObject.addProperty("text", getShortExecutionSemesterName(executionCourse.getExecutionPeriod()));
        executionCourseJsonObject.addProperty("description", executionCourse.getNameI18N().getContent());
        int positiveGrades = 0;
        int negativeGrades = 0;
        int notEvaluatedGrades = 0;
        for (Enrolment enrolment : executionCourse.getActiveEnrollments()) {
            if (enrolment.isNotEvaluated()) {
                notEvaluatedGrades++;
            } else if (enrolment.isFlunked()) {
                negativeGrades++;
            } else {
                positiveGrades++;
            }
        }
        if (positiveGrades + negativeGrades + notEvaluatedGrades == 0) {
            return null;
        }
        executionCourseJsonObject.addProperty("positive-grades", positiveGrades);
        executionCourseJsonObject.addProperty("negative-grades", negativeGrades);
        executionCourseJsonObject.addProperty("not-evaluated-grades", notEvaluatedGrades);
        return executionCourseJsonObject;
    }

    //REFACTOR: THIS IS HORRIBLE
    private String getShortExecutionSemesterName(ExecutionSemester executionSemester) {
        return executionSemester.getExecutionYear().getName().substring(2, 4) + "/"
                + executionSemester.getExecutionYear().getName().substring(7, 9) + " (S" + executionSemester.getSemester() + ")";
    }

    private JsonObject computeFinalGrades(ExecutionCourse executionCourse) {
        List<Enrolment> enrolments = executionCourse.getActiveEnrollments();
        if (enrolments.isEmpty()) {
            return null;
        }
        JsonObject enrolmentsJsonObject = new JsonObject();
        enrolmentsJsonObject.addProperty("name", BundleUtil.getString(Bundle.STUDENT, "label.student.statistics.marksheet"));
        GradeScale gradeScale = enrolments.get(0).getGradeScale();
        enrolmentsJsonObject.addProperty("grade-scale", gradeScale.name());
        populateMinAndMaxGrade(gradeScale, enrolmentsJsonObject);
        JsonArray gradesArray = new JsonArray();
        for (Enrolment enrolment : enrolments) {
            JsonObject enrolmentJsonObject = new JsonObject();
            enrolmentJsonObject.addProperty("id", enrolment.getRegistration().getStudent().getNumber());
            enrolmentJsonObject.addProperty("name", enrolment.getRegistration().getStudent().getName());
            enrolmentJsonObject.addProperty("grade",
                    enrolment.isNotEvaluated() ? "NE" : enrolment.isFlunked() ? "RE" : enrolment.getGradeValue());
            gradesArray.add(enrolmentJsonObject);
        }
        enrolmentsJsonObject.add("grades", gradesArray);
        return enrolmentsJsonObject;
    }

    private JsonElement computeStudentProgress(Student student) {
        JsonArray jsonArray = new JsonArray();
        for (Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive() || registration.isConcluded()) {
                jsonArray.add(computeRegistrationProgress(registration));
            }
        }
        return jsonArray;
    }

    private JsonElement computeRegistrationProgress(Registration registration) {
        JsonObject registrationProgressJsonObject = new JsonObject();
        registrationProgressJsonObject.addProperty("year", registration.getStartDate().getYear());
        registrationProgressJsonObject.addProperty("degree", registration.getDegreeCurricularPlanName());
        registrationProgressJsonObject.addProperty("required-credits", registration.getDegree().getEctsCredits());
        registrationProgressJsonObject.addProperty("credits", registration.getEctsCredits());
        if (registration.isConcluded()) {
            registrationProgressJsonObject.addProperty("status", "concluded");
        } else if (registration.isActive()) {
            registrationProgressJsonObject.addProperty("status", "active");
        }
        return registrationProgressJsonObject;
    }
}
