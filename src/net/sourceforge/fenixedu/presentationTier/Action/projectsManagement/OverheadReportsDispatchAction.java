/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOverheadReport;
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
public class OverheadReportsDispatchAction extends ReportsDispatchAction {

    public ActionForward getReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        final String costCenter = request.getParameter("costCenter");
        if (reportType.getReportType() != null) {
            if (reportType.equals(ReportType.GENERATED_OVERHEADS)
                    || reportType.equals(ReportType.TRANSFERED_OVERHEADS)
                    || reportType.equals(ReportType.OVERHEADS_SUMMARY)) {
                final InfoOverheadReport infoReport = (InfoOverheadReport) ServiceUtils.executeService(
                        userView, "ReadOverheadReport", new Object[] { userView.getUtilizador(),
                                costCenter, reportType, null });
                getSpans(request, infoReport);
                request.setAttribute("infoReport", infoReport);
                request.setAttribute("reportType", reportTypeStr);
                getCostCenterName(request, costCenter);
                return mapping.findForward("show" + reportTypeStr);
            }
        }

        return mapping.findForward("index");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        final String costCenter = request.getParameter("costCenter");

        HSSFWorkbook wb = new HSSFWorkbook();
        String fileName = new String("listagem");

        if (reportType.getReportType() != null) {
            if (reportType.equals(ReportType.GENERATED_OVERHEADS)
                    || reportType.equals(ReportType.TRANSFERED_OVERHEADS)
                    || reportType.equals(ReportType.OVERHEADS_SUMMARY)) {
                InfoOverheadReport infoOverheadReport = (InfoOverheadReport) ServiceUtils
                        .executeService(userView, "ReadOverheadReport", new Object[] {
                                userView.getUtilizador(), costCenter, reportType, null });
                infoOverheadReport.getReportToExcel(userView, wb, reportType);
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