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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.academic.util.FenixDigestUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MarkSheetDocument extends FenixReport {
    private static final long serialVersionUID = -1015332436546905622L;

    protected MarkSheet markSheet;

    public MarkSheetDocument(MarkSheet markSheet) {
        super();
        this.markSheet = markSheet;
        fillReport();
    }

    @Override
    public String getKey() {
        if (markSheet.isRectification()) {
            return "markSheetRectification";
        }
        return "markSheet";
    }

    @Override
    public String getReportTemplateKey() {
        if (markSheet.isRectification()) {
            return super.getReportTemplateKey() + "-rectified";
        }
        return super.getReportTemplateKey();
    }

    @Override
    public String getReportFileName() {
        return "MarkSheet-" + markSheet.getCheckSum() + (markSheet.isRectification() ? "-rectified" : "");
    }

    @Override
    protected void fillReport() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        getPayload().addProperty("executionYear", markSheet.getExecutionPeriod().getExecutionYear().getYear());
        getPayload().addProperty("executionSemester", markSheet.getExecutionPeriod().getSemester());
        getPayload().addProperty("currentDate", new LocalDate().toString(dtf));
        getPayload().addProperty("profUsername", markSheet.getResponsibleTeacher().getTeacherId());
        getPayload().addProperty("profName", markSheet.getResponsibleTeacher().getPerson().getName());
        if (!markSheet.getCurricularCourse().isBolonhaDegree()) {
            getPayload().addProperty("curricularCourseCode", markSheet.getCurricularCourse().getCode());
        }
        getPayload().addProperty("curricularCourseName", markSheet.getCurricularCourseName());
        getPayload().addProperty("degreeName", markSheet.getDegreeName());
        getPayload().addProperty("checksum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));

        if(markSheet.isRectification()) {
            final EnrolmentEvaluation rectification = markSheet.getEnrolmentEvaluationsSet().iterator().next();
            final EnrolmentEvaluation rectified = rectification.getRectified();

            getPayload().addProperty("studentNumber", rectification.getRegistration().getNumber());
            getPayload().addProperty("studentName", rectification.getRegistration().getName());
            getPayload().addProperty("rectifiedSeason", rectified.getEvaluationSeason().getAcronym().getContent());
            getPayload().addProperty("rectifiedGrade", rectified.getGradeValue());
            getPayload().addProperty("rectifiedExamDate", sdf.format(rectified.getExamDate()));
            getPayload().addProperty("rectificationSeason", rectification.getEvaluationSeason().getAcronym().getContent());
            getPayload().addProperty("rectificationGrade", rectification.getGradeValue());
            getPayload().addProperty("rectificationExamDate", sdf.format(rectification.getExamDate()));
            getPayload().addProperty("creatorUsername", markSheet.getCreator().getUsername());
            getPayload().addProperty("creationDate", markSheet.getCreationDateDateTime().toString(dtf));
        } else {
            getPayload().add("students", getStudentEntries());
        }
    }

    private JsonArray getStudentEntries() {
        JsonArray result = new JsonArray();

        List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluationsSet());
        evaluations.sort(EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);

        for (EnrolmentEvaluation evaluation : evaluations) {
            JsonObject student = new JsonObject();
            student.addProperty("number", evaluation.getRegistration().getNumber());
            student.addProperty("name", evaluation.getRegistration().getName());
            student.addProperty("season", evaluation.getEvaluationSeason().getAcronym().getContent());
            student.addProperty("grade", evaluation.getGradeValue());
            student.addProperty("examDate", new SimpleDateFormat("dd/MM/yyyy").format(evaluation.getExamDate()));
            result.add(student);
        }

        return result;
    }
}