/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorApresentacao.Action.publication;

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
import constants.publication.PublicationConstants;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationsAction extends FenixAction {

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
        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);
        String string = request.getParameter("typePublication");

        if ((session != null)) {

            Object[] args = { userView.getUtilizador(), string };

            SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadPublications",
                    args);

            request.setAttribute("siteView", siteView);
        }
        ActionForward actionForward = null;

        if (string.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
            actionForward = mapping.findForward("show-didatic-Teacher-form");
        } else {
            actionForward = mapping.findForward("show-cientific-Teacher-form");
        }
        return actionForward;
    }
}