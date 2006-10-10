package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

public class ViewAssiduousnessDispatchAction extends FenixDispatchAction {
    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        String action = request.getParameter("action");
        request.setAttribute("action", action);
        return mapping.findForward("choose-employee");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        final Integer employeeNumber = new Integer(((DynaActionForm) form).getString("employeeNumber"));

        Employee employee = Employee.readByNumber(employeeNumber);
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else if (yearMonth.getYear() < 2006) {            
            saveErrors(request, employee, yearMonth,"error.invalidPastDateNoData");            
            return mapping.findForward("show-employee-work-sheet");
        }

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        int endDay = beginDate.dayOfMonth().getMaximumValue();
        if (yearMonth.getYear() == new YearMonthDay().getYear()
                && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
            endDay = new YearMonthDay().getDayOfMonth();
            request.setAttribute("displayCurrentDayNote","true");
        }

        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                endDay);

        Object[] args = { employee.getAssiduousness(), beginDate, endDate };
        EmployeeWorkSheet employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(userView,
                "ReadEmployeeWorkSheet", args);
        request.setAttribute("employeeWorkSheet", employeeWorkSheet);

        request.setAttribute("yearMonth", yearMonth);
        return mapping.findForward("show-employee-work-sheet");
    }

    private void saveErrors(HttpServletRequest request, Employee employee, YearMonth yearMonth, String message) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("message", new ActionMessage(message));
        saveMessages(request, actionMessages);
        request.setAttribute("yearMonth", yearMonth);
        EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
        employeeWorkSheet.setEmployee(employee);
        request.setAttribute("employeeWorkSheet", employeeWorkSheet);
    }
}