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

import java.util.Optional;

public class MarkSheetDocument extends FenixReport {
    private static final long serialVersionUID = -1015332436546905622L;

    private static final String DATE_FORMAT_PT = "dd/MM/yyyy";

    protected MarkSheet markSheet;

    public MarkSheetDocument(MarkSheet markSheet) {
        super();
        this.markSheet = markSheet;
        fillReport();
    }

    @Override
    public String getKey() {
        return (markSheet.isRectification() ? "markSheetRectification" : "markSheet");
    }

    @Override
    public String getReportTemplateKey() {
        String key = super.getReportTemplateKey();
        return (markSheet.isRectification() ? key + "-rectified" : key);
    }

    @Override
    public String getReportFileName() {
        return "MarkSheet-" + markSheet.getCheckSum() + (markSheet.isRectification() ? "-rectified" : "");
    }

    @Override
    protected void fillReport() {
        getPayload().addProperty("executionYear", markSheet.getExecutionPeriod().getExecutionYear().getYear());
        getPayload().addProperty("executionSemester", markSheet.getExecutionPeriod().getSemester());
        getPayload().addProperty("currentDate", new LocalDate().toString(DATE_FORMAT_PT));
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
            getPayload().addProperty("rectifiedExamDate", Optional.ofNullable(rectified.getExamDate())
                            .map(d -> new LocalDate(d).toString(DATE_FORMAT_PT)).orElse(""));
            getPayload().addProperty("rectificationSeason", rectification.getEvaluationSeason().getAcronym().getContent());
            getPayload().addProperty("rectificationGrade", rectification.getGradeValue());
            getPayload().addProperty("rectificationExamDate",  Optional.ofNullable(rectification.getExamDate())
                    .map(d -> new LocalDate(d).toString(DATE_FORMAT_PT)).orElse(""));
            getPayload().addProperty("creatorUsername", markSheet.getCreator().getUsername());
            getPayload().addProperty("creationDate", markSheet.getCreationDateDateTime().toString(DATE_FORMAT_PT));
        } else {
            getPayload().add("students", getStudentEntries());
        }
    }

    private JsonArray getStudentEntries() {
        JsonArray result = new JsonArray();

        markSheet.getEnrolmentEvaluationsSet().stream().sorted(EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER).map(e -> {
            JsonObject student = new JsonObject();
            student.addProperty("number", e.getRegistration().getNumber());
            student.addProperty("name", e.getRegistration().getName());
            student.addProperty("season", e.getEvaluationSeason().getAcronym().getContent());
            student.addProperty("grade", e.getGradeValue());
            student.addProperty("examDate", Optional.ofNullable(e.getExamDate())
                    .map(d -> new LocalDate(d).toString(DATE_FORMAT_PT)).orElse(""));
            return student;
        }).forEach(result::add);

        return result;
    }

}