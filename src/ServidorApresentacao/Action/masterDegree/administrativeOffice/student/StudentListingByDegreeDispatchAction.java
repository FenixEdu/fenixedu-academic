
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import framework.factory.ServiceManagerServiceFactory;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class StudentListingByDegreeDispatchAction extends DispatchAction {

		public ActionForward prepareReading(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

			HttpSession session = request.getSession();

			String executionYearString = getFromRequest("executionYear", request);
			String executionDegreeString = getFromRequest("degree", request);

			request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
			request.setAttribute("executionYear", executionYearString);
			request.setAttribute("degree", executionDegreeString);

			// Get the Students List			
			Object args[] = { executionDegreeString, executionYearString };
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			ArrayList studentList = null;
			try {
				studentList = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadStudentsByExecutionDegreeAndExecutionYear", args);
			} catch (NonExistingServiceException e) {
				ActionErrors errors = new ActionErrors();
				errors.add("nonExisting", new ActionError("message.public.not.found.studentsByDegree", executionDegreeString));
				saveErrors(request, errors);
				return mapping.getInputForward();

			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("studentList", studentList);

			return mapping.findForward("PrepareSuccess");
		}
	  
	private String getFromRequest(String parameter, HttpServletRequest request) {
			String parameterString = request.getParameter(parameter);
			if (parameterString == null) {
				parameterString = (String) request.getAttribute(parameter);
			}
			return parameterString;
		}
}
