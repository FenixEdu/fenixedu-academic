/*
 * Created on Jan 11, 2005
 */

package ServidorApresentacao.Action.projectsManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.projectsManagement.InfoCoordinatorReport;
import DataBeans.projectsManagement.InfoProjectReport;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.projectsManagement.ReportType;
import Util.projectsManagement.RubricType;

/**
 * @author Susana Fernandes
 */
public class ProjectReportsDispatchAction extends FenixDispatchAction {
    private final int numberOfSpanElements = 20;

    public ActionForward prepareShowReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);

        if (reportType.getReportType() != null
                && (reportType.equals(ReportType.EXPENSES) || reportType.equals(ReportType.REVENUE) || reportType.equals(ReportType.CABIMENTOS) || reportType
                        .equals(ReportType.ADIANTAMENTOS))) {
            final List projectList = (List) ServiceUtils.executeService(userView, "ReadUserProjects", new Object[] { userView, new Boolean(true) });

            request.setAttribute("projectList", projectList);
            request.setAttribute("reportType", reportTypeStr);
            return mapping.findForward("projectList");
        }

        return mapping.findForward("index");
    }

    public ActionForward getReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        if (reportType.getReportType() != null) {
            request.setAttribute("reportType", reportTypeStr);
            if (reportType.equals(ReportType.EXPENSES)) {
                return getExpensesReport(mapping, form, request, response);
            } else if (reportType.equals(ReportType.REVENUE) || reportType.equals(ReportType.ADIANTAMENTOS)
                    || reportType.equals(ReportType.CABIMENTOS)) {
                final Integer projectCode = getCodeFromRequest(request, "projectCode");
                final InfoProjectReport infoReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadReport", new Object[] { userView,
                        reportType, projectCode });
                request.setAttribute("infoReport", infoReport);
            } else if (reportType.equals(ReportType.SUMMARY)) {
                final List infoSummaryReportList = (List) ServiceUtils.executeService(userView, "ReadSummaryReport", new Object[] { userView });
                request.setAttribute("infoSummaryReportList", infoSummaryReportList);
            }

            if (request.getParameter("print") != null && request.getParameter("print").equals("true"))
                return mapping.findForward("print" + reportTypeStr);
            return mapping.findForward("show" + reportTypeStr);
        }

        return mapping.findForward("index");
    }

    public ActionForward getExpensesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final Integer projectCode = getCodeFromRequest(request, "projectCode");
        final String rubric = request.getParameter("rubric");

        InfoProjectReport infoExpensesReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadExpensesReport", new Object[] { null,
                userView, ReportType.EXPENSES, projectCode, rubric });

        if (infoExpensesReport != null && infoExpensesReport.getInfoProject() != null) {
            Boolean lastSpan = new Boolean(true);
            int lines = infoExpensesReport.getLines().size();

            if (request.getParameter("print") == null && lines > numberOfSpanElements) {

                Integer span = getCodeFromRequest(request, "span");
                if (span == null)
                    span = new Integer(0);

                int pagesNumber = (int) Math.ceil((double) lines / numberOfSpanElements);
                int startSpan = span.intValue() * numberOfSpanElements;
                int length = numberOfSpanElements;
                if (span.intValue() == (pagesNumber - 1))
                    length = lines % numberOfSpanElements;
                else
                    lastSpan = new Boolean(false);

                request.setAttribute("span", span);
                request.setAttribute("startSpan", new Integer(startSpan));
                request.setAttribute("length", new Integer(length));
                request.setAttribute("numberOfSpanElements", new Integer(numberOfSpanElements));
                request.setAttribute("spanNumber", new Integer(pagesNumber));
            }
            if (lastSpan.booleanValue() && (rubric == null || rubric.equals(""))) {
                infoExpensesReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadExpensesReport", new Object[] {
                        infoExpensesReport, userView, ReportType.EXPENSES, projectCode, rubric });
                int i = 0;
            }
            request.setAttribute("lastSpan", lastSpan);
        }
        request.setAttribute("rubric", rubric);
        request.setAttribute("infoExpensesReport", infoExpensesReport);

        if (request.getParameter("print") != null && request.getParameter("print").equals("true"))
            return mapping.findForward("print" + ReportType.EXPENSES_STRING);
        return mapping.findForward("show" + ReportType.EXPENSES_STRING);
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String reportTypeStr = request.getParameter("reportType");
        final ReportType reportType = new ReportType(reportTypeStr);
        String report = new String();

        HSSFWorkbook wb = new HSSFWorkbook();
        String fileName = new String("listagem");
        InfoProjectReport infoProjectReport = null;
        Integer projectCode = null;
        if (reportType.getReportType() != null) {
            if (!reportType.equals(ReportType.SUMMARY))
                projectCode = getCodeFromRequest(request, "projectCode");
            if (reportType.equals(ReportType.EXPENSES)) {
                infoProjectReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadExpensesReport", new Object[] { null, userView,
                        reportType, projectCode, request.getParameter("rubric") });
                infoProjectReport.getReportToExcel(userView, wb, reportType);
            } else if (reportType.equals(ReportType.SUMMARY)) {
                List infoProjectReportList = (List) ServiceUtils.executeService(userView, "ReadSummaryReport", new Object[] { userView });
                for (int i = 0; i < infoProjectReportList.size(); i++) {
                    ((InfoCoordinatorReport) infoProjectReportList.get(i)).getReportToExcel(userView, wb, reportType);
                }

            } else {
                infoProjectReport = (InfoProjectReport) ServiceUtils.executeService(userView, "ReadReport", new Object[] { userView, reportType,
                        projectCode });
                infoProjectReport.getReportToExcel(userView, wb, reportType);
            }

            fileName = reportType.getReportLabel();
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

    public ActionForward showHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final String helpPage = request.getParameter("helpPage");

        final RubricType rubricType = RubricType.getRubricType(helpPage);
        if (rubricType != null) {
            List infoRubricList = (List) ServiceUtils.executeService(userView, "ReadRubric", new Object[] { rubricType });
            request.setAttribute("infoRubricList", infoRubricList);
        } else if (!helpPage.equals("listHelp"))
            return mapping.findForward("index");

        request.setAttribute("helpPage", helpPage);
        return mapping.findForward("helpPage");
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        try {
            Object objectCode = request.getAttribute(codeString);
            if (objectCode != null) {
                if (objectCode instanceof String)
                    code = new Integer((String) objectCode);
                else if (objectCode instanceof Integer)
                    code = (Integer) objectCode;
            } else {
                String thisCodeString = request.getParameter(codeString);
                if (thisCodeString != null)
                    code = new Integer(thisCodeString);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return code;
    }

}