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

import DataBeans.grant.contract.InfoGrantContract;
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

		if (request.getParameter("idInternal") != null)
			idInternal = new Integer(request.getParameter("idInternal"));

	
		if (idInternal.intValue() == 0)
		{
			//TODO... excepcao
		}

		Object[] args = { idInternal };
		IUserView userView = SessionUtils.getUserView(request);
		InfoGrantOwner infoGrantOwner =
			(InfoGrantOwner) ServiceUtils.executeService(userView, "ReadGrantOwner", args);

		if (infoGrantOwner == null)
		{
			//TODO... excepcao.. o grant owner nao existe
		}
		request.setAttribute("infoGrantOwner", infoGrantOwner);
		
		//Read contracts od grant owner
		List infoGrantContractList =
			(List) ServiceUtils.executeService(userView, "ReadAllContractsByGrantOwner", args);
		
		InfoGrantContract testContract = (InfoGrantContract) infoGrantContractList.get(0);
System.out.println("GRANT RESPONSIBLE TEACHER: " + testContract.getGrantResponsibleTeacherInfo().getResponsibleTeacherInfo().getTeacherNumber().toString());

		//If they exist put them on request
		if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
			request.setAttribute("infoGrantContractList", infoGrantContractList);
		
		return mapping.findForward("manage-grant-owner");
	}
}