/*
 * Created on 26/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteProjects;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr & scpo
 *
 */
public class ViewExecutionCourseProjectsAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseCodeString = request.getParameter("executionCourseCode");
		Integer executionCourseCode = new Integer(executionCourseCodeString);

		ISiteComponent viewProjectsComponent;
		Object[] args = { executionCourseCode };
		try {
			viewProjectsComponent = (InfoSiteProjects) ServiceUtils.executeService(userView, "ReadExecutionCourseProjects", args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		InfoSiteProjects infoSiteProjects = (InfoSiteProjects) viewProjectsComponent;
		List infoGroupPropertiesList = new ArrayList();
		if (infoSiteProjects != null) {
			infoGroupPropertiesList = infoSiteProjects.getInfoGroupPropertiesList();
		}
		request.setAttribute("infoGroupPropertiesList", infoGroupPropertiesList);
		
		return mapping.findForward("sucess");

	}

}
