/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadOldPublicationsAction extends FenixAction
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
        HttpSession session = request.getSession(false);

        String string = request.getParameter("oldPublicationType");
        OldPublicationType oldPublicationType = null;
        IUserView userView = SessionUtils.getUserView(request);

        if ((session != null) && (string != null))
        {
            oldPublicationType = OldPublicationType.getEnum(string);

            Object[] args = { oldPublicationType, userView.getUtilizador()};
            SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadOldPublications", args);

            request.setAttribute("siteView", siteView);
        }
        ActionForward actionForward = null;

        if (oldPublicationType.equals(OldPublicationType.CIENTIFIC))
        {
            actionForward = mapping.findForward("show-cientific-form");
        } else
        {
            actionForward = mapping.findForward("show-didactic-form");
        }
        return actionForward;
    }
}
