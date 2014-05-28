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
package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.curriculumLineLog.SearchCurriculumLineLog;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.log.CurriculumLineLog;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerStudentsApp;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeTableXYDataset;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "curriculum-logs", titleKey = "title.curriculum.line.logs")
@Mapping(path = "/curriculumLineLogs", module = "manager")
@Forwards({ @Forward(name = "searchCurriculumLineLogs", path = "/manager/viewCurriculumLineLogs.jsp"),
        @Forward(name = "viewCurriculumLineLogStatistics", path = "/manager/viewCurriculumLineLogStatistics.jsp") })
public class CurriculumLineLogsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareViewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("bean", new SearchCurriculumLineLog());
        return mapping.findForward("searchCurriculumLineLogs");
    }

    public ActionForward viewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final SearchCurriculumLineLog searchCurriculumLineLog = getRenderedObject();
        request.setAttribute("bean", searchCurriculumLineLog);
        Student student = Student.readStudentByNumber(searchCurriculumLineLog.getStudentNumber());

        if (student == null) {
            addActionMessage(request, "exception.student.does.not.exist");
            return mapping.findForward("searchCurriculumLineLogs");
        }

        request.setAttribute("curriculumLineLogs", student.getCurriculumLineLogs(searchCurriculumLineLog.getExecutionPeriod()));
        return mapping.findForward("searchCurriculumLineLogs");
    }

    public static class CurriculumLineLogStatisticsCalculator {

        private static final int INTERVAL_SIZE_IN_MILLIS = 300000;

        final Interval enrolmentPeriod;
        final int[] enrolments;
        final int[] unenrolments;

        public CurriculumLineLogStatisticsCalculator(final ExecutionSemester executionSemester) {
            enrolmentPeriod = findEnrolmentPeriod(executionSemester);
            final long start = enrolmentPeriod.getStart().getMillis();
            final long durationMillis = enrolmentPeriod.toDurationMillis();
            final int numberOfIntervals = (int) (durationMillis / INTERVAL_SIZE_IN_MILLIS) + 1;

            enrolments = new int[numberOfIntervals];
            unenrolments = new int[numberOfIntervals];

            for (final CurriculumLineLog curriculumLineLog : executionSemester.getCurriculumLineLogsSet()) {
                if (curriculumLineLog instanceof EnrolmentLog) {
                    final DateTime dateTime = curriculumLineLog.getDateDateTime();
                    if (enrolmentPeriod.contains(dateTime)) {
                        final long offset = dateTime.getMillis() - start;
                        final int i = (int) offset / INTERVAL_SIZE_IN_MILLIS;

                        if (curriculumLineLog.getAction() == EnrolmentAction.ENROL) {
                            enrolments[i]++;
                        } else {
                            unenrolments[i]++;
                        }
                    }
                }
            }
        }

        public Interval getEnrolmentPeriod() {
            return enrolmentPeriod;
        }

        public int[] getEnrolments() {
            return enrolments;
        }

        public int[] getUnenrolments() {
            return unenrolments;
        }

        private Interval findEnrolmentPeriod(final ExecutionSemester executionSemester) {
            DateTime start = null, end = null;
            for (final EnrolmentPeriod enrolmentPeriod : executionSemester.getEnrolmentPeriodSet()) {
                if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
                    final DegreeType degreeType = enrolmentPeriod.getDegreeCurricularPlan().getDegreeType();
                    if (degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                            || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
                        if (start == null || start.isAfter(enrolmentPeriod.getStartDateDateTime())) {
                            start = enrolmentPeriod.getStartDateDateTime();
                        }
                        if (end == null || end.isBefore(enrolmentPeriod.getEndDateDateTime())) {
                            end = enrolmentPeriod.getEndDateDateTime();
                        }
                    }
                }
            }
            return start == null || end == null ? null : new Interval(start, end);
        }

        public byte[] getOperationsChart() throws IOException {
            final TimeTableXYDataset dataset = new TimeTableXYDataset();

            DateTime index = enrolmentPeriod.getStart();
            for (int i = 0; i < enrolments.length; i++) {
                final Second second = new Second(index.toDate());
                index = index.plus(INTERVAL_SIZE_IN_MILLIS);

                final int enrolmentCount = enrolments[i];
                dataset.add(second, enrolmentCount, "Enrolments");

                final int unenrolmentCount = unenrolments[i];
                dataset.add(second, unenrolmentCount, "Unenrolments");
            }

            return getOperationsChart(dataset);
        }

        private byte[] getOperationsChart(final TimeTableXYDataset dataset) throws IOException {
            final JFreeChart jfreeChart = ChartFactory.createTimeSeriesChart("", "", "", dataset, true, true, true);
            final BufferedImage bufferedImage = jfreeChart.createBufferedImage(1000, 500);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            bufferedImage.flush();
            outputStream.close();
            return outputStream.toByteArray();
        }

    }

    public ActionForward viewCurriculumLineLogStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");

        if (executionSemester != null) {
            request.setAttribute("executionSemester", executionSemester);

            final CurriculumLineLogStatisticsCalculator curriculumLineLogStatisticsCalculator =
                    new CurriculumLineLogStatisticsCalculator(executionSemester);
            request.setAttribute("curriculumLineLogStatisticsCalculator", curriculumLineLogStatisticsCalculator);
            return mapping.findForward("viewCurriculumLineLogStatistics");
        }

        return prepareViewCurriculumLineLogs(mapping, form, request, response);
    }

    public ActionForward viewCurriculumLineLogStatisticsChartOperations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");

        if (executionSemester != null) {
            final CurriculumLineLogStatisticsCalculator curriculumLineLogStatisticsCalculator =
                    new CurriculumLineLogStatisticsCalculator(executionSemester);

            ServletOutputStream writer = null;
            try {
                writer = response.getOutputStream();
                response.setContentType("image/jpeg");
                writer.write(curriculumLineLogStatisticsCalculator.getOperationsChart());
                writer.flush();
            } finally {
                writer.close();
                response.flushBuffer();
            }

        }

        return null;
    }
}