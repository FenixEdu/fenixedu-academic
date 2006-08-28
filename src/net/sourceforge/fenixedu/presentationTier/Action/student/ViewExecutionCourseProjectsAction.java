/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author asnr & scpo
 *
 */
public class ViewExecutionCourseProjectsAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException, FenixFilterException, FenixServiceException {

		HttpSession session = request.getSession(false);
		IUserView userView = getUserView(request);

		String executionCourseCodeString = request.getParameter("executionCourseCode");

		if (executionCourseCodeString == null || executionCourseCodeString.equals("")) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.notSelected.executionCourse");
			actionErrors1.add("errors.notSelected.executionCourse", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("insucess");
		}
		Integer executionCourseCode = new Integer(executionCourseCodeString);

		Object[] args = {executionCourseCode, userView.getUtilizador()};
        ISiteComponent viewProjectsComponent = (InfoSiteProjects) ServiceUtils.executeService(userView, "ReadExecutionCourseProjects", args);

        Object[] args2 = { executionCourseCode };
        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceUtils.executeService(userView, "ReadExecutionCourseByOID", args2);        
        request.setAttribute("infoExecutionCourse", infoExecutionCourse);

		InfoSiteProjects infoSiteProjects = (InfoSiteProjects) viewProjectsComponent;
		List infoGroupPropertiesList = new ArrayList();
		if (infoSiteProjects != null) {
			infoGroupPropertiesList = infoSiteProjects.getInfoGroupPropertiesList();
		}else{
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			error1 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors1.add("errors.noStudentInAttendsSet", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("noprojects");
		}

		
		
		request.setAttribute("infoGroupPropertiesList", infoGroupPropertiesList);

		return mapping.findForward("sucess");

	}

}
