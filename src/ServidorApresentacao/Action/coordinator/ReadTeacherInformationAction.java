/*
 * Created on Nov 15, 2003
 *  
 */
package ServidorApresentacao.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import DataBeans.teacher.InfoSiteTeacherInformation;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadTeacherInformationAction extends FenixAction {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { request.getParameter("username") };
        SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTeacherInformation",
                args);
        InfoSiteTeacherInformation infoSiteTeacherInformation = (InfoSiteTeacherInformation) siteView
                .getComponent();
        request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);
        return mapping.findForward("show");
    }
}