/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.owner;

import java.util.List;

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
    	Integer idInternal = null;
    	if(request.getParameter("idInternal") != null)
    		 idInternal = new Integer(request.getParameter("idInternal"));
    	else
    		idInternal = new Integer(0);
    		
        InfoGrantOwner infoGrantOwner = null;

        if (idInternal.intValue() == 0)
        {
            //ERRO!!!!!
        }

        Object[] args = { idInternal };
        IUserView userView = SessionUtils.getUserView(request);
        infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService(userView, "ReadGrantOwner", args);

        if (infoGrantOwner != null)
            request.setAttribute("infoGrantOwner", infoGrantOwner);
        //Ler contractos
        List infoGrantContractList =
            (List) ServiceUtils.executeService(userView, "ReadAllContractsByGrantOwner", args);

        if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
            request.setAttribute("infoGrantContractList", infoGrantContractList);

        return mapping.findForward("manage-grant-owner");
    }
}