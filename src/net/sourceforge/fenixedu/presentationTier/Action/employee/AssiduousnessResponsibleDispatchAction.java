package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.UnitEmployees;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class AssiduousnessResponsibleDispatchAction extends FenixDispatchAction {

    public ActionForward showEmployeeList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        YearMonth yearMonth = null;
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        if (viewState != null) {
            yearMonth = (YearMonth) viewState.getMetaObject().getObject();
        }
        if (yearMonth == null) {
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
        } else if (yearMonth.getYear() > new YearMonthDay().getYear()
                || (yearMonth.getYear() == new YearMonthDay().getYear() && yearMonth.getMonth()
                        .compareTo(Month.values()[new YearMonthDay().getMonthOfYear() - 1]) > 0)) {
            saveErrors(request, "error.invalidFutureDate");
            yearMonth = new YearMonth();
            yearMonth.setYear(new YearMonthDay().getYear());
            yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
            request.setAttribute("yearMonth", yearMonth);
            return mapping.getInputForward();
        } else if (yearMonth.getYear() < 2006) {
            saveErrors(request, "error.invalidPastDate");
            request.setAttribute("yearMonth", yearMonth);
            return mapping.getInputForward();
        }

        YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
                yearMonth.getMonth().ordinal() + 1, 01);
        YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
                beginDate.dayOfMonth().getMaximumValue());

        request.setAttribute("yearMonth", yearMonth);
        List<UnitEmployees> unitEmployeesList = new ArrayList<UnitEmployees>();
        for (PersonFunction personFunction : userView.getPerson().getPersonFuntions(beginDate, endDate)) {
            if (personFunction.getFunction().getFunctionType() == FunctionType.ASSIDUOUSNESS_RESPONSIBLE) {
                // tenho acesso as pessoas numa nidade.... mas a função pode ter outro prazo!
                List<Employee> employeeList = personFunction.getFunction().getUnit()
                        .getAllWorkingEmployees(beginDate, endDate);
                if (!employeeList.isEmpty()) {
                    UnitEmployees unitEmployees = new UnitEmployees();
                    unitEmployees.setUnit(personFunction.getFunction().getUnit());
                    unitEmployees.setEmployeeList(employeeList);
                    Collections.sort(unitEmployees.getEmployeeList(), new BeanComparator(
                            "employeeNumber"));
                    unitEmployeesList.add(unitEmployees);
                }
                for (Unit unit : personFunction.getFunction().getUnit().getAllActiveSubUnits(
                        new YearMonthDay())) {
                    employeeList = unit.getAllWorkingEmployees(beginDate, endDate);
                    if (!employeeList.isEmpty()) {
                        UnitEmployees unitEmployees = new UnitEmployees();
                        unitEmployees.setUnit(unit);
                        unitEmployees.setEmployeeList(employeeList);
                        Collections.sort(unitEmployees.getEmployeeList(), new BeanComparator(
                                "employeeNumber"));
                        unitEmployeesList.add(unitEmployees);
                    }
                }
            }
        }
        Collections.sort(unitEmployeesList, new BeanComparator("unit.name"));
        request.setAttribute("unitEmployeesList", unitEmployeesList);
        return mapping.findForward("show-employee-list");
    }

    public ActionForward showEmployeeWorkSheet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        final IUserView userView = SessionUtils.getUserView(request);
        Integer employeeNumber = new Integer(request.getParameter("employeeNumber"));
        Employee employee = Employee.readByNumber(employeeNumber);
        if (employee == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        YearMonth yearMonth = new YearMonth();
        yearMonth.setYear(new Integer(year));
        yearMonth.setMonth(Month.valueOf(month));
        if (yearMonth.getYear() > new YearMonthDay().getYear()
                || (yearMonth.getYear() == new YearMonthDay().getYear() && yearMonth.getMonth()
                        .compareTo(Month.values()[new YearMonthDay().getMonthOfYear() - 1]) > 0)) {
            saveErrors(request, "error.invalidFutureDate");
            EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
            employeeWorkSheet.setEmployee(employee);
            request.setAttribute("employeeWorkSheet", employeeWorkSheet);
            return mapping.findForward("show-employee-work-sheet");
        } else if (yearMonth.getYear() < 2006) {
            saveErrors(request, "error.invalidPastDate");
            EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
            employeeWorkSheet.setEmployee(employee);
            request.setAttribute("employeeWorkSheet", employeeWorkSheet);
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
        EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
        employeeWorkSheet.setEmployee(employee);
        if (employee.getAssiduousness() != null) {
            try {
                Object[] args = { employee.getAssiduousness(), beginDate, endDate };
                employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(userView,
                        "ReadAssiduousnessResponsibleWorkSheet", args);
                request.setAttribute("employeeWorkSheet", employeeWorkSheet);
            } catch (NotAuthorizedFilterException e) {
                saveErrors(request, "error.notAuthorized");
                return mapping.findForward("show-employee-work-sheet");
            }
        }
        request.setAttribute("employeeWorkSheet", employeeWorkSheet);
        request.setAttribute("yearMonth", yearMonth);
        return mapping.findForward("show-employee-work-sheet");
    }

    private void saveErrors(HttpServletRequest request, String message) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("message", new ActionMessage(message));
        saveMessages(request, actionMessages);
    }
}
