/*
 * Created on 20/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.contract.InfoGrantType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditGrantTypeAction extends DispatchAction
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
		if (request.getParameter("idGrantType") != null)
			idGrantType = new Integer(request.getParameter("idGrantType"));

		DynaValidatorForm grantTypeForm = (DynaValidatorForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		if (idGrantType != null) //Edit - read the grant type
		{
			try
			{
				Object[] args = { idGrantType };
				InfoGrantType infoGrantType =
					(InfoGrantType) ServiceUtils.executeService(userView, "ReadGrantType", args);

				setFormGrantType(grantTypeForm, infoGrantType);
			}
			catch (Exception e)
			{
				return setError(request, mapping, "errors.grant.type.bd.read", "manage-grant-type", null);
			}
		}
		return mapping.findForward("edit-grant-type");
	}

	/*
	 * Edit a Grant Type
	 */
	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantTypeForm = (DynaValidatorForm) form;

		try
		{
			InfoGrantType infoGrantType = populateInfoFromForm(editGrantTypeForm);
			if (infoGrantType.getMaxPeriodDays().intValue()
				< infoGrantType.getMinPeriodDays().intValue())
				return setError(request, mapping, "errors.grant.type.maxminconflit", null, null);

			Object[] args = { infoGrantType };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditGrantType", args);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.type.bd.edit", null, null);
		}
		return mapping.findForward("manage-grant-type");
	}

	/*
	 * Populates form from InfoContract
	 */
	private void setFormGrantType(DynaValidatorForm form, InfoGrantType infoGrantType) throws Exception
	{
		BeanUtils.copyProperties(form, infoGrantType);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (infoGrantType.getState() != null)
			form.set("state", sdf.format(infoGrantType.getState()));
	}

	private InfoGrantType populateInfoFromForm(DynaValidatorForm editGrantTypeForm) throws Exception
	{
		InfoGrantType infoGrantType = new InfoGrantType();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (!editGrantTypeForm.get("state").equals(""))
			infoGrantType.setState(sdf.parse((String) editGrantTypeForm.get("state")));
		if (!editGrantTypeForm.get("minPeriodDays").equals(""))
			infoGrantType.setMinPeriodDays(new Integer((String) editGrantTypeForm.get("minPeriodDays")));
		else
			infoGrantType.setMinPeriodDays(new Integer(0));
		if (!editGrantTypeForm.get("maxPeriodDays").equals(""))
			infoGrantType.setMaxPeriodDays(new Integer((String) editGrantTypeForm.get("maxPeriodDays")));
		else
			infoGrantType.setMaxPeriodDays(new Integer(0));
		if (!editGrantTypeForm.get("indicativeValue").equals(""))
			infoGrantType.setIndicativeValue(
				new Double((String) editGrantTypeForm.get("indicativeValue")));

		//The next fields are required
		infoGrantType.setIdInternal((Integer) editGrantTypeForm.get("idInternal"));
		infoGrantType.setName((String) editGrantTypeForm.get("name"));
		infoGrantType.setSigla((String) editGrantTypeForm.get("sigla"));
		infoGrantType.setSource((String) editGrantTypeForm.get("source"));

		return infoGrantType;
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
		ActionError error = new ActionError(errorMessage, actionArg);
		errors.add(errorMessage, error);
		saveErrors(request, errors);

		if (forwardPage != null)
			return mapping.findForward(forwardPage);
		else
			return mapping.getInputForward();
	}
}
