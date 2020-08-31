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
package org.fenixedu.academic.ui.struts.action.coordinator.xviews;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/analytics", module = "coordinator", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "showHome", path = "/coordinator/analytics/home.jsp") })
public class ExecutionYearViewDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    private JsonArray computeExecutionYearsForDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        JsonArray executionYears = new JsonArray();

        degreeCurricularPlan.getExecutionDegreesSet().stream().map(ed -> ed.getExecutionYear()).sorted(Comparator.reverseOrder())
                .limit(4).forEach(ey -> executionYears.add(executionYearToJson(ey)));

        return executionYears;
    }

    private JsonObject executionYearToJson(ExecutionYear year) {
        JsonObject executionYearJson = new JsonObject();
        executionYearJson.addProperty("id", year.getExternalId());
        executionYearJson.addProperty("name", year.getQualifiedName());
        return executionYearJson;
    }

    public ActionForward showHome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        User userView = Authenticate.getUser();

        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        JsonArray executionYears = computeExecutionYearsForDegreeCurricularPlan(degreeCurricularPlan);
        request.setAttribute("executionYears", executionYears);

        String executionYearId = request.getParameter("executionYear");

        ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(degreeCurricularPlan.getDegree().getCalendar());

        if (executionYearId != null) {
            currentExecutionYear = FenixFramework.getDomainObject(executionYearId);
        }

        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        request.setAttribute("currentExecutionYear", computeExecutionYearStatistics(degreeCurricularPlan, currentExecutionYear));

        return mapping.findForward("showHome");

    }

    private Set<Enrolment> getDegreeCurricularPlanEnrolmentsForExecutionYear(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionYear executionYear) {
        Set<Enrolment> enrolments = new HashSet<Enrolment>();
        for (StudentCurricularPlan scp : degreeCurricularPlan.getStudentCurricularPlansSet()) {
            for (Enrolment enrol : scp.getEnrolmentsSet()) {
                if (enrol.getExecutionInterval().getExecutionYear() == executionYear && !enrol.isAnnulled()
                        && enrol.getParentCycleCurriculumGroup() != null && degreeCurricularPlan
                                .getCycleCourseGroup(enrol.getParentCycleCurriculumGroup().getCycleType()) != null) {
                    enrolments.add(enrol);
                }
            }
        }
        return enrolments;
    }

    @Deprecated
    private static final String NA = "NA";

    private static class CurricularCourseGradeEntry {

        BigDecimal sum = BigDecimal.ZERO;

        Integer notEvaluated = 0;
        Integer approved = 0;
        Integer flunked = 0;
        Integer attending = 0;

        List<Enrolment> enrolmentList = new ArrayList<Enrolment>();

        Integer quantity = 0;

        public CurricularCourseGradeEntry(Enrolment enrolment) {
            plus(enrolment);
        }

        public CurricularCourseGradeEntry plus(Enrolment enrolment) {
            enrolmentList.add(enrolment);
            Grade grade = enrolment.getGrade();
            if (grade == null || grade.isEmpty()) {
                attending++;
            } else if (grade.isApproved()) {
                approved++;
                if (grade.isNumeric()) {
                    sum = sum.add(grade.getNumericValue());
                    quantity++;
                }
            } else if (NA.equals(grade.getValue())) {
                notEvaluated++;
            } else if (grade.isNotApproved()) {
                flunked++;
            }
            return this;
        }

        public BigDecimal getAverage() {
            if (quantity > 0) {
                return sum.divide(new BigDecimal(quantity), RoundingMode.HALF_EVEN);
            } else {
                return BigDecimal.ZERO;
            }

        }

        public int getTotal() {
            return notEvaluated + approved + flunked + attending;
        }

    }

    private static class CurricularYearGradeEntry {

        BigDecimal sum = BigDecimal.ZERO;

        Integer notEvaluated = 0;
        Integer approved = 0;
        Integer flunked = 0;
        Integer attending = 0;

        Integer quantity = 0;

        public CurricularYearGradeEntry(Enrolment enrolment) {
            plus(enrolment);
        }

        public CurricularYearGradeEntry plus(Enrolment enrolment) {
            Grade grade = enrolment.getGrade();
            if (grade == null || grade.isEmpty()) {
                attending++;
            } else if (grade.isApproved()) {
                approved++;
                if (grade.isNumeric()) {
                    sum = sum.add(grade.getNumericValue());
                    quantity++;
                }
            } else if (NA.equals(grade.getValue())) {
                notEvaluated++;
            } else if (grade.isNotApproved()) {
                flunked++;
            }
            return this;
        }

        public BigDecimal getAverage() {
            if (quantity > 0) {
                return sum.divide(new BigDecimal(quantity), RoundingMode.HALF_EVEN);
            } else {
                return BigDecimal.ZERO;
            }

        }

        public int getTotal() {
            return notEvaluated + approved + flunked + attending;
        }
    }

    private JsonObject computeExecutionYearStatistics(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear) {
        JsonObject result = new JsonObject();
        result.addProperty("id", executionYear.getExternalId());
        result.addProperty("name", executionYear.getQualifiedName());

        Map<CurricularYear, CurricularYearGradeEntry> curricularYearGradeMap =
                new HashMap<CurricularYear, CurricularYearGradeEntry>();

        Map<CurricularCourse, CurricularCourseGradeEntry> curricularCourseGradeMap =
                new HashMap<CurricularCourse, CurricularCourseGradeEntry>();

        for (Enrolment enrolment : getDegreeCurricularPlanEnrolmentsForExecutionYear(degreeCurricularPlan, executionYear)) {
            updateCurricularYearGradeMapIfRelevant(curricularYearGradeMap, enrolment, executionYear);
            updateCurricularCourseGradeMapIfRelevant(curricularCourseGradeMap, enrolment);
        }

        int degreeCurricularPlanAttending = 0;
        int degreeCurricularPlanApproved = 0;
        int degreeCurricularPlanNotEvaluated = 0;
        int degreeCurricularPlanFlunked = 0;

        JsonArray curricularYearsJsonArray = new JsonArray();
        for (Entry<CurricularYear, CurricularYearGradeEntry> entry : curricularYearGradeMap.entrySet()) {

            JsonObject curricularYearJsonObject = new JsonObject();
            curricularYearJsonObject.addProperty("year", entry.getKey().getYear());
            curricularYearJsonObject.addProperty("average", entry.getValue().getAverage());
            curricularYearJsonObject.addProperty("total", entry.getValue().getTotal());

            int curricularYearApproved = entry.getValue().approved;
            degreeCurricularPlanApproved += curricularYearApproved;

            int curricularYearFlunked = entry.getValue().flunked;
            degreeCurricularPlanFlunked += curricularYearFlunked;

            int curricularYearNotEvaluated = entry.getValue().notEvaluated;
            degreeCurricularPlanNotEvaluated += curricularYearNotEvaluated;

            int curricularYearAttending = entry.getValue().attending;
            degreeCurricularPlanAttending += curricularYearAttending;

            curricularYearJsonObject.addProperty("approved", curricularYearApproved);
            curricularYearJsonObject.addProperty("flunked", curricularYearFlunked);
            curricularYearJsonObject.addProperty("not-evaluated", curricularYearNotEvaluated);
            curricularYearJsonObject.addProperty("attending", curricularYearAttending);

            curricularYearsJsonArray.add(curricularYearJsonObject);
        }

        result.addProperty("attending", degreeCurricularPlanAttending);
        result.addProperty("approved", degreeCurricularPlanApproved);
        result.addProperty("notEvaluated", degreeCurricularPlanNotEvaluated);
        result.addProperty("flunked", degreeCurricularPlanFlunked);

        result.addProperty("total", degreeCurricularPlanAttending + degreeCurricularPlanApproved
                + degreeCurricularPlanNotEvaluated + degreeCurricularPlanFlunked);

        JsonArray curricularCoursesJsonArray = new JsonArray();

        int years = degreeCurricularPlan.getDurationInYears();
        for (int i = 1; i <= years; i++) {
            JsonObject year = new JsonObject();
            year.addProperty("year", i);
            JsonArray curricularCoursesArray = new JsonArray();
            for (CurricularCourse curricularCourse : degreeCurricularPlan
                    .getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, i)) {
                if (curricularCourseGradeMap.containsKey(curricularCourse)) {
                    CurricularCourseGradeEntry entry = curricularCourseGradeMap.get(curricularCourse);

                    JsonObject curricularCourseJsonObject = new JsonObject();
                    int curricularCourseApproved = entry.approved;
                    int curricularCourseFlunked = entry.flunked;
                    int curricularCourseNotEvaluated = entry.notEvaluated;
                    int curricularCourseAttending = entry.attending;

                    curricularCourseJsonObject.addProperty("acronym", curricularCourse.getAcronym());
                    curricularCourseJsonObject.addProperty("name", curricularCourse.getName());

                    curricularCourseJsonObject.addProperty("approved", curricularCourseApproved);
                    curricularCourseJsonObject.addProperty("flunked", curricularCourseFlunked);
                    curricularCourseJsonObject.addProperty("not-evaluated", curricularCourseNotEvaluated);
                    curricularCourseJsonObject.addProperty("attending", curricularCourseAttending);

                    curricularCourseJsonObject.addProperty("average", entry.getAverage());

                    curricularCourseJsonObject.addProperty("total", entry.getTotal());

                    JsonArray gradesArray = new JsonArray();
                    for (Enrolment enrolment : entry.enrolmentList) {
                        Grade grade = enrolment.getGrade();
                        JsonObject enrolmentJson = new JsonObject();
                        if (grade != null && grade.isApproved()) {
                            enrolmentJson.addProperty("grade", grade.getIntegerValue());
                        } else if (NA.equals(grade.getValue())) {
                            enrolmentJson.addProperty("grade", "NA");
                        } else if (grade.isNotApproved()) {
                            enrolmentJson.addProperty("grade", "RE");
                        }
                        gradesArray.add(enrolmentJson);
                    }
                    curricularCourseJsonObject.add("grades", gradesArray);

                    curricularCoursesArray.add(curricularCourseJsonObject);
                }
            }
            year.add("entries", curricularCoursesArray);

            curricularCoursesJsonArray.add(year);
        }

        result.add("curricular-years", curricularYearsJsonArray);
        result.add("curricular-courses", curricularCoursesJsonArray);
        return result;

    }

    private void updateCurricularYearGradeMapIfRelevant(Map<CurricularYear, CurricularYearGradeEntry> map, Enrolment enrolment,
            ExecutionYear executionYear) {
        CurricularYear year = CurricularYear.readByYear(enrolment.getRegistration().getCurricularYear(executionYear));
        if (map.containsKey(year)) {
            map.get(year).plus(enrolment);
        } else {
            map.put(year, new CurricularYearGradeEntry(enrolment));
        }
    }

    private void updateCurricularCourseGradeMapIfRelevant(Map<CurricularCourse, CurricularCourseGradeEntry> map,
            Enrolment enrolment) {
        CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        if (map.containsKey(curricularCourse)) {
            map.get(curricularCourse).plus(enrolment);
        } else {
            map.put(curricularCourse, new CurricularCourseGradeEntry(enrolment));
        }
    }

}
