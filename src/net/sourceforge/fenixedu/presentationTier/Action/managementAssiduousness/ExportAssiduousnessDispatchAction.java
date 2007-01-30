package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

public class ExportAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward chooseYearMonth(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        YearMonth yearMonth = new YearMonth();
        yearMonth.setYear(new YearMonthDay().getYear());
        yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        request.setAttribute("yearMonth", yearMonth);
        request.setAttribute("actionPath", mapping.getPath());
        request.setAttribute("action", request.getParameter("action"));
        return mapping.findForward("choose-year-month");
    }

    public ActionForward exportToPDFWorkDaySheet(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else if (yearMonth.getYear() > new YearMonthDay().getYear()) {
            // erro - não pode ver nos anos futuros
            System.out.println("erro - não pode ver nos anos futuros");
        }
        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        if (yearMonth.getYear() == new YearMonthDay().getYear()
                && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
            endDay = new YearMonthDay().getDayOfMonth();
        }

        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);

        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                LanguageUtils.getLocale());
        final IUserView userView = SessionUtils.getUserView(request);
        List<EmployeeWorkSheet> employeeWorkSheetList = null;
        Map parameters = new HashMap();
        ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources",
                LanguageUtils.getLocale());
        String month = bundleEnumeration.getString(yearMonth.getMonth().toString());
        StringBuilder stringBuilder = new StringBuilder(month).append(" ").append(yearMonth.getYear());

        parameters.put("yearMonth", stringBuilder.toString());
        String path = getServlet().getServletContext().getRealPath("/");
        parameters.put("path", path);

        Object[] args = { beginDate, endDate };
        employeeWorkSheetList = (List<EmployeeWorkSheet>) ServiceUtils.executeService(userView,
                "ReadAllAssiduousnessWorkSheets", args);

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("unitCode"));
        comparatorChain.addComparator(new BeanComparator("employee.employeeNumber"));
        Collections.sort(employeeWorkSheetList, comparatorChain);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=verbetes.pdf");

        byte[] data = ReportsUtils.exportToPdf("assiduousness.workDaySheet", parameters, bundle,
                employeeWorkSheetList);
        response.setContentLength(data.length);
        ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();
        response.flushBuffer();

        return mapping.findForward("");
    }
}
