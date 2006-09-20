/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListPayedInsurancesDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        List<ExecutionYear> executionYears = ExecutionYear.readNotClosedExecutionYears();
        Collections.sort(executionYears, new BeanComparator("year"));
        request.setAttribute("executionYears", executionYears);

        request.setAttribute("days", Data.getMonthDays());
        request.setAttribute("months", Data.getMonthsStartingInOne());
        request.setAttribute("years", Data.getCustomYears(executionYears.get(0)
                .getBeginDateYearMonthDay().getYear(), Calendar.getInstance().get(Calendar.YEAR)));

        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("executionYearID", ExecutionYear.readCurrentExecutionYear().getIdInternal());
        
        return mapping.findForward("chooseDates");
    }

    public ActionForward generateList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, IOException {

        DynaActionForm actionForm = (DynaActionForm) form;

        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(
                (Integer) actionForm.get("executionYearID"));

        Integer endDateDay = (Integer) actionForm.get("endDateDay");
        Integer endDateMonth = (Integer) actionForm.get("endDateMonth");
        Integer endDateYear = (Integer) actionForm.get("endDateYear");
        Integer beginDateDay = (Integer) actionForm.get("beginDateDay");
        Integer beginDateMonth = (Integer) actionForm.get("beginDateMonth");
        Integer beginDateYear = (Integer) actionForm.get("beginDateYear");

        YearMonthDay endDate = (endDateDay > 0 && endDateMonth > 0 && endDateYear > 0) ? new YearMonthDay(
                endDateYear, endDateMonth, endDateDay)
                : null;
        YearMonthDay beginDate = (beginDateDay > 0 && beginDateMonth > 0 && beginDateYear > 0) ? new YearMonthDay(
                beginDateYear, beginDateMonth, beginDateDay)
                : null;

        Object[] args = { executionYear, beginDate, endDate };
        List<InsuranceTransaction> payedInsurances = (List<InsuranceTransaction>) ServiceUtils
                .executeService(SessionUtils.getUserView(request), "ListPayedInsurancesByDates", args);

        ResourceBundle rb = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
        String functionalityName = rb
                .getString("link.masterDegree.administrativeOffice.gratuity.listPayedInsurances");

        String filename = functionalityName.replaceAll(" ", "") + "_"
                + DateFormatUtil.format("dd-MM-yyyy", Calendar.getInstance().getTime());
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

        ServletOutputStream writer = response.getOutputStream();

        final List<Object> headers = getHeaders(rb);
        final Spreadsheet spreadsheet = new Spreadsheet(functionalityName, headers);
        fillSpreadSheet(payedInsurances, spreadsheet);
        spreadsheet.exportToXLSSheet(writer);

        writer.flush();
        response.flushBuffer();

        return null;
    }

    private void fillSpreadSheet(List<InsuranceTransaction> payedInsurances, Spreadsheet spreadsheet) {
        for (InsuranceTransaction insuranceTransaction : payedInsurances) {
            final Row row = spreadsheet.addRow();
            row.setCell(insuranceTransaction.getStudent().getNumber().toString());
            row.setCell(insuranceTransaction.getStudent().getPerson().getName());
            String studentCurricularPlans = "";
            for (StudentCurricularPlan studentCurricularPlan : insuranceTransaction.getStudent()
                    .getStudentCurricularPlans()) {
                studentCurricularPlans += studentCurricularPlan.getDegreeCurricularPlan().getName()
                        + " ";
            }
            row.setCell(studentCurricularPlans);
            row.setCell(insuranceTransaction.getExecutionYear().getYear());
            row.setCell(DateFormatUtil.format("dd-MM-yyyy", insuranceTransaction
                    .getTransactionDateDateTime().toDate()));
        }
    }

    private List<Object> getHeaders(ResourceBundle rb) {
        final List<Object> headers = new ArrayList<Object>();
        
        headers.add(rb.getString("label.number"));
        headers.add(rb.getString("label.name"));
        headers.add(rb.getString("label.masterDegree.administrativeOffice.curricularPlans"));
        headers.add(rb.getString("label.executionYear"));
        headers.add(rb.getString("label.masterDegree.administrativeOffice.paymentDate"));
        
        return headers;
    }

}
