/*
 * Created on 15/May/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

/**
 * @author lmac1
 *
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;



public class ShowStudentsEnrolledInExamAction extends FenixAction{
	
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
	
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseCodeString=  request.getParameter("objectCode");
		Integer executionCourseCode = new Integer(executionCourseCodeString);
		String examCodeString = request.getParameter("examCode");
		Integer examCode = new Integer(examCodeString);
		Object[] args={executionCourseCode,examCode};
		SiteView siteView=null;
		try {
			siteView=(SiteView) ServiceUtils.executeService(userView,"ReadStudentsEnrolledInExam",args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("siteView",siteView);
		request.setAttribute("objectCode",executionCourseCode);
		return mapping.findForward("showStudents");
		
		}
			
}
