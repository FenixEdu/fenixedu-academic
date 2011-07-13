package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
 * 
 * @author naat
 * @author pcma
 * 
 */
@Mapping(module = "student", path = "/viewExecutionCourseForuns", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp", tileProperties = @Tile(bodyContext = "/student/forums/context.jsp")),
		@Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp", tileProperties = @Tile(bodyContext = "/student/forums/context.jsp")),
		@Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp", tileProperties = @Tile(bodyContext = "/student/forums/context.jsp")),
		@Forward(name = "viewForuns", path = "/student/forums/viewExecutionCourseForuns.jsp") })
public class ViewExecutionCourseForunsDispatchAction extends ForunsManagement {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException, FenixServiceException {

	SortedSet<Attends> attendsForCurrentExecutionPeriod = new TreeSet<Attends>(
		Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
	attendsForCurrentExecutionPeriod.addAll(getLoggedPerson(request).getCurrentAttends());

	request.setAttribute("attendsForExecutionPeriod", attendsForCurrentExecutionPeriod);

	return mapping.findForward("viewForuns");

    }

}