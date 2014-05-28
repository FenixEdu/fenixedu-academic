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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminScholarshipsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminScholarshipsApp.class, path = "report-students-utl",
        titleKey = "label.academicAdminOffice.scholarship.utl.report", accessGroup = "academic(REPORT_STUDENTS_UTL_CANDIDATES)")
@Mapping(path = "/reportStudentsUTLCandidates", module = "academicAdministration")
@Forwards({ @Forward(name = "prepare", path = "/academicAdminOffice/scholarship/utl/report/prepare.jsp"),
        @Forward(name = "showReport", path = "/academicAdminOffice/scholarship/utl/report/showReport.jsp"),
        @Forward(name = "viewDetails", path = "/academicAdminOffice/scholarship/utl/report/viewDetails.jsp"),
        @Forward(name = "prepareForOneStudent", path = "/academicAdminOffice/scholarship/utl/report/prepareForOneStudent.jsp") })
public class ReportStudentsUTLCandidatesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ReportStudentsUTLCandidatesBean bean = new ReportStudentsUTLCandidatesBean();
        request.setAttribute("bean", bean);

        return mapping.findForward("prepare");
    }

    public ActionForward showReport(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        ReportStudentsUTLCandidatesBean bean = getRenderedObject("bean");

        if (bean == null || bean.getXlsFile() == null) {
            return prepare(mapping, actionForm, request, response);
        }

        POIFSFileSystem fs = new POIFSFileSystem(bean.getXlsFile());
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);

        if (sheet == null) {
            addErrorMessage(request, "error", "error.academicAdminOffice.scholarship.utl.report.invalid.spreadsheet",
                    new String[0]);
            return prepare(mapping, actionForm, request, response);
        }

        ReportStudentsUTLCandidates report = null;

        if (bean.getForFirstYear()) {
            report = new ReportStudentsUTLCandidatesForFirstYear(bean.getExecutionYear(), sheet);
        } else {
            report = new ReportStudentsUTLCandidates(bean.getExecutionYear(), sheet);
        }

        request.setAttribute("report", report);

        List<StudentLine> correctStudentLines = new ArrayList<StudentLine>();
        List<StudentLine> erroneousStudentLines = new ArrayList<StudentLine>();

        erroneousStudentLines.addAll(report.getErroneousStudentLines());

        for (StudentLine studentLine : report.getCorrectStudentLines()) {
            if (studentLine.isAbleToReadAllValues()) {
                correctStudentLines.add(studentLine);
            } else {
                erroneousStudentLines.add(studentLine);
            }
        }

        request.setAttribute("correctStudentLines", correctStudentLines);
        request.setAttribute("erroneousStudentLines", erroneousStudentLines);

        return mapping.findForward("showReport");
    }

    public ActionForward viewDetails(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        return mapping.findForward("viewDetails");
    }

    public ActionForward exportReport(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        ReportStudentsUTLCandidates report = getRenderedObject("report");

        HSSFWorkbook generateReport = report.generateReport();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=bolsa_accao_social_utl.xls");
        generateReport.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.flushBuffer();

        return null;
    }

    public ActionForward exportErrors(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        ReportStudentsUTLCandidates report = getRenderedObject("report");

        HSSFWorkbook generateReport = report.generateErrors();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=erros_bolsa_accao_social.xls");
        generateReport.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.flushBuffer();

        return null;
    }

    public ActionForward prepareForOneStudent(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ReportStudentsUTLCandidatesBean bean = new ReportStudentsUTLCandidatesBean();

        request.setAttribute("bean", bean);
        return mapping.findForward("prepareForOneStudent");
    }

    public ActionForward showReportForOneStudent(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ReportStudentsUTLCandidatesBean bean = getRenderedObject("bean");

        if (bean == null) {
            return prepareForOneStudent(mapping, form, request, response);
        }

        ReportStudentsUTLCandidates report = null;
        if (bean.getForFirstYear()) {
            report =
                    new ReportStudentsUTLCandidatesForOneStudentFirstYear(bean.getExecutionYear(),
                            Student.readStudentByNumber(bean.getStudentNumber()));
        } else {
            report =
                    new ReportStudentsUTLCandidatesForOneStudent(bean.getExecutionYear(), Student.readStudentByNumber(bean
                            .getStudentNumber()));
        }

        request.setAttribute("report", report);

        List<StudentLine> correctStudentLines = new ArrayList<StudentLine>();
        List<StudentLine> erroneousStudentLines = new ArrayList<StudentLine>();

        erroneousStudentLines.addAll(report.getErroneousStudentLines());

        for (StudentLine studentLine : report.getCorrectStudentLines()) {
            if (studentLine.isAbleToReadAllValues()) {
                correctStudentLines.add(studentLine);
            } else {
                erroneousStudentLines.add(studentLine);
            }
        }

        request.setAttribute("correctStudentLines", correctStudentLines);
        request.setAttribute("erroneousStudentLines", erroneousStudentLines);

        return mapping.findForward("showReport");
    }
}
