package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.UnableToPrintServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class PrintMarkSheetDispatchAction extends MarkSheetDispatchAction {

    public ActionForward searchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("searchMarkSheet");
    }

    public ActionForward choosePrinterMarkSheetsWeb(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response, ExecutionSemester.readActualExecutionPeriod());
    }

    public ActionForward choosePrinterMarkSheetsWebPostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	DynaActionForm form = (DynaActionForm) actionForm;
	final Integer ecID = Integer.valueOf(form.getString("ecID"));
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(ecID);

	return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response, executionSemester);
    }

    private ActionForward choosePrinterMarkSheetsWeb(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, ExecutionSemester executionSemester) {
	DynaActionForm form = (DynaActionForm) actionForm;
	String[] printerNames = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace()
		.getPrinterNamesByFunctionalityName("markSheet");
	request.setAttribute("printerNames", Arrays.asList(printerNames));

	Collection<MarkSheet> webMarkSheetsNotPrinted = executionSemester
		.getWebMarkSheetsNotPrintedByAdministraticeOfficeAndCampus(AccessControl.getPerson().getEmployee()
			.getAdministrativeOffice(), AccessControl.getPerson().getEmployee().getCurrentCampus());

	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("curricularCourseMap", buildMapWithCurricularCoursesAndNumberOfMarkSheets(webMarkSheetsNotPrinted));
	request.setAttribute("totalMarkSheetsCount", webMarkSheetsNotPrinted.size());

	List<ExecutionSemester> notClosedExecutionPeriods = ExecutionSemester.readNotClosedExecutionPeriods();
	Collections.sort(notClosedExecutionPeriods, new ReverseComparator(
		ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR));
	List<LabelValueBean> periods = new ArrayList<LabelValueBean>();
	for (ExecutionSemester period : notClosedExecutionPeriods) {
	    periods.add(new LabelValueBean(period.getExecutionYear().getYear() + " - " + period.getName(), period.getIdInternal()
		    .toString()));
	}

	request.setAttribute("periods", periods);
	form.set("ecID", executionSemester.getIdInternal().toString());

	return mapping.findForward("choosePrinterMarkSheetsWeb");
    }

    private Map<CurricularCourse, Integer> buildMapWithCurricularCoursesAndNumberOfMarkSheets(
	    Collection<MarkSheet> webMarkSheetsNotPrinted) {
	final Map<CurricularCourse, Integer> result = new TreeMap<CurricularCourse, Integer>(new Comparator<CurricularCourse>() {
	    public int compare(CurricularCourse o1, CurricularCourse o2) {
		return o1.getDegreeCurricularPlan().getDegree().getName().compareTo(
			o2.getDegreeCurricularPlan().getDegree().getName());
	    }
	});
	for (final MarkSheet markSheet : webMarkSheetsNotPrinted) {
	    Integer markSheetNumber = result.get(markSheet.getCurricularCourse());
	    result.put(markSheet.getCurricularCourse(), (markSheetNumber == null) ? Integer.valueOf(1) : Integer
		    .valueOf(markSheetNumber.intValue() + 1));
	}
	return result;
    }

    public ActionForward choosePrinterMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	DynaActionForm form = (DynaActionForm) actionForm;
	String[] printerNames = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace()
		.getPrinterNamesByFunctionalityName("markSheet");

	request.setAttribute("printerNames", Arrays.asList(printerNames));
	if (form.get("markSheet") == null || form.getString("markSheet").length() == 0) {
	    form.set("markSheet", request.getAttribute("markSheet"));
	}
	return mapping.findForward("choosePrinterMarkSheet");
    }

    public ActionForward printMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm form = (DynaActionForm) actionForm;
	String markSheet = form.getString("markSheet");
	if (markSheet.equals("all")) {
	    return printWebMarkSheets(mapping, actionForm, request, response);
	} else {
	    return printMarkSheet(mapping, actionForm, request, response);
	}
    }

    private ActionForward printMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm form = (DynaActionForm) actionForm;
	String printerName = form.getString("printerName");
	String markSheetString = form.getString("markSheet");
	MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(Integer.valueOf(markSheetString));
	ActionMessages actionMessages = new ActionMessages();
	try {
	    ServiceUtils.executeService(getUserView(request), "PrintMarkSheet", new Object[] { markSheet, printerName });
	} catch (NotAuthorizedException e) {
	    addMessage(request, actionMessages, "error.notAuthorized");
	    return mapping.getInputForward();
	} catch (UnableToPrintServiceException e) {
	    request.setAttribute("markSheet", markSheetString);
	    addMessage(request, actionMessages, e.getMessage());
	    return choosePrinterMarkSheet(mapping, actionForm, request, response);
	}
	return mapping.findForward("searchMarkSheetFilled");
    }

    private ActionForward printWebMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm form = (DynaActionForm) actionForm;
	String printerName = form.getString("printerName");
	final Integer ecID = Integer.valueOf(form.getString("ecID"));
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(ecID);
	ActionMessages actionMessages = new ActionMessages();
	try {
	    ServiceUtils.executeService(getUserView(request), "PrintMarkSheets",
		    new Object[] {
			    executionSemester.getWebMarkSheetsNotPrintedByAdministraticeOfficeAndCampus(AccessControl.getPerson()
				    .getEmployee().getAdministrativeOffice(), AccessControl.getPerson().getEmployee()
				    .getCurrentCampus()), printerName });
	} catch (NotAuthorizedException e) {
	    addMessage(request, actionMessages, "error.notAuthorized");
	    return mapping.getInputForward();
	} catch (UnableToPrintServiceException e) {
	    addMessage(request, actionMessages, e.getMessage());
	    return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response);
	}
	return mapping.getInputForward();
    }

}
