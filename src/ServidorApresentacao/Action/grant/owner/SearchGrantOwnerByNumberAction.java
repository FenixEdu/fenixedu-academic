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
		//Ler os dados do form bean
		DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
		String idGrantOwner = (String) searchGrantOwnerForm.get("idGrantOwner");

		//Caso sejam null, vamos verificar se os atributos tb são, pode ser um voltar!
		if (idGrantOwner.equals("") && (request.getParameter("sidGrantOwner") != null))
			idGrantOwner = request.getParameter("sidGrantOwner");

		Integer arg = new Integer(idGrantOwner);

		Object[] args = { null, null, null, arg };

		IUserView userView = SessionUtils.getUserView(request);

		List infoGrantOwnerList = (List) ServiceUtils.executeService(userView, "SearchGrantOwner", args);

		if (!infoGrantOwnerList.isEmpty())
		{
			request.setAttribute("infoGrantOwnerList", infoGrantOwnerList);
			return mapping.findForward("search-succesfull");
		} else
		{
			ActionErrors errors = new ActionErrors();
			String notMessageKey = "errors.grant.owner.not.found";

			ActionError error = new ActionError(notMessageKey, idGrantOwner);
			errors.add(notMessageKey, error);
			saveErrors(request, errors);

			return mapping.findForward("search-unSuccesfull");
		}
	}
}
