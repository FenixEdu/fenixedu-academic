/*
 * Created on Nov 15, 2003
 *  
 */
package ServidorApresentacao.Action.gesdis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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

        SiteView siteView = null;
        Object[] args = { new Integer(executionCourseId) };
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCourseInformation", args);
        } catch (NotAuthorizedException e) {
            errors.add("notResponsible", new ActionError("message.error.notAuthorized"));
            saveErrors(request, errors);

            request.setAttribute("objectCode", executionCourseId);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        return mapping.findForward("successfull-read");
    }

}