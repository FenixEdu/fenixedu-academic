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
 *  
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
        //Integer grantOwnerId = (Integer)request.getAttribute("grantOwnerId");
        Integer grantOwnerId = new Integer(21);
        InfoGrantOwner infoGrantOwner = null;
        
        if(grantOwnerId == null)
        {
            //ERRO!!!!!
        }
        
		Object[] args = { grantOwnerId };
		IUserView userView = SessionUtils.getUserView(request);
		infoGrantOwner = (InfoGrantOwner)ServiceUtils.executeService(userView, "ReadGrantOwner", args);
        
		if(infoGrantOwner != null)
		{
		    request.setAttribute("infoGrantOwner",infoGrantOwner);
		}
		else
		{
		    request.setAttribute("duh","duh");
		}
		
		//Faltam os contratos
		
		return mapping.findForward("manage-grant-owner");
    }
}