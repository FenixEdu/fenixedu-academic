/*
 * Created on 29/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

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

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.grant.contract.InfoGrantProject;
import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.InvalidGrantPaymentEntityException;
import ServidorAplicacao.Servico.exceptions.grant.InvalidProjectResponsibleTeacherException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditGrantPartAction extends DispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantPartForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm grantPartForm = (DynaValidatorForm) form;

		Integer idGrantPart = null;
		Integer edit = null;
		if (request.getParameter("idGrantPart") != null)
			idGrantPart = new Integer(request.getParameter("idGrantPart"));
		if (request.getParameter("edit") != null)
			edit = new Integer(request.getParameter("edit"));

		if (edit == null) //Validation error
		{
			request.setAttribute("idSubsidy", request.getParameter("grantSubsidyId"));
			return mapping.findForward("edit-grant-part");
		}

		if (idGrantPart != null) //Read the contract part
		{
			try
			{
				//Read the contract
				Object[] args = { idGrantPart };
				InfoGrantPart infoGrantPart =
					(InfoGrantPart) ServiceUtils.executeService(userView, "ReadGrantPart", args);

				//Populate the form
				setFormGrantPart(grantPartForm, infoGrantPart);
				request.setAttribute("idSubsidy", infoGrantPart.getInfoGrantSubsidy().getIdInternal());
			}
			catch (FenixServiceException e)
			{
				return setError(request, mapping, "errors.grant.part.read", "manage-grant-part", null);
			}
			catch (Exception e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-part",
					null);
			}
		}
		else //New contract
			{
			try
			{
				Integer idSubsidy = new Integer(request.getParameter("idSubsidy"));
				grantPartForm.set("grantSubsidyId", idSubsidy);
				request.setAttribute("idSubsidy", idSubsidy);
			}
			catch (Exception e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-part",
					null);
			}
		}

		return mapping.findForward("edit-grant-part");
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantPartForm = (DynaValidatorForm) form;
		InfoGrantPart infoGrantPart = null;
		try
		{
			//Verificar se o campo paymentEntityType é diferente null
			if (editGrantPartForm.get("paymentEntityType") == null)
				return setError(
					request,
					mapping,
					"errors.grant.part.invalidPaymentEntityType",
					null,
					null);

			infoGrantPart = populateInfoFromForm(editGrantPartForm);

			Object[] args = { infoGrantPart };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditGrantPart", args);

			request.setAttribute("idSubsidy", editGrantPartForm.get("grantSubsidyId"));
		}
		catch (InvalidGrantPaymentEntityException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.part.invalidPaymentEntity",
				null,
				infoGrantPart.getInfoGrantPaymentEntity().getNumber());
		}
		catch (InvalidProjectResponsibleTeacherException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.part.invalidProjectTeacher",
				null,
				infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.part.edit", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		return mapping.findForward("manage-grant-part");
	}

	public ActionForward doDelete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		try
		{
			Integer idGrantPart = new Integer(request.getParameter("idGrantPart"));
			Integer idSubsidy = new Integer(request.getParameter("idSubsidy"));

			Object[] args = { idGrantPart };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "DeleteGrantPart", args);

			request.setAttribute("idSubsidy", idSubsidy);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.part.delete", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		return mapping.findForward("manage-grant-part");
	}

	/*
	 * Populates form from InfoSubsidy
	 */
	private void setFormGrantPart(DynaValidatorForm form, InfoGrantPart infoGrantPart) throws Exception
	{
		BeanUtils.copyProperties(form, infoGrantPart);
		form.set("grantSubsidyId", infoGrantPart.getInfoGrantSubsidy().getIdInternal());
		if (infoGrantPart.getInfoGrantPaymentEntity().getNumber() != null)
			form.set(
				"grantPaymentEntityNumber",
				infoGrantPart.getInfoGrantPaymentEntity().getNumber().toString());
		if (infoGrantPart.getInfoResponsibleTeacher() != null
			&& infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber() != null)
			form.set(
				"responsibleTeacherNumber",
				infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber().toString());
		if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter)
			form.set("paymentEntityType", new Integer(1));
		else if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantProject)
			form.set("paymentEntityType", new Integer(2));
	}

	private InfoGrantPart populateInfoFromForm(DynaValidatorForm editGrantPartForm) throws Exception
	{
		InfoGrantPart infoGrantPart = new InfoGrantPart();
		if (editGrantPartForm.get("idInternal") != null
			&& !editGrantPartForm.get("idInternal").equals(""))
			infoGrantPart.setIdInternal((Integer) editGrantPartForm.get("idInternal"));

		InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
		infoGrantSubsidy.setIdInternal((Integer) editGrantPartForm.get("grantSubsidyId"));
		infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);

		InfoTeacher infoTeacher = new InfoTeacher();
		infoTeacher.setTeacherNumber(
			new Integer((String) editGrantPartForm.get("responsibleTeacherNumber")));
		infoGrantPart.setInfoResponsibleTeacher(infoTeacher);

		InfoGrantPaymentEntity infoPaymentEntity = null;

		if (editGrantPartForm.get("grantPaymentEntityNumber").equals("1"))
			infoPaymentEntity = new InfoGrantCostCenter();
		else
			infoPaymentEntity = new InfoGrantProject();

		infoPaymentEntity.setNumber(
			new Integer((String) editGrantPartForm.get("grantPaymentEntityNumber")));
		infoGrantPart.setInfoGrantPaymentEntity(infoPaymentEntity);

		//Percentage
		BeanUtils.copyProperties(infoGrantPart, editGrantPartForm);

		return infoGrantPart;
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