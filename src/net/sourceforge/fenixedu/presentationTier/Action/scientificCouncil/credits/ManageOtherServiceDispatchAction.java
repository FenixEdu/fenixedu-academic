/**
 *  Apr 21, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ManageOtherServiceDispatchAction extends FenixDispatchAction {

    public ActionForward showOtherServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm otherServiceForm = (DynaActionForm) form;
	Integer teacherId = (Integer) otherServiceForm.get("teacherId");
	Integer executionPeriodId = Integer.valueOf(otherServiceForm.getString("executionPeriodId"));

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);

	if (teacher == null) {
	    return mapping.findForward("teacher-not-found");
	}

	request.setAttribute("executionPeriod", executionSemester);
	request.setAttribute("teacher", teacher);

	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService != null && !teacherService.getOtherServices().isEmpty()) {
	    request.setAttribute("otherServices", teacherService.getOtherServices());
	}
	return mapping.findForward("show-other-services");
    }

    public ActionForward prepareEditOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm otherServiceForm = (DynaActionForm) form;

	ExecutionSemester executionSemester = null;
	Teacher teacher = null;
	Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");
	if (otherServiceID == null || otherServiceID == 0) {
	    Integer teacherID = (Integer) otherServiceForm.get("teacherId");
	    String executionPeriodString = otherServiceForm.getString("executionPeriodId");
	    Integer executionPeriodID = Integer.valueOf(executionPeriodString);
	    teacher = rootDomainObject.readTeacherByOID(teacherID);
	    executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	} else {
	    OtherService otherService = (OtherService) rootDomainObject.readTeacherServiceItemByOID(otherServiceID);
	    otherServiceForm.set("credits", String.valueOf(otherService.getCredits()));
	    otherServiceForm.set("reason", otherService.getReason());

	    teacher = otherService.getTeacherService().getTeacher();
	    executionSemester = otherService.getTeacherService().getExecutionPeriod();
	}

	request.setAttribute("teacher", teacher);
	request.setAttribute("executionPeriod", executionSemester);
	return mapping.findForward("edit-other-service");
    }

    public ActionForward editOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm otherServiceForm = (DynaActionForm) form;

	Double credits = null;
	try {
	    credits = Double.valueOf(otherServiceForm.getString("credits"));
	} catch (NumberFormatException e) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("", new ActionMessage("error.invalid.credits"));
	    saveMessages(request, actionMessages);
	    return prepareEditOtherService(mapping, form, request, response);
	}

	String reason = otherServiceForm.getString("reason");
	Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");

	if (otherServiceID == null || otherServiceID == 0) {
	    Integer teacherID = (Integer) otherServiceForm.get("teacherId");
	    Integer executionPeriodID = Integer.valueOf((String) otherServiceForm.get("executionPeriodId"));
	    Object[] args = { teacherID, executionPeriodID, credits, reason };
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateOtherService", args);
	} else {
	    Object[] args = { otherServiceID, credits, reason };
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "EditOtherService", args);
	}

	return mapping.findForward("successful-edit");
    }

    public ActionForward deleteOtherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm otherServiceForm = (DynaActionForm) form;
	Integer otherServiceID = (Integer) otherServiceForm.get("otherServiceID");
	ServiceUtils
		.executeService(SessionUtils.getUserView(request), "DeleteOtherServiceByOID", new Object[] { otherServiceID });
	return mapping.findForward("successful-delete");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws NumberFormatException, FenixFilterException, FenixServiceException {

	return showOtherServices(mapping, form, request, response);
    }
}
