/*
 * Created on 2003/12/25
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;

/**
 * @author Luis Cruz
 */
public class MonitorSystemDA extends FenixDispatchAction {

    public ActionForward monitor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = UserView.getUser();

	SystemInfo systemInfoApplicationServer = ServiceManagerServiceFactory.getSystemInfo(userView);
	request.setAttribute("systemInfoApplicationServer", systemInfoApplicationServer);

	SystemInfo systemInfoWebContainer = ServiceManagerServiceFactory.getSystemInfo(userView);
	request.setAttribute("systemInfoWebContainer", systemInfoWebContainer);

	request.setAttribute("startMillis", ""
		+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getStartMillis());
	request.setAttribute("endMillis", ""
		+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getEndMillis());
	request.setAttribute("chronology", ""
		+ ExecutionSemester.readActualExecutionSemester().getAcademicInterval().getChronology().toString());

	return mapping.findForward("Show");
    }

    public ActionForward mock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final Person person = getDomainObject(request, "personID");

	final String requestURL = request.getRequestURL().toString();

	IUserView currentUser = UserView.getUser();

	if (currentUser == null || !currentUser.hasRoleType(RoleType.MANAGER)) {
	    throw new Error("not.authorized");
	}

	final IUserView userView = new Authenticate().mock(person, requestURL);

	UserView.setUser(userView);

	request.getSession().setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);

	final ActionForward forward = new ActionForward("/home.do", true);
	forward.setModule("/");
	return forward;
    }

}