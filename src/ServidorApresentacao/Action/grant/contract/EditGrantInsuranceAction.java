/*
 * Created on Jun 26, 2004
 */
package ServidorApresentacao.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantInsurance;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;


/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantInsuranceAction extends FenixDispatchAction {

	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantInsuranceForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm grantInsuranceForm = (DynaValidatorForm) form;
		
		Integer idInsurance = null;
		Integer idContract = null;
		try
		//Probably a validation error
		{
			if (!verifyParameterInRequest(request, "idInsurance"))
			{
				//Check if is a new Insurance
				idContract = new Integer(request.getParameter("idContract"));
			}
			else
			{
				idInsurance = new Integer(request.getParameter("idInsurance"));
			}
		}
		catch (Exception e)
		{
			request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
			request.setAttribute("contractNumber", request.getParameter("contractNumber"));
			request.setAttribute("grantTypeName", request.getParameter("grantTypeName"));
			if (verifyParameterInRequest(request,"idInsurance"))
				request.setAttribute("idInsurance", new Integer(request.getParameter("idInsurance")));
			return mapping.findForward("edit-grant-insurance");
		}
		
		try
		{
			InfoGrantContract infoGrantContract = null;
		
			if (idContract != null)	//if is a new Insurance
			{
				//Read the contract
				Object[] args3 = {idContract};
				infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
						"ReadGrantContract", args3);
				if (infoGrantContract != null)
				{
					request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
					request.setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo().getName());
					grantInsuranceForm.set("state", new Integer(-1));
				}
			}
			else
			{
				//Read the Insurance
				Object[] args = {idInsurance};
				InfoGrantInsurance infoGrantInsurance = (InfoGrantInsurance) ServiceUtils.executeService(
						userView, "ReadGrantInsurance", args);
				
				idContract = infoGrantInsurance.getInfoGrantContract().getIdInternal();
				//Read the contract
				Object[] args2 = {idContract};
				infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
						"ReadGrantContract", args2);
				if (infoGrantContract != null)
				{
					request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
					request.setAttribute("grantTypeName", infoGrantContract.getGrantTypeInfo().getName());
				}
				//Populate the form
				if (infoGrantInsurance != null)
				{
					setFormGrantInsurance(grantInsuranceForm, infoGrantInsurance);
					request.setAttribute("idInsurance", infoGrantInsurance.getIdInternal());
				}
			}
			grantInsuranceForm.set("idContract", idContract);
			request.setAttribute("idContract", idContract);
			grantInsuranceForm.set("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
			request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.insurance.read", "manage-grant-insurance", null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-insurance",null);
		}
		return mapping.findForward("edit-grant-insurance");
	}
	
	public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			DynaValidatorForm editGrantInsuranceForm = (DynaValidatorForm) form;
			InfoGrantInsurance infoGrantInsurance = populateInfoFromForm(editGrantInsuranceForm);
			IUserView userView = SessionUtils.getUserView(request);
			
			if(infoGrantInsurance.getState().equals(new Integer(-1))) //If is a new Insurance
			{
				infoGrantInsurance.setState(new Integer(1)); //Active this insurance
			}
			
			//Save the insurance
			Object[] args = {infoGrantInsurance};			
			ServiceUtils.executeService(userView, "EditGrantInsurance", args);
			request.setAttribute("idInternal", editGrantInsuranceForm.get("idGrantOwner"));
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.insurance.edit", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		return mapping.findForward("manage-grant-insurance");
	}
	/*
	 * Populates form from InfoSubsidy
	 */
	private void setFormGrantInsurance(DynaValidatorForm form, InfoGrantInsurance infoGrantInsurance)
			throws Exception
	{
	    //TODO
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		
//		form.set("idGrantSubsidy", infoGrantInsurance.getIdInternal());
//		if (infoGrantInsurance.getValue() != null)
//			form.set("value", infoGrantInsurance.getValue().toString());
//		if (infoGrantInsurance.getValueFullName() != null)
//			form.set("valueFullName", infoGrantInsurance.getValueFullName());
//		if (infoGrantInsurance.getTotalCost() != null)
//			form.set("totalCost", infoGrantInsurance.getTotalCost().toString());
//		if (infoGrantInsurance.getDateBeginSubsidy() != null)
//			form.set("dateBeginSubsidy", sdf.format(infoGrantInsurance.getDateBeginSubsidy()));
//		if (infoGrantInsurance.getDateEndSubsidy() != null)
//			form.set("dateEndSubsidy", sdf.format(infoGrantInsurance.getDateEndSubsidy()));
//		//In case state is null.. than this is a new subsidy, put the state -1
//		if (infoGrantInsurance.getState() != null)
//			form.set("state", infoGrantInsurance.getState());
//		else
//			form.set("state", new Integer(-1));
	}
	private InfoGrantInsurance populateInfoFromForm(DynaValidatorForm editGrantInsuranceForm)
			throws Exception
	{
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		
//		InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
//		InfoGrantContract infoGrantContract = new InfoGrantContract();
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "idGrantSubsidy"))
//			infoGrantSubsidy.setIdInternal((Integer) editGrantSubsidyForm.get("idGrantSubsidy"));
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "value"))
//			infoGrantSubsidy.setValue(new Double((String) editGrantSubsidyForm.get("value")));
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "valueFullName"))
//			infoGrantSubsidy.setValueFullName((String) editGrantSubsidyForm.get("valueFullName"));
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "totalCost"))
//			infoGrantSubsidy.setTotalCost(new Double((String) editGrantSubsidyForm.get("totalCost")));
//		infoGrantContract.setIdInternal((Integer) editGrantSubsidyForm.get("idContract"));
//		
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "dateBeginSubsidy"))
//			infoGrantSubsidy.setDateBeginSubsidy(sdf.parse((String) editGrantSubsidyForm.get("dateBeginSubsidy")));
//		
//		if (verifyStringParameterInForm(editGrantSubsidyForm, "dateEndSubsidy"))
//			infoGrantSubsidy.setDateEndSubsidy(sdf.parse((String) editGrantSubsidyForm.get("dateEndSubsidy")));
//		
//		infoGrantSubsidy.setInfoGrantContract(infoGrantContract);
//		infoGrantSubsidy.setState((Integer) editGrantSubsidyForm.get("state"));
//		return infoGrantSubsidy;
	    //TODO
	    return null;
	}
}
