/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOpeningProjectFileReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Susana Fernandes
 */
public class ProjectReportsDispatchAction extends ReportsDispatchAction {

    public ActionForward prepareShowReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final String costCenter = request.getParameter("costCenter");
        final ReportType reportType = new ReportType(reportTypeStr);
        getCostCenterName(request, costCenter);
        request.setAttribute("reportType", reportTypeStr);
        if (reportType.getReportType() != null
                && (reportType.equals(ReportType.EXPENSES) || reportType.equals(ReportType.REVENUE) || reportType.equals(ReportType.CABIMENTOS) || reportType
                        .equals(ReportType.ADIANTAMENTOS)) || reportType.equals(ReportType.COMPLETE_EXPENSES)
                || reportType.equals(ReportType.OPENING_PROJECT_FILE) || reportType.equals(ReportType.PROJECT_BUDGETARY_BALANCE)) {
            final List projectList = (List) ServiceUtils.executeService(userView, "ReadUserProjects", new Object[] { userView.getUtilizador(),
                    costCenter, new Boolean(true) });

            request.setAttribute("projectList", projectList);
            return mapping.findForward("projectList");
        } else if (reportType.getReportType() != null && (reportType.equals(ReportType.SUMMARY))) {
            final List coordinatorsList = (List) ServiceUtils.executeService(userView, "ReadCoordinators", new Object[] { userView.getUtilizador(),
                    costCenter });
            if (coordinatorsList != null && coordinatorsList.size() == 1) {
                request.setAttribute("coordinatorCode", ((InfoRubric) coordinatorsList.get(0)).getCode());
                return getReport(mapping, form, request, response);
            }
            request.setAttribute("coordinatorsList", coordinatorsList);
            return mapping.findForward("coordinatorsList");
        }

        return mapping.findForward("index");
    }

    public ActionForward getReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        final String costCenter = request.getParameter("costCenter");
        if (reportType.getReportType() != null) {
            request.setAttribute("reportType", reportTypeStr);
            getCostCenterName(request, costCenter);
            if (reportType.equals(ReportType.EXPENSES) || reportType.equals(ReportType.COMPLETE_EXPENSES)) {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                final String rubric = request.getParameter("rubric");
                InfoProjectReport infoExpensesReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadExpensesReport", new Object[] {
                        userView.getUtilizador(), costCenter, reportType, projectCode, rubric });
                getSpans(request, infoExpensesReport);
                request.setAttribute("rubric", rubric);
                request.setAttribute("infoExpensesReport", infoExpensesReport);

            } else if (reportType.equals(ReportType.REVENUE) || reportType.equals(ReportType.ADIANTAMENTOS)
                    || reportType.equals(ReportType.CABIMENTOS) || reportType.equals(ReportType.PROJECT_BUDGETARY_BALANCE)) {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                final InfoProjectReport infoReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadReport", new Object[] {
                        userView.getUtilizador(), costCenter, reportType, projectCode });
                request.setAttribute("infoReport", infoReport);
            } else if (reportType.equals(ReportType.SUMMARY)) {
                final Integer coordinatorCode = getCodeFromRequest(request, "coordinatorCode");
                final InfoCoordinatorReport infoSummaryReport = (InfoCoordinatorReport) ServiceUtils.executeService(userView, "ReadSummaryReport",
                        new Object[] { userView.getUtilizador(), costCenter, coordinatorCode });
                request.setAttribute("infoSummaryReport", infoSummaryReport);
            } else if (reportType.equals(ReportType.OPENING_PROJECT_FILE)) {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                final InfoOpeningProjectFileReport infoOpeningProjectFileReport = (InfoOpeningProjectFileReport) ServiceUtils.executeService(
                        userView, "ReadOpeningProjectFileReport", new Object[] { userView.getUtilizador(), costCenter, projectCode });
                request.setAttribute("infoOpeningProjectFileReport", infoOpeningProjectFileReport);
            }
            return mapping.findForward("show" + reportTypeStr);
        }

        return mapping.findForward("index");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        final String costCenter = request.getParameter("costCenter");

        HSSFWorkbook wb = new HSSFWorkbook();
        String fileName = new String("listagem");
        InfoProjectReport infoProjectReport = null;
        if (reportType.getReportType() != null) {
            if (reportType.equals(ReportType.EXPENSES) || reportType.equals(ReportType.COMPLETE_EXPENSES)) {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                infoProjectReport = (InfoExpensesReport) ServiceUtils.executeService(userView, "ReadExpensesReport", new Object[] {
                        userView.getUtilizador(), costCenter, reportType, projectCode, request.getParameter("rubric") });
                infoProjectReport.getReportToExcel(userView, wb, reportType);
            } else if (reportType.equals(ReportType.SUMMARY)) {
                final Integer coordinatorCode = getCodeFromRequest(request, "coordinatorCode");
                final InfoCoordinatorReport infoSummaryReport = (InfoCoordinatorReport) ServiceUtils.executeService(userView, "ReadSummaryReport",
                        new Object[] { userView.getUtilizador(), costCenter, coordinatorCode });
                infoSummaryReport.getReportToExcel(userView, wb, reportType);
            } else {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                infoProjectReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadReport", new Object[] { userView.getUtilizador(),
                        costCenter, reportType, projectCode });
                infoProjectReport.getReportToExcel(userView, wb, reportType);
            }
            fileName = reportType.getReportLabel().replaceAll(" ", "_");
        }
        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            wb.write(writer);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixServiceException();
        }

        return null;
    }
}