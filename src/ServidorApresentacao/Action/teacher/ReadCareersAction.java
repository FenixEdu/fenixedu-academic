/*
 * Created on 18/Nov/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class ReadCareersAction extends FenixAction
{

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        
        if (string != null)
        {
            CareerType careerType = CareerType.getEnum(string);
            GestorServicos gestor = GestorServicos.manager();
            
            Object[] args = { careerType, userView.getUtilizador() };
            
            List careers = (List) gestor.executar(userView, "ReadCareers", args);
            
            request.setAttribute("careers", careers);
        }
        return mapping.findForward("show-form");
    }

}
