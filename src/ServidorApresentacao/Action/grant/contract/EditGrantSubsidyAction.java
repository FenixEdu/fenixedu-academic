/*
 * Created on 23/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class EditGrantSubsidyAction extends FenixDispatchAction
{

	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantSubsidyForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm grantSubsidyForm = (DynaValidatorForm) form;
		Integer idSubsidy = null;
		Integer idContract = null;
		try
		//Probably a validation error
		{
			if (request.getParameter("idSubsidy") == null)
				//Check if is a new subsidy
				idContract = new Integer(request.getParameter("idContract"));
			else
				idSubsidy = new Integer(request.getParameter("idSubsidy"));
		}
		catch (Exception e)
		{
			request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
			if (request.getParameter("idSubsidy") != null
					&& !request.getParameter("idSubsidy").equals(""))
				request.setAttribute("idSubsidy", new Integer(request.getParameter("idSubsidy")));
			request.setAttribute("contractNumber", request.getParameter("contractNumber"));
			request.setAttribute("grantTypeName", request.getParameter("grantTypeName"));
			return mapping.findForward("edit-grant-subsidy");
		}
		try
		{
			InfoGrantContract infoGrantContract = null;
			//if is a new contract
			if (idContract != null)
			{
				//Read the contract
				Object[] args3 = {idContract};
				infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
						"ReadGrantContract", args3);
				if (infoGrantContract != null)
				{
					request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
					request
							.setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo()
									.getName());
				}
			}
			else
			{
				//Read the subsidy
				Object[] args = {idSubsidy};
				InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) ServiceUtils.executeService(
						userView, "ReadGrantSubsidy", args);
				idContract = infoGrantSubsidy.getInfoGrantContract().getIdInternal();
				//Read the contract
				Object[] args2 = {idContract};
				infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
						"ReadGrantContract", args2);
				if (infoGrantContract != null)
				{
					request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
					request
							.setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo()
									.getName());
				}
				//Populate the form
				if (infoGrantSubsidy != null)
				{
					setFormGrantSubsidy(grantSubsidyForm, infoGrantSubsidy);
					request.setAttribute("idSubsidy", infoGrantSubsidy.getIdInternal());
				}
			}
			grantSubsidyForm.set("idContract", idContract);
			request.setAttribute("idContract", idContract);
			grantSubsidyForm.set("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
			request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.subsidy.read", "manage-grant-contract", null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract",
					null);
		}
		return mapping.findForward("edit-grant-subsidy");
	}
	public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		DynaValidatorForm editGrantSubsidyForm = (DynaValidatorForm) form;
		InfoGrantSubsidy infoGrantSubsidy = populateInfoFromForm(editGrantSubsidyForm);
		try
		{
			Object[] args = {infoGrantSubsidy};
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditGrantSubsidy", args);
			request.setAttribute("idInternal", editGrantSubsidyForm.get("idGrantOwner"));
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.subsidy.edit", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		return mapping.findForward("manage-grant-subsidy");
	}
	/*
	 * Populates form from InfoSubsidy
	 */
	private void setFormGrantSubsidy(DynaValidatorForm form, InfoGrantSubsidy infoGrantSubsidy)
			throws Exception
	{
		form.set("idGrantSubsidy", infoGrantSubsidy.getIdInternal());
		if (infoGrantSubsidy.getValue() != null)
			form.set("value", infoGrantSubsidy.getValue().toString());
		if (infoGrantSubsidy.getValueFullName() != null)
			form.set("valueFullName", infoGrantSubsidy.getValueFullName());
		if (infoGrantSubsidy.getTotalCost() != null)
			form.set("totalCost", infoGrantSubsidy.getTotalCost().toString());
	}
	private InfoGrantSubsidy populateInfoFromForm(DynaValidatorForm editGrantSubsidyForm)
			throws Exception
	{
		InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
		InfoGrantContract infoGrantContract = new InfoGrantContract();
		if (verifyStringParameterInForm(editGrantSubsidyForm, "idGrantSubsidy"))
			infoGrantSubsidy.setIdInternal((Integer) editGrantSubsidyForm.get("idGrantSubsidy"));
		if (verifyStringParameterInForm(editGrantSubsidyForm, "value"))
			infoGrantSubsidy.setValue(new Double((String) editGrantSubsidyForm.get("value")));
		if (verifyStringParameterInForm(editGrantSubsidyForm, "valueFullName"))
			infoGrantSubsidy.setValueFullName((String) editGrantSubsidyForm.get("valueFullName"));
		if (verifyStringParameterInForm(editGrantSubsidyForm, "totalCost"))
			infoGrantSubsidy.setTotalCost(new Double((String) editGrantSubsidyForm.get("totalCost")));
		infoGrantContract.setIdInternal((Integer) editGrantSubsidyForm.get("idContract"));
		infoGrantSubsidy.setInfoGrantContract(infoGrantContract);
		return infoGrantSubsidy;
	}
}