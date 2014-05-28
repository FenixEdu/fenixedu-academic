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
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public abstract class EvaluationMethodControlDA extends FenixDispatchAction {

    private abstract static class MethodInvoker {
        public abstract void export(final Spreadsheet spreadsheet, final OutputStream outputStream) throws IOException;

        public abstract String getExtension();
    }

    @EntryPoint
    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean =
                getSearchBean(request);
        request.setAttribute("executionCourseWithNoEvaluationMethodSearchBean", executionCourseWithNoEvaluationMethodSearchBean);
        request.setAttribute("executionCourses", executionCourseWithNoEvaluationMethodSearchBean.getSearchResult());
        return mapping.findForward("search");
    }

    public ActionForward exportToXLS(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        export(request, response, new MethodInvoker() {
            @Override
            public void export(Spreadsheet spreadsheet, OutputStream outputStream) throws IOException {
                spreadsheet.exportToXLSSheet(outputStream);
            }

            @Override
            public String getExtension() {
                return ".xls";
            }
        });
        return null;
    }

    public ActionForward exportToCSV(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        export(request, response, new MethodInvoker() {
            @Override
            public void export(Spreadsheet spreadsheet, OutputStream outputStream) throws IOException {
                spreadsheet.exportToCSV(outputStream, "\t");
            }

            @Override
            public String getExtension() {
                return ".csv";
            }
        });
        return null;
    }

    public ActionForward export(HttpServletRequest request, HttpServletResponse response, final MethodInvoker methodInvoker)
            throws FenixServiceException {

        final ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean =
                getSearchBean(request);

        try {
            String filename = "ControloMetodosAvaliacao:" + new DateTime().toString("yyyyMMddHHmm");
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + methodInvoker.getExtension());

            ServletOutputStream writer = response.getOutputStream();

            final Spreadsheet spreadsheet = new Spreadsheet("ControloMetodosAvaliacao");
            for (final ExecutionCourse executionCourse : (Set<ExecutionCourse>) executionCourseWithNoEvaluationMethodSearchBean
                    .getSearchResult()) {
                final Row row = spreadsheet.addRow();
                row.setCell(executionCourse.getNome());
                final StringBuilder degrees = new StringBuilder();
                for (final Degree degree : executionCourse.getDegreesSortedByDegreeName()) {
                    if (degrees.length() > 0) {
                        degrees.append(", ");
                    }
                    degrees.append(degree.getSigla());
                }
                row.setCell(degrees.toString());
                final StringBuilder responsibleTeachers = new StringBuilder();
                final StringBuilder otherTeachers = new StringBuilder();
                for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                    final Person person = professorship.getPerson();
                    if (professorship.isResponsibleFor()) {
                        if (responsibleTeachers.length() > 0) {
                            responsibleTeachers.append(", ");
                        }
                        responsibleTeachers.append(person.getName());
                        responsibleTeachers.append(" (");
                        responsibleTeachers.append(person.getEmail());
                        responsibleTeachers.append(" )");
                    } else {
                        if (otherTeachers.length() > 0) {
                            otherTeachers.append(", ");
                        }
                        otherTeachers.append(person.getName());
                        otherTeachers.append(" (");
                        otherTeachers.append(person.getEmail());
                        otherTeachers.append(" )");
                    }
                }
                row.setCell(responsibleTeachers.toString());
                row.setCell(otherTeachers.toString());
                final StringBuilder departments = new StringBuilder();
                for (final Department department : executionCourse.getDepartments()) {
                    if (departments.length() > 0) {
                        departments.append(", ");
                    }
                    departments.append(department.getName());
                }
                row.setCell(departments.toString());
            }
            methodInvoker.export(spreadsheet, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private ExecutionCourseWithNoEvaluationMethodSearchBean getSearchBean(HttpServletRequest request) {
        ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean =
                getExecutionCourseWithNoEvaluationMethodSearchBean(request);
        if (executionCourseWithNoEvaluationMethodSearchBean == null) {
            executionCourseWithNoEvaluationMethodSearchBean = new ExecutionCourseWithNoEvaluationMethodSearchBean();
        }
        return executionCourseWithNoEvaluationMethodSearchBean;
    }

    private ExecutionCourseWithNoEvaluationMethodSearchBean getExecutionCourseWithNoEvaluationMethodSearchBean(
            final HttpServletRequest request) {
        final ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean =
                getRenderedObject();
        return executionCourseWithNoEvaluationMethodSearchBean == null ? (ExecutionCourseWithNoEvaluationMethodSearchBean) request
                .getAttribute("executionCourseWithNoEvaluationMethodSearchBean") : executionCourseWithNoEvaluationMethodSearchBean;
    }

}
