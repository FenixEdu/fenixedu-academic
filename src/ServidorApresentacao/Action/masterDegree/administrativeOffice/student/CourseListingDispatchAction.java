
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 * 
 */
public class CourseListingDispatchAction extends DispatchAction {

	public ActionForward getStudentsFromCourse(ActionMapping mapping, ActionForm form,
												HttpServletRequest request,
												HttpServletResponse response)
			throws Exception {

			HttpSession session = request.getSession(false);
			GestorServicos serviceManager = GestorServicos.manager();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
			//Get the Selected Course
			
			Integer scopeCode = null;
			String scopeCodeString = request.getParameter("scopeCode");
			if (scopeCodeString == null) {
				scopeCodeString = (String) request.getAttribute("scopeCode");
			}
			if (scopeCodeString != null) {
				scopeCode = new Integer(scopeCodeString);
			}

			String yearString = getFromRequest("executionYear", request);
			
			Object args[] = { scopeCode, yearString };
			try {

		//	result = (List)
			 serviceManager.executar(userView, "ReadStudentsAndMarksByCurricularCourse", args);

			} catch (NonExistingServiceException e) {
				ActionErrors errors = new ActionErrors();
				errors.add("error.exception.noStudents", new ActionError("error.exception.noStudents"));
				saveErrors(request, errors);
									
				return mapping.findForward("NoStudents");
			}
			
//				BeanComparator numberComparator = new BeanComparator("infoStudent.number");
//				Collections.sort(result, numberComparator);

//				request.setAttribute(SessionConstants.STUDENT_LIST, result);
		
			return mapping.findForward("Success");
	  }
	
		private String getFromRequest(String parameter, HttpServletRequest request) {
			String parameterString = request.getParameter(parameter);
			if (parameterString == null) {
				parameterString = (String) request.getAttribute(parameter);
			}
			return parameterString;
		}
		
}