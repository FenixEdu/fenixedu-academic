/*
 * Created on 20/Dec/2003
 */

package ServidorApresentacao.Action.grant.contract;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantContractEndDateBeforeBeginDateException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherPeriodNotWithinContractPeriodException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;


/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantContractAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantContractForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
	    IUserView userView = SessionUtils.getUserView(request);
		try
		{
			//Read grant types for the contract
			Object[] args2 = { };
			List grantTypeList = (List) ServiceUtils.executeService(userView, "ReadAllGrantTypes", args2);
			request.setAttribute("grantTypeList", grantTypeList);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.type.read", "manage-grant-contract", null);
		}
		
		if (!verifyParameterInRequest(request,"loaddb")) {
			//Validation error
		    request.setAttribute("idInternal",request.getParameter("idInternal"));
		    return mapping.findForward("edit-grant-contract");
		}
		Integer idContract = null;
		if (verifyParameterInRequest(request,"idContract")) {
			idContract = new Integer(request.getParameter("idContract"));
		}
		DynaValidatorForm grantContractForm = (DynaValidatorForm) form;
		if (idContract != null)
		{
			try
			{
				//Read the contract
				Object[] args = { idContract };
				InfoGrantContract infoGrantContract =
					(InfoGrantContract) ServiceUtils.executeService(userView, "ReadGrantContract", args);

				//Read the actual Regime associated with this contract
				Object[] argregime = { idContract, new Integer(1)}; 
				List infoGrantContractRegimeActiveList = 
					(List) ServiceUtils.executeService(userView, "ReadGrantContractRegimeByContractAndState",argregime);
				
				//It should only be one active contract regime
				InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime)infoGrantContractRegimeActiveList.get(0);
				
				//Populate the form
				setFormGrantContract(grantContractForm, infoGrantContract, infoGrantContractRegime);
				request.setAttribute("idInternal",infoGrantContract.getGrantOwnerInfo().getIdInternal());
			}
			catch (FenixServiceException e)
			{
				return setError(request,mapping,"errors.grant.contract.read","manage-grant-contract",null);
			}
			catch (Exception e)
			{
				return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract",null);
			}
		}
		else
		{
			//New contract
			try
			{
				grantContractForm.set("idInternal", new Integer(request.getParameter("idInternal")));
				request.setAttribute("idInternal", new Integer(request.getParameter("idInternal")));
			}
			catch (Exception e)
			{
				return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract",null);
			}
		}

		return mapping.findForward("edit-grant-contract");
	}


	/*
	 * Edit Grant Contract
	 */
	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer orientationTeacherNumber = null;
		try
		{
			DynaValidatorForm editGrantContractForm = (DynaValidatorForm) form;
			InfoGrantContract infoGrantContract = populateInfoGrantContractFromForm(editGrantContractForm);
			InfoGrantContractRegime infoGrantContractRegime = populateInfoGrantContractRegimeFromForm(editGrantContractForm, infoGrantContract);

			if(infoGrantContractRegime.getDateBeginContract().after(infoGrantContractRegime.getDateEndContract())) {
			    return setError(request, mapping, "errors.grant.contract.conflictdates", null, null);
			}
			IUserView userView = SessionUtils.getUserView(request);
	
			orientationTeacherNumber = infoGrantContract.getGrantOrientationTeacherInfo().getOrientationTeacherInfo().getTeacherNumber();			
			request.setAttribute("idInternal", editGrantContractForm.get("idInternal"));
			
			//Edit Grant Contract
			Object[] args = { infoGrantContract };
			ServiceUtils.executeService(userView, "EditGrantContract", args);
			
			if(infoGrantContract.getIdInternal() == null || infoGrantContract.getIdInternal().equals(new Integer(0))) //In case of a new contract
			{
				Object[] argcontract = {infoGrantContract.getGrantOwnerInfo().getIdInternal() };
				infoGrantContract = (InfoGrantContract)ServiceUtils.executeService(userView, "ReadLastGrantContractCreatedByGrantOwner", argcontract);
			}
			
			//Edit Grant Contract Regime
			Object[] argregime = { infoGrantContractRegime };
			ServiceUtils.executeService(userView, "EditGrantContractRegime", argregime);
			
			return mapping.findForward("manage-grant-contract");
		}
		catch (GrantOrientationTeacherPeriodNotWithinContractPeriodException e)
		{
			return setError(request,mapping,"errors.grant.contract.orientation.teacher.periodconflict",null,null);
		}
		catch (GrantContractEndDateBeforeBeginDateException e)
		{
			return setError(request, mapping, "errors.grant.contract.conflictdates", null, null);
		}
		catch (GrantTypeNotFoundException e)
		{
			return setError(request, mapping, "errors.grant.type.not.found", null, null);
		}
		catch (GrantOrientationTeacherNotFoundException e)
		{
			return setError(request,mapping,"errors.grant.contract.orientation.teacher.not.found",null,orientationTeacherNumber);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.contract.bd.create", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
	}

	/*
	 * Populates form from InfoContract
	 */
	private void setFormGrantContract(DynaValidatorForm form, InfoGrantContract infoGrantContract, InfoGrantContractRegime infoGrantContractRegime)
		throws Exception
	{
		//Grant Owner
		form.set("idInternal", infoGrantContract.getGrantOwnerInfo().getIdInternal());
		
		//Grant Contract
		form.set("idGrantContract", infoGrantContract.getIdInternal());
		form.set("contractNumber", infoGrantContract.getContractNumber().toString());
		if (infoGrantContract.getEndContractMotive() != null)
			form.set("endContractMotive", infoGrantContract.getEndContractMotive());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (infoGrantContract.getDateAcceptTerm() != null)
			form.set("dateAcceptTerm", sdf.format(infoGrantContract.getDateAcceptTerm()));
		
		//Grant Contract Orientation teacher
		form.set("grantContractOrientationTeacherNumber",infoGrantContract.getGrantOrientationTeacherInfo().getOrientationTeacherInfo().getTeacherNumber().toString());
		form.set("grantContractOrientationTeacherId",infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal());
		//Grant Contract Grant Type
		form.set("grantType", infoGrantContract.getGrantTypeInfo().getSigla());		
		
		//Grant Contract Regime
		form.set("grantContractRegimeId", infoGrantContractRegime.getIdInternal());
		if (infoGrantContractRegime.getDateBeginContract() != null)
			form.set("dateBeginContract", sdf.format(infoGrantContractRegime.getDateBeginContract()));
		if (infoGrantContractRegime.getDateEndContract() != null)
			form.set("dateEndContract", sdf.format(infoGrantContractRegime.getDateEndContract()));
		if (infoGrantContractRegime.getDateSendDispatchCC() != null)
			form.set("dateSendDispatchCC", sdf.format(infoGrantContractRegime.getDateSendDispatchCC()));
		if (infoGrantContractRegime.getDateDispatchCC() != null)
			form.set("dateDispatchCC", sdf.format(infoGrantContractRegime.getDateDispatchCC()));
		if (infoGrantContractRegime.getDateSendDispatchCD() != null)
			form.set("dateSendDispatchCD", sdf.format(infoGrantContractRegime.getDateSendDispatchCD()));
		if (infoGrantContractRegime.getDateDispatchCD() != null)
			form.set("dateDispatchCD", sdf.format(infoGrantContractRegime.getDateDispatchCD()));
	}

	/*
	 * Populates InfoGrantContract from Form
	 */
	private InfoGrantContract populateInfoGrantContractFromForm(DynaValidatorForm editGrantContractForm)
		throws Exception
	{
		InfoGrantContract infoGrantContract = new InfoGrantContract();
		InfoGrantOrientationTeacher orientationTeacher = new InfoGrantOrientationTeacher();
		InfoTeacher infoTeacher = new InfoTeacher();
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		InfoGrantType infoGrantType = new InfoGrantType();

		if(verifyStringParameterInForm(editGrantContractForm,"idGrantContract"))
			infoGrantContract.setIdInternal((Integer) editGrantContractForm.get("idGrantContract"));
        if(verifyStringParameterInForm(editGrantContractForm,"contractNumber"))
			infoGrantContract.setContractNumber(new Integer((String) editGrantContractForm.get("contractNumber")));
        infoGrantContract.setEndContractMotive((String) editGrantContractForm.get("endContractMotive"));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(verifyStringParameterInForm(editGrantContractForm,"dateAcceptTerm"))
			infoGrantContract.setDateAcceptTerm(sdf.parse((String) editGrantContractForm.get("dateAcceptTerm")));
		
        //Setting InfoGrantOwner
        infoGrantOwner.setIdInternal((Integer) editGrantContractForm.get("idInternal"));
		infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

		//Setting Grant Type
		infoGrantType.setSigla((String) editGrantContractForm.get("grantType"));
		infoGrantContract.setGrantTypeInfo(infoGrantType);

		//Setting Grant Orientation Teacher
        if(verifyStringParameterInForm(editGrantContractForm,"dateBeginContract"))
		{
			orientationTeacher.setBeginDate(sdf.parse((String) editGrantContractForm.get("dateBeginContract")));
		}
        if(verifyStringParameterInForm(editGrantContractForm,"dateEndContract"))
		{
			orientationTeacher.setEndDate(sdf.parse((String) editGrantContractForm.get("dateEndContract")));
		}
		infoTeacher.setTeacherNumber(new Integer((String) editGrantContractForm.get("grantContractOrientationTeacherNumber")));
		orientationTeacher.setOrientationTeacherInfo(infoTeacher);
		orientationTeacher.setIdInternal((Integer) editGrantContractForm.get("grantContractOrientationTeacherId"));
		infoGrantContract.setGrantOrientationTeacherInfo(orientationTeacher);

		return infoGrantContract;
	}
	
	/*
	 * Populate InfoGrantContractRegime from Form
	 */
	private InfoGrantContractRegime populateInfoGrantContractRegimeFromForm(DynaValidatorForm editGrantContractForm, InfoGrantContract infoGrantContract)
	throws Exception
	{
		InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();
		
		if(verifyStringParameterInForm(editGrantContractForm,"grantContractRegimeId"))
			infoGrantContractRegime.setIdInternal((Integer) editGrantContractForm.get("grantContractRegimeId"));
		infoGrantContractRegime.setState(new Integer(1));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
	    if(verifyStringParameterInForm(editGrantContractForm,"dateBeginContract"))
			infoGrantContractRegime.setDateBeginContract(sdf.parse((String) editGrantContractForm.get("dateBeginContract")));
	    if(verifyStringParameterInForm(editGrantContractForm,"dateEndContract"))
			infoGrantContractRegime.setDateEndContract(sdf.parse((String) editGrantContractForm.get("dateEndContract")));
	    if(verifyStringParameterInForm(editGrantContractForm,"dateSendDispatchCC"))
			infoGrantContractRegime.setDateSendDispatchCC(sdf.parse((String) editGrantContractForm.get("dateSendDispatchCC")));
	    if(verifyStringParameterInForm(editGrantContractForm,"dateDispatchCC"))
			infoGrantContractRegime.setDateDispatchCC(sdf.parse((String) editGrantContractForm.get("dateDispatchCC")));
	    if(verifyStringParameterInForm(editGrantContractForm,"dateSendDispatchCD"))
	    	infoGrantContractRegime.setDateSendDispatchCD(sdf.parse((String) editGrantContractForm.get("dateSendDispatchCD")));
	    if(verifyStringParameterInForm(editGrantContractForm,"dateDispatchCD"))
			infoGrantContractRegime.setDateDispatchCD(sdf.parse((String) editGrantContractForm.get("dateDispatchCD")));
	    
	    infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
	    	    
		return infoGrantContractRegime;
	}
}
