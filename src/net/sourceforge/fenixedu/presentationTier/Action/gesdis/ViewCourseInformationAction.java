/*
 * Created on Nov 15, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gesdis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ViewCourseInformationAction extends FenixAction {
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
        ActionErrors errors = new ActionErrors();
        IUserView userView = SessionUtils.getUserView(request);
        String executionCourseId = request.getParameter("executionCourseId");
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        SiteView siteView = null;
        Object[] args = { new Integer(executionCourseId) };
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCourseInformation", args);
        } catch (NotAuthorizedFilterException e) {
            errors.add("notResponsible", new ActionError("label.notAuthorized.courseInformation"));
            saveErrors(request, errors);

            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        return mapping.findForward("successfull-read");
    }

}