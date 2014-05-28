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
package net.sourceforge.fenixedu.domain.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcess;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public abstract class GepReportFile extends GepReportFile_Base {

    private static final Logger logger = LoggerFactory.getLogger(GepReportFile.class);

    public GepReportFile() {
        super();
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
        result.append(getDegreeType() == null ? "Todos_Cursos" : getDegreeType().name()).append("_");
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
        row.setCell(degree.getDegreeType().getLocalizedName());
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
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                return true;
            }
        }
        return false;
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
        if (current.getDegreeType() == DegreeType.BOLONHA_DEGREE
                || current.getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
            List<Registration> path = new ArrayList<Registration>();
            path.add(current);
            Registration source;
            if (current.hasSourceRegistration()
                    && (!(source = current.getSourceRegistration()).isBolonha() || isValidSourceLink(source))) {
                path.addAll(getFullRegistrationPath(source));
            } else if ((source = findSourceRegistrationByEquivalencePlan(current)) != null) {
                path.addAll(getFullRegistrationPath(source));
            }
            Collections.sort(path, Registration.COMPARATOR_BY_START_DATE);
            return path;
        } else {
            return Collections.singletonList(current);
        }
    }

    protected static boolean isValidSourceLink(Registration source) {
        return source.getActiveStateType().equals(RegistrationStateType.TRANSITED)
                || source.getActiveStateType().equals(RegistrationStateType.FLUNKED)
                || source.getActiveStateType().equals(RegistrationStateType.INTERNAL_ABANDON)
                || source.getActiveStateType().equals(RegistrationStateType.EXTERNAL_ABANDON)
                || source.getActiveStateType().equals(RegistrationStateType.INTERRUPTED);
    }

    private static Registration findSourceRegistrationByEquivalencePlan(Registration targetRegistration) {
        final DegreeCurricularPlan targetDegreeCurricularPlan = targetRegistration.getLastDegreeCurricularPlan();
        if (targetDegreeCurricularPlan.getEquivalencePlan() != null) {
            for (Registration sourceRegistration : targetRegistration.getStudent().getRegistrations()) {
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

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasDegreeType() {
        return getDegreeType() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
