/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.owner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class ManageGrantOwnerAction extends DispatchAction
{
    /*
	 * Fills the form with the correspondent data
	 */
    public ActionForward prepareManageGrantOwnerForm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        Integer idInternal = null;

        //TODO.. verificar so que null.. ou tb verificar se são strings vazias!!!
        if (request.getParameter("idInternal") != null)
            idInternal = new Integer(request.getParameter("idInternal"));
        else if ((Integer)request.getAttribute("idInternal") != null)
        	idInternal = (Integer)request.getAttribute("idInternal");

        //Run the service
        Object[] args = { idInternal };
        IUserView userView = SessionUtils.getUserView(request);
        InfoGrantOwner infoGrantOwner =
            (InfoGrantOwner) ServiceUtils.executeService(userView, "ReadGrantOwner", args);

        if (infoGrantOwner == null)
        {
            //TODO... excepcao.. o grant owner nao existe
        	System.out.println("erro 200!!!");
        }
        request.setAttribute("infoGrantOwner", infoGrantOwner);

        return mapping.findForward("manage-grant-owner");
    }
}