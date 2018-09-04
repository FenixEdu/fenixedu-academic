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
package org.fenixedu.academic.domain.reports;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.util.HtmlToTextConverterUtil;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

public abstract class GepReportFile extends GepReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(GepReportFile.class);
    public static final String CODE_SEPARATOR = "_#_";

    public GepReportFile() {
        super();
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    public static String getExecutionSemesterCode(ExecutionSemester executionSemester) {
        return executionSemester.getSemester() + CODE_SEPARATOR + executionSemester.getYear();
    }

    public static String getExecutionCourseCode(ExecutionCourse executionCourse) {
        return executionCourse.getSigla() + CODE_SEPARATOR + getExecutionSemesterCode(executionCourse.getExecutionPeriod());
    }

    public static String getExecutionDegreeCode(ExecutionDegree executionDegree) {
        return getDegreeCurricularPlanCode(executionDegree.getDegreeCurricularPlan()) + CODE_SEPARATOR
                + executionDegree.getExecutionYear().getName();
    }

    private static String getDegreeCurricularPlanCode(DegreeCurricularPlan dcp) {
        return dcp.getName() + CODE_SEPARATOR + dcp.getDegree().getSigla();
    }

    public static String getCompetenceCourseCode(CompetenceCourse competenceCourse) {
        return competenceCourse.getAcronym();
    }

    public static String getProfessorshipCode(Professorship professorship) {
        return professorship.getPerson().getUsername() + CODE_SEPARATOR
                + getExecutionCourseCode(professorship.getExecutionCourse());
    }

    public static String getShiftCode(Shift shift) {
        return shift.getNome() + CODE_SEPARATOR + getExecutionCourseCode(shift.getExecutionCourse());
    }

    public static String getWrittenEvaluationCode(WrittenEvaluation writtenEvaluation) {
        StringBuilder code =
                new StringBuilder().append(writtenEvaluation.getInterval()).append(writtenEvaluation.getFullName())
                        .append(writtenEvaluation.getEvaluationType());
        writtenEvaluation.getAssociatedExecutionCoursesSet().stream().forEach(ec -> code.append(getExecutionCourseCode(ec)));
        return Hashing.murmur3_128().hashBytes(code.toString().getBytes(StandardCharsets.UTF_8)).toString();
    }

    public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
        ExecutionYear executionYearFourYearsBack = executionYear;
        if (executionYear != null) {
            for (int i = 5; i > 1; i--) {
                final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
                if (previousExecutionYear != null) {
                    executionYearFourYearsBack = previousExecutionYear;
                }
            }
        }
        return executionYearFourYearsBack;
    }

    @Override
    public String getDescription() {
        return " no formato " + getType().toUpperCase();
    }

    public abstract String getJobName();

    protected abstract String getPrefix();

    @Override
    public String getFilename() {
        return getReportName().replace(' ', '_') + "." + getType();
    }

    private String getReportName() {

        final StringBuilder result = new StringBuilder();
        result.append(getRequestDate().toString("yyyy_MM_dd_HH_mm")).append("_");
        result.append(getPrefix()).append("_");
        result.append(getDegreeType() == null ? "Todos_Cursos" : getDegreeType().getName().getContent()).append("_");
        result.append(getExecutionYear() == null ? "Todos_Anos" : getExecutionYear().getName().replace('/', '_'));

        return result.toString();
    }

    protected void setDegreeHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("tipo curso");
        spreadsheet.setHeader("nome curso");
        spreadsheet.setHeader("sigla curso");
    }

    protected void setDegreeHeaders(final Spreadsheet spreadsheet, final String suffix) {
        spreadsheet.setHeader("tipo curso " + suffix);
        spreadsheet.setHeader("nome curso " + suffix);
        spreadsheet.setHeader("sigla curso " + suffix);
    }

    protected void setDegreeCells(final Row row, final Degree degree) {
        row.setCell(degree.getDegreeType().getName().getContent());
        row.setCell(degree.getNameI18N().getContent());
        row.setCell(degree.getSigla());
    }

    protected boolean checkDegreeType(final DegreeType degreeType, final ConclusionProcess conclusionProcess) {
        return degreeType == null || conclusionProcess.getDegree().getDegreeType() == degreeType;
    }

    protected static boolean checkDegreeType(final DegreeType degreeType, final Degree degree) {
        return degreeType == null || degree.getDegreeType() == degreeType;
    }

    protected static boolean checkExecutionYear(final ExecutionYear executionYear, final DegreeCurricularPlan degreeCurricularPlan) {
        return executionYear == null || degreeCurricularPlan.hasExecutionDegreeFor(executionYear);
    }

    protected static boolean checkExecutionYear(ExecutionYear executionYear, final CurricularCourse curricularCourse) {
        return executionYear == null || curricularCourse.isActive(executionYear);
    }

    protected static boolean checkExecutionYear(ExecutionYear executionYear, ExecutionCourse executionCourse) {
        return executionYear == null || executionCourse.getExecutionYear().equals(executionYear);
    }

    protected boolean isActive(final Degree degree) {
        return degree.getDegreeCurricularPlansSet().stream()
                .anyMatch(degreeCurricularPlan -> checkExecutionYear(getExecutionYear(), degreeCurricularPlan));
    }

    protected String normalize(final String text) {
        if (!StringUtils.isEmpty(text)) {
            String result = "";
            try {
                result = HtmlToTextConverterUtil.convertToText(text);
            } catch (Exception ex) {
                result = HtmlToTextConverterUtil.convertToTextWithRegEx(text);
            }
            return result.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
        }
        return "";
    }

    public abstract void renderReport(Spreadsheet spreadsheet) throws Exception;

    @Override
    public QueueJobResult execute() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(getReportName());

        this.renderReport(spreadsheet);

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        if ("csv".compareTo(getType()) == 0) {
            spreadsheet.exportToCSV(byteArrayOS, "\t");
        } else {
            spreadsheet.exportToXLSSheet(byteArrayOS);
        }

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("application/txt");
        queueJobResult.setContent(byteArrayOS.toByteArray());

        logger.info("Job " + getFilename() + " completed");

        return queueJobResult;
    }

    public String getUpperCaseType() {
        return this.getType().toUpperCase();
    }

    protected static List<Registration> getFullRegistrationPath(final Registration current) {
        if (current.getDegreeType().isBolonhaDegree() || current.getDegreeType().isIntegratedMasterDegree()) {
            List<Registration> path = new ArrayList<Registration>();
            path.add(current);
            Registration source;
            if (current.getSourceRegistration() != null
                    && (!(source = current.getSourceRegistration()).isBolonha() || isValidSourceLink(source))) {
                path.addAll(getFullRegistrationPath(source));
            } else if ((source = findSourceRegistrationByEquivalencePlan(current)) != null) {
                path.addAll(getFullRegistrationPath(source));
            }
            path.sort(Registration.COMPARATOR_BY_START_DATE);
            return path;
        } else {
            return Collections.singletonList(current);
        }
    }

    protected static boolean isValidSourceLink(Registration source) {
        return Stream.of(RegistrationStateType.TRANSITED, RegistrationStateType.FLUNKED, RegistrationStateType.INTERNAL_ABANDON,
                RegistrationStateType.EXTERNAL_ABANDON, RegistrationStateType.INTERRUPTED)
                .anyMatch(registrationStateType -> source.getActiveStateType() == registrationStateType);
    }

    private static Registration findSourceRegistrationByEquivalencePlan(Registration targetRegistration) {
        final DegreeCurricularPlan targetDegreeCurricularPlan = targetRegistration.getLastDegreeCurricularPlan();
        if (targetDegreeCurricularPlan.getEquivalencePlan() != null) {
            for (Registration sourceRegistration : targetRegistration.getStudent().getRegistrationsSet()) {
                final DegreeCurricularPlan sourceDegreeCurricularPlan = sourceRegistration.getLastDegreeCurricularPlan();
                if (sourceRegistration != targetRegistration
                        && sourceRegistration.getActiveStateType() == RegistrationStateType.TRANSITED
                        && targetDegreeCurricularPlan.getEquivalencePlan().getSourceDegreeCurricularPlan()
                                .equals(sourceDegreeCurricularPlan)) {
                    return sourceRegistration;
                }
            }
        }
        return null;
    }

}
