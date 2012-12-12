package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author naat
 * @author pcma
 */
@Mapping(module = "teacher", path = "/executionCourseForumManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewForum", path = "view-forum"), @Forward(name = "viewThread", path = "view-thread"),
	@Forward(name = "createThreadAndMessage", path = "create-thread-and-message"),
	@Forward(name = "viewForuns", path = "view-foruns") })
public class ExecutionCourseForumManagementDispatchAction extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ManageExecutionCourseDA.propageContextIds(request);
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward viewForuns(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

	request.setAttribute("foruns", executionCourse.getForuns());

	return mapping.findForward("viewForuns");
    }

}