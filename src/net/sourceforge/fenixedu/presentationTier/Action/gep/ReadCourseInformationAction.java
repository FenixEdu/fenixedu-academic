/*
 * Created on Dec 22, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCourseInformationAction extends FenixAction {
    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();

	Object[] args1 = { new Integer(request.getParameter("executionCourseId")) };
	TeacherAdministrationSiteView teacherAdministrationSiteView = (TeacherAdministrationSiteView) ServiceUtils
		.executeService("ReadCourseInformation", args1);
	InfoSiteCourseInformation infoSiteCourseInformation = (InfoSiteCourseInformation) teacherAdministrationSiteView
		.getComponent();
	request.setAttribute("infoSiteCourseInformation", infoSiteCourseInformation);

	if (request.getParameter("executionDegreeId") != null) {
	    Object[] args2 = { new Integer(request.getParameter("executionDegreeId")) };
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
		    "ReadExecutionDegreeByOID", args2);
	    request.setAttribute("infoExecutionDegree", infoExecutionDegree);
	}

	return mapping.findForward("show");
    }
}