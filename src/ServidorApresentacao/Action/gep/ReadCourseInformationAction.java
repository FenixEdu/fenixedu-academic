/*
 * Created on Dec 22, 2003
 *  
 */
package ServidorApresentacao.Action.gep;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.gesdis.InfoSiteCourseInformation;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCourseInformationAction extends FenixAction
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        Object[] args1 = { new Integer(request.getParameter("executionCourseId"))};
        TeacherAdministrationSiteView teacherAdministrationSiteView =
            (TeacherAdministrationSiteView) ServiceUtils.executeService(
                userView,
                "ReadCourseInformation",
                args1);
        InfoSiteCourseInformation infoSiteCourseInformation =
            (InfoSiteCourseInformation) teacherAdministrationSiteView.getComponent();
        request.setAttribute("infoSiteCourseInformation", infoSiteCourseInformation);

        if (request.getParameter("executionDegreeId") != null)
        {
            Object[] args2 = { new Integer(request.getParameter("executionDegreeId"))};
            InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionDegreeByOID",
                    args2);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }

        return mapping.findForward("show");
    }
}
