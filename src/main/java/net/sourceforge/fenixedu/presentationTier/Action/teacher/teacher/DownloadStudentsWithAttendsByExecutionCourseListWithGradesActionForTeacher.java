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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.teacher;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(module = "teacher", path = "/getTabSeparatedStudentListWithGrades", functionality = ManageExecutionCourseDA.class)
public class DownloadStudentsWithAttendsByExecutionCourseListWithGradesActionForTeacher extends FenixDispatchAction {

    public ActionForward downloadStudentListForGradesForm(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final ExecutionCourse executionCourse = getDomainObject(request, "executionCourseOID");

        final Spreadsheet spreadsheet = new Spreadsheet(executionCourse.getSigla());
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.number"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, "label.degree.code"));
        spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION,
                "label.attends.enrollmentState"));
        final List<Evaluation> evaluations = executionCourse.getOrderedAssociatedEvaluations();
        for (final Evaluation evaluation : evaluations) {
            if (evaluation instanceof AdHocEvaluation) {
                final AdHocEvaluation adHocEvaluation = (AdHocEvaluation) evaluation;
                spreadsheet.setHeader(adHocEvaluation.getName());
            } else if (evaluation instanceof FinalEvaluation) {
                final FinalEvaluation finalEvaluation = (FinalEvaluation) evaluation;
                spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION,
                        "label.final.evaluation"));
            } else if (evaluation instanceof OnlineTest) {
                final OnlineTest onlineTest = (OnlineTest) evaluation;
                spreadsheet.setHeader(onlineTest.getDistributedTest().getTitle());
            } else if (evaluation instanceof Project) {
                final Project project = (Project) evaluation;
                spreadsheet.setHeader(project.getName());
            } else if (evaluation instanceof Exam) {
                final Exam exam = (Exam) evaluation;
                spreadsheet.setHeader(BundleUtil.getString(Bundle.APPLICATION, exam.getSeason()
                        .getKey()));
            } else if (evaluation instanceof WrittenTest) {
                final WrittenTest writtenTest = (WrittenTest) evaluation;
                spreadsheet.setHeader(writtenTest.getDescription());
            } else {
                throw new Error("unknow.evaluation.type: " + evaluation);
            }
        }

        for (final Attends attends : executionCourse.getOrderedAttends()) {
            final Registration registration = attends.getRegistration();
            final Row row = spreadsheet.addRow();
            row.setCell(registration.getNumber());
            row.setCell(registration.getName());
            row.setCell(registration.getDegree().getSigla());
            if (attends.hasEnrolment()) {
                final EnrolmentEvaluationType enrolmentEvaluationType = attends.getEnrolmentEvaluationType();
                row.setCell(enrolmentEvaluationType.getDescription());
            } else {
                row.setCell(BundleUtil.getString(Bundle.APPLICATION,
                        "label.attends.enrollmentState.notEnrolled"));
            }
            for (final Evaluation evaluation : evaluations) {
                final Mark mark = findMark(attends, evaluation);
                if (mark == null) {
                    row.setCell(" ");
                } else {
                    row.setCell(mark.getMark());
                }
            }
        }

        try {
            response.setHeader("Content-disposition", "attachment; filename=" + executionCourse.getSigla() + ".xls");
            response.setContentType("application/vnd.ms-excel");

            final ServletOutputStream outputStream = response.getOutputStream();
            spreadsheet.exportToXLSSheet(outputStream);
            outputStream.close();
        } catch (final IOException e1) {
            throw new Error(e1);
        }

        return null;

    }

    private Mark findMark(final Attends attends, final Evaluation evaluation) {
        for (final Mark mark : attends.getAssociatedMarksSet()) {
            if (mark.getEvaluation() == evaluation) {
                return mark;
            }
        }
        return null;
    }

}