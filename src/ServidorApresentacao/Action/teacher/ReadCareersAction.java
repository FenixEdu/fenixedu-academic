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
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareersAction extends FenixAction
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

        String string = request.getParameter("careerType");
        CareerType careerType = null;
        IUserView userView = SessionUtils.getUserView(request);

        if ((session != null) && (string != null))
        {
            careerType = CareerType.getEnum(string);

            Object[] args = { careerType, userView.getUtilizador()};
            SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadCareers", args);

            request.setAttribute("siteView", siteView);
        }
        ActionForward actionForward = null;

        if (careerType.equals(CareerType.PROFESSIONAL))
        {
            actionForward = mapping.findForward("show-professional-form");
        } else
        {
            actionForward = mapping.findForward("show-teaching-form");
        }
        return actionForward;
    }
}
