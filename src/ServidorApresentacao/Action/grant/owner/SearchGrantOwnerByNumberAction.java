/*
 * Created on Dec 20, 2003
 *  
 */
package ServidorApresentacao.Action.grant.owner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class SearchGrantOwnerByNumberAction extends DispatchAction
{
	public ActionForward searchGrantOwner(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		List infoGrantOwnerList = null;

		try
		{
			//Read attributes from FormBean
			DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
			String idGrantOwner = (String) searchGrantOwnerForm.get("idGrantOwner");
			//Run the service			
			Integer arg = new Integer(idGrantOwner);
			Object[] args = { null, null, null, arg };
			IUserView userView = SessionUtils.getUserView(request);
			infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner", args);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable","search-unSuccesfull",null);
		}

		if (!infoGrantOwnerList.isEmpty())
		{
			request.setAttribute("infoGrantOwnerList", infoGrantOwnerList);
			return mapping.findForward("search-succesfull");
		}
		else
		{
			return setError(request, mapping, "errors.grant.owner.not.found","search-unSuccesfull",null);
		}
	}
	
	/*
	 * Sets an error to be displayed in the page and sets the mapping forward
	 */
	private ActionForward setError(
			HttpServletRequest request,
			ActionMapping mapping,
			String errorMessage,
			String forwardPage,
			Object actionArg)
	{
		ActionErrors errors = new ActionErrors();
		String notMessageKey = errorMessage;
		ActionError error = new ActionError(notMessageKey, actionArg);
		errors.add(notMessageKey, error);
		saveErrors(request, errors);

		if (forwardPage != null)
			return mapping.findForward(forwardPage);
		else
			return mapping.getInputForward();
	}
}
