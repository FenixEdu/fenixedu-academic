/*
 * Created on 8/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadExecutionCourseToAssociateCurricularCoursesAction extends FenixAction {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

	HttpSession session = request.getSession(false);

	//		IUserView userView = SessionUtils.getUserView(request);
	UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
	Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

	Object args[] = { curricularCourseId };

	InfoCurricularCourse infoCurricularCourse = null;

	try {
		infoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService(userView, "ReadCurricularCourse", args);

	} catch (NonExistingServiceException e) {
		throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
	} catch (FenixServiceException fenixServiceException) {
		throw new FenixActionException(fenixServiceException.getMessage());
	}

	if (infoCurricularCourse.getBasic().booleanValue())
		request.setAttribute("basic", "");
	return mapping.findForward("viewExecutionPeriods");
	}
}