/*
 * Created on 8/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionPeriod;
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
public class ReadExecutionCoursesToAssociateCurricularCourseAction extends FenixAction {

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

	HttpSession session = request.getSession(false);

	UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

	Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
		
	Object args[] = { executionPeriodId };

		List infoExecutionCoursesList = null;
		try {
			infoExecutionCoursesList = (List) ServiceUtils.executeService(userView, "ReadExecutionCoursesByExecutionPeriod", args);

		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
	
	InfoExecutionPeriod infoExecutionPeriod = null;
	
	try {
			infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadExecutionPeriod", args);

		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingExecutionPeriod", "", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException);
		}
	String ExecutionPeriodNameAndYear = new String(infoExecutionPeriod.getName()+"-"+infoExecutionPeriod.getInfoExecutionYear().getYear());
	request.setAttribute("executionPeriodNameAndYear",ExecutionPeriodNameAndYear);
	
	request.setAttribute("infoExecutionCoursesList", infoExecutionCoursesList);
	
	return mapping.findForward("viewExecutionCourses");
	}
}