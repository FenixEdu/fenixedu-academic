/*
 * Created on 10/Mai/2004
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
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContractRegimeAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantContractRegime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm grantContractRegimeForm = (DynaValidatorForm) form;
		Integer idGrantContractRegime = null;
		Integer idContract = null;
		
		try
		{
			if (!verifyParameterInRequest(request,"grantContractRegimeId"))
			{
				//Check if is a new contract regime
				idContract = new Integer(request.getParameter("idContract"));
			}
			else
			{
				idGrantContractRegime = new Integer(request.getParameter("grantContractRegimeId"));
			}
		}
		catch (Exception e)//Probably a validation error
		{
			request.setAttribute("contractNumber", request.getParameter("contractNumber"));
			request.setAttribute("idContract", request.getParameter("idContract"));
			if (verifyParameterInRequest(request,"idContractRegime"))
				request.setAttribute("grantContractRegimeId", new Integer(request.getParameter("grantContractRegimeId")));

			return mapping.findForward("edit-grant-contract-regime");
		}
		
		try
		{
			InfoGrantContract infoGrantContract = null;
		
			if (idContract != null) //if is a new Contract Regime
			{
				//Read the contract regime
				Object[] args3 = { idContract };
				infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,"ReadGrantContract", args3);
				
				if (infoGrantContract != null)
				{
					request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
				}
				grantContractRegimeForm.set("state", new Integer(-1));
			}
			else
			{
				//Read the subsidy
				Object[] args = { idGrantContractRegime };
				InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) ServiceUtils.executeService(
						userView, "ReadGrantContractRegime", args);
				
				idContract = infoGrantContractRegime.getInfoGrantContract().getIdInternal();

				//Populate the form
				if (infoGrantContractRegime != null)
				{
					setFormGrantContractRegime(grantContractRegimeForm, infoGrantContractRegime);
					request.setAttribute("grantContractRegimeId", infoGrantContractRegime.getIdInternal());
					request.setAttribute("contractNumber", infoGrantContractRegime.getInfoGrantContract().getContractNumber().toString());
				}
			}

			grantContractRegimeForm.set("idContract", idContract);
			request.setAttribute("idContract", idContract);
			
			return mapping.findForward("edit-grant-contract-regime");
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.contact.regime.read", "manage-grant-contract-regime", null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract-regime",null);
		}
	}
	
	/*
	 * Edit a contract regime
	 */
	public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			IUserView userView = SessionUtils.getUserView(request);
			DynaValidatorForm editGrantContractRegimeForm = (DynaValidatorForm) form;
			InfoGrantContractRegime infoGrantContractRegime = 
				(InfoGrantContractRegime)populateInfoFromForm(editGrantContractRegimeForm);
			
			/*
			 * Verify the teacher
			 */
			InfoTeacher infoTeacher = null;
            if (infoGrantContractRegime.getInfoTeacher() != null)
            {
                Object[] argsTeacher = {infoGrantContractRegime.getInfoTeacher().getTeacherNumber()};
                infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,"ReadTeacherByNumber", argsTeacher);
                
                if (infoTeacher == null)
                {
                    return setError(request, mapping, "errors.grant.contract.regime.unknownTeacher", null,
                    		infoGrantContractRegime.getInfoTeacher().getTeacherNumber());
                }
                else
                {
                	infoGrantContractRegime.setInfoTeacher(infoTeacher);
                }
            }
            
			/*
			 * If this is a new contract regime, than desactivate all other regime's and set this
			 * as the contract regime active
			 */
			if(infoGrantContractRegime.getState().equals(new Integer(-1)))
			{
				//Read Active Contract Regimes
				Object[] argActiveRegime = { infoGrantContractRegime.getInfoGrantContract().getIdInternal(), new Integer(1) };
				List infoGrantActiveRegimeList = (List) ServiceUtils.executeService(userView, "ReadGrantContractRegimeByContractAndState", argActiveRegime);
				
				//Desactivate them
				for(int i = 0; i < infoGrantActiveRegimeList.size(); i++)
				{
					InfoGrantContractRegime infoGrantRegimeTemp = (InfoGrantContractRegime) infoGrantActiveRegimeList.get(i);
					infoGrantRegimeTemp.setState(new Integer(0));
					Object[] argTemp = { infoGrantRegimeTemp};			
					ServiceUtils.executeService(userView, "EditGrantContractRegime", argTemp);
				}
			}
			
			//Active this regime
			infoGrantContractRegime.setState(new Integer(1));

			//Save the regime
			Object[] args = { infoGrantContractRegime};			
			ServiceUtils.executeService(userView, "EditGrantContractRegime", args);
			
			return mapping.findForward("manage-grant-contract-regime");
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.contract.regime.edit", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
	}
	
	
	/*
	 * Populates form from InfoSubsidy
	 */
	private void setFormGrantContractRegime(DynaValidatorForm form, InfoGrantContractRegime infoGrantContractRegime)
			throws Exception
	{			
		//Grant Contract Regime
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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
		if (infoGrantContractRegime.getDateAcceptTerm() != null)
			form.set("dateAcceptTerm", sdf.format(infoGrantContractRegime.getDateAcceptTerm()));
		if (infoGrantContractRegime.getInfoTeacher() != null)
			form.set("grantContractRegimeTeacherNumber",infoGrantContractRegime.getInfoTeacher().getTeacherNumber().toString());
		if (infoGrantContractRegime.getState() != null)
			form.set("state",infoGrantContractRegime.getState());
		else
			form.set("state", new Integer(-1));
		form.set("contractNumber", infoGrantContractRegime.getInfoGrantContract().getContractNumber().toString());
	}
	
	/*
	 * Populates Info from Form
	 */
	private InfoGrantContractRegime populateInfoFromForm(DynaValidatorForm editGrantContractRegimeForm)
			throws Exception
	{
		InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();
		
		if(verifyStringParameterInForm(editGrantContractRegimeForm,"grantContractRegimeId"))
			infoGrantContractRegime.setIdInternal((Integer) editGrantContractRegimeForm.get("grantContractRegimeId"));
		infoGrantContractRegime.setState((Integer) editGrantContractRegimeForm.get("state"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateBeginContract"))
			infoGrantContractRegime.setDateBeginContract(sdf.parse((String) editGrantContractRegimeForm.get("dateBeginContract")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateEndContract"))
			infoGrantContractRegime.setDateEndContract(sdf.parse((String) editGrantContractRegimeForm.get("dateEndContract")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateSendDispatchCC"))
			infoGrantContractRegime.setDateSendDispatchCC(sdf.parse((String) editGrantContractRegimeForm.get("dateSendDispatchCC")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateDispatchCC"))
			infoGrantContractRegime.setDateDispatchCC(sdf.parse((String) editGrantContractRegimeForm.get("dateDispatchCC")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateSendDispatchCD"))
	    	infoGrantContractRegime.setDateSendDispatchCD(sdf.parse((String) editGrantContractRegimeForm.get("dateSendDispatchCD")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateDispatchCD"))
			infoGrantContractRegime.setDateDispatchCD(sdf.parse((String) editGrantContractRegimeForm.get("dateDispatchCD")));
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"dateAcceptTerm"))
			infoGrantContractRegime.setDateAcceptTerm(sdf.parse((String) editGrantContractRegimeForm.get("dateAcceptTerm")));

	    InfoGrantContract infoGrantContract = new InfoGrantContract();
	    infoGrantContract.setIdInternal((Integer) editGrantContractRegimeForm.get("idContract"));
	    infoGrantContractRegime.setInfoGrantContract(infoGrantContract);
	    
	    if(verifyStringParameterInForm(editGrantContractRegimeForm,"grantContractRegimeTeacherNumber"))
	    {
	    	InfoTeacher infoTeacher = new InfoTeacher();
	    	infoTeacher.setTeacherNumber(new Integer((String) editGrantContractRegimeForm.get("grantContractRegimeTeacherNumber")));
	    	infoGrantContractRegime.setInfoTeacher(infoTeacher);
	    }
	    
		return infoGrantContractRegime;
	}
}