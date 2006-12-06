package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeAnulateJustificationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeJustificationFactoryCreator;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.EmployeeJustificationFactoryEditor;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.file.FileManagerException;

public class EmployeeAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateEmployeeJustification(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        Employee employee = Employee.readByNumber(new Integer(getFromRequest(request, "employeeNumber")
                .toString()));
        String dateString = request.getParameter("date");
        YearMonthDay date = null;
        if (!StringUtils.isEmpty(dateString)) {
            date = new YearMonthDay(dateString);
        }
        EmployeeJustificationFactoryCreator employeeJustificationFactory = new EmployeeJustificationFactoryCreator(
                employee, date, EmployeeJustificationFactory.CorrectionType.valueOf(request
                        .getParameter("correction")));
        request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
        request.setAttribute("yearMonth", getYearMonth(request, date));
        if (StringUtils.isEmpty(dateString)) {
            return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form,
                    request, response);
        }
        return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, form, request,
                response);
    }

    public ActionForward prepareEditEmployeeJustification(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        Justification justification = (Justification) rootDomainObject
                .readAssiduousnessRecordByOID(new Integer(getFromRequest(request, "idInternal")
                        .toString()));
        EmployeeJustificationFactoryEditor employeeJustificationFactory = new EmployeeJustificationFactoryEditor(
                justification);
        request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
        request.setAttribute("yearMonth", getYearMonth(request, null));
        request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
                .getEmployeeNumber());
        return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
                response);
    }

    public ActionForward chooseJustificationMotivePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) getFactoryObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("yearMonth", getYearMonth(request, employeeJustificationFactory.getDate()));
        request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
                .getEmployeeNumber());
        request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
        if (employeeJustificationFactory.getDate() == null) {
            return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, actionForm,
                    request, response);
        }
        return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, actionForm, request,
                response);
    }

    public ActionForward editEmployeeJustification(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) getFactoryObject();
        try {
            Object result = executeService(request, "ExecuteFactoryMethod",
                    new Object[] { employeeJustificationFactory });
            if (result != null) {
                setError(request, "errorMessage", (ActionMessage) result);
                request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
                return chooseJustificationMotivePostBack(mapping, form, request, response);
            }
        } catch (FileManagerException ex) {
            setError(request, "errorMessage", (ActionMessage) new ActionMessage(ex.getKey(), ex
                    .getArgs()));
            RenderUtils.invalidateViewState();
            request.setAttribute("employeeJustificationFactory", employeeJustificationFactory);
            return chooseJustificationMotivePostBack(mapping, form, request, response);
        }
        request.setAttribute("employeeNumber", employeeJustificationFactory.getEmployee()
                .getEmployeeNumber());
        YearMonth yearMonth = getYearMonth(request, employeeJustificationFactory.getDate());
        request.setAttribute("yearMonth", yearMonth);
        if (employeeJustificationFactory.getDate() == null) {
            return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form,
                    request, response);
        }
        return new ViewEmployeeAssiduousnessDispatchAction().showWorkSheet(mapping, form, request,
                response);
    }

    public ActionForward deleteEmployeeJustification(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        Justification justification = (Justification) rootDomainObject
                .readAssiduousnessRecordByOID(new Integer(getFromRequest(request, "idInternal")
                        .toString()));
        Employee modifiedBy = getUserView(request).getPerson().getEmployee();

        EmployeeAnulateJustificationFactory employeeAnulateJustificationFactory = new EmployeeAnulateJustificationFactory(
                justification, modifiedBy);

        request.setAttribute("employeeNumber", justification.getAssiduousness().getEmployee()
                .getEmployeeNumber());
        request.setAttribute("yearMonth", getYearMonth(request, null));
        try {
            Object result = executeService(request, "ExecuteFactoryMethod",
                    new Object[] { employeeAnulateJustificationFactory });
            if (result != null) {
                setError(request, "errorMessage", (ActionMessage) result);
                request
                        .setAttribute("employeeJustificationFactory",
                                employeeAnulateJustificationFactory);
                return chooseJustificationMotivePostBack(mapping, form, request, response);
            }
        } catch (FileManagerException ex) {
            setError(request, "errorMessage", (ActionMessage) new ActionMessage(ex.getKey(), ex
                    .getArgs()));
            RenderUtils.invalidateViewState();
        }

        return new ViewEmployeeAssiduousnessDispatchAction().showJustifications(mapping, form, request,
                response);
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }

    private YearMonth getYearMonth(HttpServletRequest request, YearMonthDay date) {
        YearMonth yearMonth = (YearMonth) getRendererObject("yearMonth");
        if (date == null) {
            if (yearMonth == null) {
                yearMonth = new YearMonth();

                String year = request.getParameter("year");
                String month = request.getParameter("month");

                if (StringUtils.isEmpty(year)) {
                    yearMonth.setYear(new YearMonthDay().getYear());
                } else {
                    yearMonth.setYear(new Integer(year));
                }
                if (StringUtils.isEmpty(month)) {
                    yearMonth.setMonth(Month.values()[new YearMonthDay().getMonthOfYear() - 1]);
                } else {
                    yearMonth.setMonth(Month.valueOf(month));
                }
            }
        } else {
            yearMonth = new YearMonth(date);
        }

        if (yearMonth.getYear() < 2006) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message", new ActionMessage("error.invalidPastDateNoData"));
            saveMessages(request, actionMessages);
            request.setAttribute("yearMonth", yearMonth);
            return null;
        }
        return yearMonth;
    }
}