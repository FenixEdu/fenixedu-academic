/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.CareerType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareersAction extends FenixAction {

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

        String string = request.getParameter("careerType");
        CareerType careerType = null;
        IUserView userView = SessionUtils.getUserView(request);

        if ((session != null) && (string != null)) {
            careerType = CareerType.getEnum(string);

            Object[] args = { careerType, userView.getUtilizador() };
            SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCareers", args);

            request.setAttribute("siteView", siteView);
        }
        ActionForward actionForward = null;

        if (careerType.equals(CareerType.PROFESSIONAL)) {
            actionForward = mapping.findForward("show-professional-form");
        } else {
            actionForward = mapping.findForward("show-teaching-form");
        }
        return actionForward;
    }
}