/*
 * Created on 20/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.contract.InfoGrantType;
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
public class EditGrantTypeAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantTypeForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer idGrantType = null;
		if (verifyParameterInRequest(request, "idGrantType"))
		{
			idGrantType = new Integer(request.getParameter("idGrantType"));
		}

		if (idGrantType != null) //Edit
		{
			try
			{
				DynaValidatorForm grantTypeForm = (DynaValidatorForm) form;
				IUserView userView = SessionUtils.getUserView(request);

				//Read the grant type
				Object[] args = { idGrantType };
				InfoGrantType infoGrantType =
					(InfoGrantType) ServiceUtils.executeService(userView, "ReadGrantType", args);

				//Populate the form
				setFormGrantType(grantTypeForm, infoGrantType);
			}
			catch (FenixServiceException e)
			{
				return setError(request, mapping, "errors.grant.type.read", null, null);
			}
		}
		return mapping.findForward("edit-grant-type");
	}

	/*
	 * Edit the Grant Type
	 */
	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			DynaValidatorForm editGrantTypeForm = (DynaValidatorForm) form;
			InfoGrantType infoGrantType = populateInfoFromForm(editGrantTypeForm);

			Object[] args = { infoGrantType };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditGrantType", args);
			
			return mapping.findForward("manage-grant-type");
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.type.bd.create", null, null);
		}
	}

	/*
	 * Populates Form from Info
	 */
	private void setFormGrantType(DynaValidatorForm form, InfoGrantType infoGrantType) throws Exception
	{
		BeanUtils.copyProperties(form, infoGrantType);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (infoGrantType.getState() != null)
			form.set("state", sdf.format(infoGrantType.getState()));
	}

	/*
	 * Populates Info from Form
	 */
	private InfoGrantType populateInfoFromForm(DynaValidatorForm editGrantTypeForm) throws Exception
	{
		InfoGrantType infoGrantType = new InfoGrantType();
		
        if(verifyStringParameterInForm(editGrantTypeForm, "minPeriodDays"))
            infoGrantType.setMinPeriodDays(new Integer((String) editGrantTypeForm.get("minPeriodDays")));
		if(verifyStringParameterInForm(editGrantTypeForm, "maxPeriodDays"))
			infoGrantType.setMinPeriodDays(new Integer((String) editGrantTypeForm.get("maxPeriodDays")));
        if(verifyStringParameterInForm(editGrantTypeForm, "indicativeValue"))
			infoGrantType.setIndicativeValue(new Double((String) editGrantTypeForm.get("indicativeValue")));
		
        infoGrantType.setIdInternal((Integer) editGrantTypeForm.get("idInternal"));
        infoGrantType.setName((String) editGrantTypeForm.get("name"));
		infoGrantType.setSigla((String) editGrantTypeForm.get("sigla"));
		infoGrantType.setSource((String) editGrantTypeForm.get("source"));

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(verifyStringParameterInForm(editGrantTypeForm, "state"))
			infoGrantType.setState(sdf.parse((String) editGrantTypeForm.get("state")));

		return infoGrantType;
	}
}
