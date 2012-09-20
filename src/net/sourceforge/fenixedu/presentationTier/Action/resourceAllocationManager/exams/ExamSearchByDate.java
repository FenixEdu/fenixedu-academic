/*
 * Created on 5/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadExamsByDate;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExam;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Ana e Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/ExamSearchByDate", input = "/ExamSearchByDate.do?method=prepare&page=0", attribute = "examSearchByDateForm", formBean = "examSearchByDateForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show", path = "df.page.showExamsByDate"),
		@Forward(name = "choose", path = "df.page.examSearchByDate") })
public class ExamSearchByDate extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final String executionPeriodString = (String) request.getParameter(PresentationConstants.EXECUTION_PERIOD_OID);
	request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, executionPeriodString);
	return mapping.findForward("choose");
    }

    public ActionForward prepareAfterEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm formSearch = (DynaActionForm) form;

	String strDate = (String) request.getAttribute(PresentationConstants.DATE);
	Calendar date = Calendar.getInstance();
	date.setTimeInMillis(new Long(strDate).longValue());
	formSearch.set("day", new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
	formSearch.set("month", new Integer(date.get(Calendar.MONTH) + 1).toString());
	formSearch.set("year", new Integer(date.get(Calendar.YEAR)).toString());

	String strStartTime = (String) request.getAttribute(PresentationConstants.START_TIME);
	if (strStartTime != null && !strStartTime.equals("null")) {
	    Calendar startTime = Calendar.getInstance();
	    startTime.setTimeInMillis(new Long(strStartTime).longValue());
	    formSearch.set("beginningHour", new Integer(startTime.get(Calendar.HOUR_OF_DAY)).toString());
	    formSearch.set("beginningMinute", new Integer(startTime.get(Calendar.MINUTE)).toString());
	} else {
	    formSearch.set("beginningHour", null);
	    formSearch.set("beginningMinute", null);
	}

	String strEndTime = (String) request.getAttribute(PresentationConstants.END_TIME);
	if (strEndTime != null && !strEndTime.equals("null")) {
	    Calendar endTime = Calendar.getInstance();
	    endTime.setTimeInMillis(new Long(strEndTime).longValue());
	    formSearch.set("endHour", new Integer(endTime.get(Calendar.HOUR_OF_DAY)).toString());
	    formSearch.set("endMinute", new Integer(endTime.get(Calendar.MINUTE)).toString());
	} else {
	    formSearch.set("endHour", null);
	    formSearch.set("endMinute", null);
	}

	return search(mapping, form, request, response);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm examSearchByDateForm = (DynaActionForm) form;
	IUserView userView = UserView.getUser();

	// exam date
	Calendar examDate = Calendar.getInstance();
	Integer day = new Integer((String) examSearchByDateForm.get("day"));
	Integer month = new Integer((String) examSearchByDateForm.get("month"));
	Integer year = new Integer((String) examSearchByDateForm.get("year"));
	examDate.set(Calendar.YEAR, year.intValue());
	examDate.set(Calendar.MONTH, month.intValue() - 1);
	examDate.set(Calendar.DAY_OF_MONTH, day.intValue());
	if (examDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
	    ActionError actionError = new ActionError("error.sunday");
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("error.sunday", actionError);
	    saveErrors(request, actionErrors);
	    return prepare(mapping, form, request, response);
	}

	// exam start time
	Calendar examStartTime = Calendar.getInstance();
	try {
	    Integer startHour = new Integer((String) examSearchByDateForm.get("beginningHour"));
	    Integer startMinute = new Integer((String) examSearchByDateForm.get("beginningMinute"));
	    examStartTime.set(Calendar.HOUR_OF_DAY, startHour.intValue());
	    examStartTime.set(Calendar.MINUTE, startMinute.intValue());
	    examStartTime.set(Calendar.SECOND, 0);
	} catch (NumberFormatException ex) {
	    examStartTime = null;
	}

	// exam end time
	Calendar examEndTime = Calendar.getInstance();
	try {
	    Integer endHour = new Integer((String) examSearchByDateForm.get("endHour"));
	    Integer endMinute = new Integer((String) examSearchByDateForm.get("endMinute"));
	    examEndTime.set(Calendar.HOUR_OF_DAY, endHour.intValue());
	    examEndTime.set(Calendar.MINUTE, endMinute.intValue());
	    examEndTime.set(Calendar.SECOND, 0);
	} catch (Exception e) {
	    examEndTime = null;
	}

	if (examStartTime != null && examEndTime != null && examStartTime.after(examEndTime)) {
	    ActionError actionError = new ActionError("error.timeSwitched");
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("error.timeSwitched", actionError);
	    saveErrors(request, actionErrors);
	    return prepare(mapping, form, request, response);
	}

	String examDateString = " Exames de dia " + new Integer(examDate.get(Calendar.DAY_OF_MONTH)) + "/"
		+ new Integer(examDate.get(Calendar.MONTH) + 1).toString() + "/" + new Integer(examDate.get(Calendar.YEAR));

	// String examStartTimeString = "";
	if (examStartTime != null) {
	    examDateString += " a começar às " + new Integer(examStartTime.get(Calendar.HOUR_OF_DAY)) + ":"
		    + new Integer(examStartTime.get(Calendar.MINUTE));
	}
	// String examEndTimeString = "";
	if (examEndTime != null && examStartTime != null) {
	    examDateString += " e";
	}
	if (examEndTime != null) {
	    examDateString += " a terminar às " + new Integer(examEndTime.get(Calendar.HOUR_OF_DAY)) + ":"
		    + new Integer(examEndTime.get(Calendar.MINUTE));
	}

	InfoViewExam infoViewExam = (InfoViewExam) ReadExamsByDate.run(examDate, examStartTime, examEndTime);

	if (infoViewExam.getInfoViewExamsByDayAndShift().size() != 0) {
	    request.setAttribute(PresentationConstants.LIST_EXAMSANDINFO, infoViewExam.getInfoViewExamsByDayAndShift());
	}
	request.setAttribute(PresentationConstants.EXAM_DATEANDTIME_STR, examDateString);

	Long date = new Long(examDate.getTimeInMillis());
	request.setAttribute(PresentationConstants.DATE, date.toString());
	if (examStartTime != null) {
	    Long sTime = new Long(examStartTime.getTimeInMillis());
	    request.setAttribute(PresentationConstants.START_TIME, sTime.toString());
	}
	if (examEndTime != null) {
	    Long eTime = new Long(examEndTime.getTimeInMillis());
	    request.setAttribute(PresentationConstants.END_TIME, eTime.toString());
	}

	return mapping.findForward("show");
    }

}