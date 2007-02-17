package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class ViewAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward showSchedules(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
	for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
	    workScheduleList.add(workScheduleType);
	}
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("ojbConcreteClass"));
	comparatorChain.addComparator(new BeanComparator("acronym"));
	Collections.sort(workScheduleList, comparatorChain);
	request.setAttribute("workScheduleList", workScheduleList);
	return mapping.findForward("show-all-schedules");
    }

    public ActionForward showJustificationMotives(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	List<JustificationMotive> justificationMotives = new ArrayList<JustificationMotive>();
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationType() != null) {
		justificationMotives.add(justificationMotive);
	    }
	}
	Collections.sort(justificationMotives, new BeanComparator("justificationType"));
	request.setAttribute("justificationMotives", justificationMotives);
	return mapping.findForward("show-justification-motives");
    }

    public ActionForward showRegularizationMotives(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	List<JustificationMotive> regularizationMotives = new ArrayList<JustificationMotive>();
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationType() == null) {
		regularizationMotives.add(justificationMotive);
	    }
	}
	Collections.sort(regularizationMotives, new BeanComparator("acronym"));
	request.setAttribute("regularizationMotives", regularizationMotives);
	return mapping.findForward("show-regularization-motives");
    }

    public ActionForward showBalances(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	YearMonth yearMonth = getYearMonth(request);
	if (yearMonth == null) {
	    request.setAttribute("yearMonth", new YearMonth(new YearMonthDay()));
	    return mapping.findForward("show-balances");
	}
	request.setAttribute("yearMonth", yearMonth);
	YearMonthDay now = new YearMonthDay();
	if (yearMonth.getYear() < 2006) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidPastDateNoData"));
	    saveMessages(request, actionMessages);
	    return mapping.findForward("show-balances");
	} else if (yearMonth.getYear() > now.getYear()
		|| (yearMonth.getYear() == now.getYear() && yearMonth.getNumberOfMonth() > now
			.getMonthOfYear())) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidFutureDate"));
	    saveMessages(request, actionMessages);
	    return mapping.findForward("show-balances");
	}

	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
		yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	if (yearMonth.getYear() == new YearMonthDay().getYear()
		&& yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
	    endDay = new YearMonthDay().getDayOfMonth();
	    request.setAttribute("displayCurrentDayNote", "true");
	}
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
		endDay);
	Object[] args = { beginDate, endDate };
	List<EmployeeWorkSheet> employeeWorkSheetList = (List<EmployeeWorkSheet>) ServiceUtils
		.executeService(SessionUtils.getUserView(request), "ReadMonthBalances", args);
	Collections.sort(employeeWorkSheetList, new BeanComparator("employee.employeeNumber"));
	request.setAttribute("employeeWorkSheetList", employeeWorkSheetList);
	return mapping.findForward("show-balances");
    }

    private YearMonth getYearMonth(HttpServletRequest request) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = (YearMonth) request.getAttribute("yearMonth");
	}
	if (yearMonth == null) {
	    String year = request.getParameter("year");
	    String month = request.getParameter("month");
	    if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(month)) {
		yearMonth = new YearMonth();
		yearMonth.setYear(new Integer(year));
		yearMonth.setMonth(Month.valueOf(month));
	    }
	}
	return yearMonth;
    }
}