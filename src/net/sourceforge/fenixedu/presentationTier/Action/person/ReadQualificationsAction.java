/*
 * Created on Nov 18, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoSiteQualifications;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 */
public class ReadQualificationsAction extends FenixAction {
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

        if (session != null) {
            Object[] args = { userView.getUtilizador() };

            InfoSiteQualifications infoSiteQualifications = (InfoSiteQualifications) ServiceUtils
                    .executeService(userView, "ReadQualifications", args);

            request.setAttribute("infoSiteQualifications", infoSiteQualifications);
        }
        return mapping.findForward("show-form");
    }

}