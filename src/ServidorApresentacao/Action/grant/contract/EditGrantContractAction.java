/*
 * Created on 20/Dec/2003
 */

package ServidorApresentacao.Action.grant.contract;

import java.text.SimpleDateFormat;
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

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantResponsibleTeacher;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantContractEndDateBeforeBeginDateException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao
	.Servico
	.exceptions
	.grant
	.GrantOrientationTeacherPeriodNotWithinContractPeriodException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherNotFoundException;
import ServidorAplicacao
	.Servico
	.exceptions
	.grant
	.GrantResponsibleTeacherPeriodNotWithinContractPeriodException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantContractAction extends DispatchAction
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

		Integer idContract = null;
		if (request.getParameter("idContract") != null) //Read the contract number
			idContract = new Integer(request.getParameter("idContract"));

		DynaValidatorForm grantContractForm = (DynaValidatorForm) form;
		if (idContract != null)
		{
			try
			{
				//Read the contract
				Object[] args = { idContract };
				InfoGrantContract infoGrantContract =
					(InfoGrantContract) ServiceUtils.executeService(userView, "ReadGrantContract", args);

				//Populate the form
				setFormGrantContract(grantContractForm, infoGrantContract);
				request.setAttribute(
					"idInternal",
					infoGrantContract.getGrantOwnerInfo().getIdInternal());
			}
			catch (FenixServiceException e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.contract.read",
					"manage-grant-contract",
					null);
			}
			catch (Exception e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-contract",
					null);
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
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-contract",
					null);
			}
		}

		try
		{
			//Read grant types for the contract
			Object[] args2 = {
			};
			List grantTypeList =
				(List) ServiceUtils.executeService(userView, "ReadAllGrantTypes", args2);
			request.setAttribute("grantTypeList", grantTypeList);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.type.read", "manage-grant-contract", null);
		}
		return mapping.findForward("edit-grant-contract");
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantContractForm = (DynaValidatorForm) form;
		InfoGrantContract infoGrantContract = populateInfoFromForm(editGrantContractForm);

		Integer responsibleTeacherNumber =
			infoGrantContract
				.getGrantResponsibleTeacherInfo()
				.getResponsibleTeacherInfo()
				.getTeacherNumber();

		Integer orientationTeacherNumber =
			infoGrantContract
				.getGrantOrientationTeacherInfo()
				.getOrientationTeacherInfo()
				.getTeacherNumber();

		try
		{
			Object[] args = { infoGrantContract };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditGrantContract", args);
		}
		catch (GrantResponsibleTeacherPeriodNotWithinContractPeriodException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.contract.responsible.teacher.periodconflict",
				null,
				null);
		}
		catch (GrantOrientationTeacherPeriodNotWithinContractPeriodException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.contract.orientation.teacher.periodconflict",
				null,
				null);
		}
		catch (GrantContractEndDateBeforeBeginDateException e)
		{
			return setError(request, mapping, "errors.grant.contract.conflictdates", null, null);
		}
		catch (GrantTypeNotFoundException e)
		{
			return setError(request, mapping, "errors.grant.type.not.found", null, null);
		}
		catch (GrantResponsibleTeacherNotFoundException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.contract.responsible.teacher.not.found",
				null,
				responsibleTeacherNumber);
		}
		catch (GrantOrientationTeacherNotFoundException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.contract.orientation.teacher.not.found",
				null,
				orientationTeacherNumber);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.contract.bd.create", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		request.setAttribute("idInternal", editGrantContractForm.get("idInternal"));

		return mapping.findForward("manage-grant-contract");
	}

	/*
	 * Populates form from InfoContract
	 */
	private void setFormGrantContract(DynaValidatorForm form, InfoGrantContract infoGrantContract)
		throws Exception
	{
		//BeanUtils.copyProperties(form, infoGrantContract);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		form.set("idGrantContract", infoGrantContract.getIdInternal());
		if (infoGrantContract.getDateBeginContract() != null)
			form.set("dateBeginContract", sdf.format(infoGrantContract.getDateBeginContract()));
		if (infoGrantContract.getDateEndContract() != null)
			form.set("dateEndContract", sdf.format(infoGrantContract.getDateEndContract()));

		if (infoGrantContract.getDateSendDispatchCC() != null)
			form.set("dateSendDispatchCC", sdf.format(infoGrantContract.getDateSendDispatchCC()));
		if (infoGrantContract.getDateDispatchCC() != null)
			form.set("dateDispatchCC", sdf.format(infoGrantContract.getDateDispatchCC()));
		if (infoGrantContract.getDateSendDispatchCD() != null)
			form.set("dateSendDispatchCD", sdf.format(infoGrantContract.getDateSendDispatchCD()));
		if (infoGrantContract.getDateDispatchCD() != null)
			form.set("dateDispatchCD", sdf.format(infoGrantContract.getDateDispatchCD()));
		if (infoGrantContract.getDateAcceptTerm() != null)
			form.set("dateAcceptTerm", sdf.format(infoGrantContract.getDateAcceptTerm()));

		if (infoGrantContract.getEndContractMotive() != null)
			form.set("endContractMotive", infoGrantContract.getEndContractMotive());
		form.set("grantType", infoGrantContract.getGrantTypeInfo().getSigla());
		form.set("contractNumber", infoGrantContract.getContractNumber().toString());
		form.set(
			"grantResponsibleTeacher",
			infoGrantContract
				.getGrantResponsibleTeacherInfo()
				.getResponsibleTeacherInfo()
				.getTeacherNumber()
				.toString());
		form.set(
			"grantOrientationTeacher",
			infoGrantContract
				.getGrantOrientationTeacherInfo()
				.getOrientationTeacherInfo()
				.getTeacherNumber()
				.toString());
		form.set("idInternal", infoGrantContract.getGrantOwnerInfo().getIdInternal());
		form.set(
			"grantResponsibleTeacherIdInternal",
			infoGrantContract.getGrantResponsibleTeacherInfo().getIdInternal());
		form.set(
			"grantOrientationTeacherIdInternal",
			infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal());
	}

	private InfoGrantContract populateInfoFromForm(DynaValidatorForm editGrantContractForm)
		throws Exception
	{
		InfoGrantContract infoGrantContract = new InfoGrantContract();
		InfoGrantOrientationTeacher orientationTeacher = new InfoGrantOrientationTeacher();
		InfoGrantResponsibleTeacher responsibleTeacher = new InfoGrantResponsibleTeacher();
		InfoTeacher orientationInfoTeacher = new InfoTeacher();
		InfoTeacher responsibleInfoTeacher = new InfoTeacher();
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		InfoGrantType grantType = new InfoGrantType();

		//Format of date in the form
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		//set dateBeginContract and dateEndContract
		if (editGrantContractForm.get("dateBeginContract") != null
			&& !editGrantContractForm.get("dateBeginContract").equals(""))
		{
			infoGrantContract.setDateBeginContract(
				sdf.parse((String) editGrantContractForm.get("dateBeginContract")));
			responsibleTeacher.setBeginDate(
				sdf.parse((String) editGrantContractForm.get("dateBeginContract")));
			orientationTeacher.setBeginDate(
				sdf.parse((String) editGrantContractForm.get("dateBeginContract")));
		}
		if (editGrantContractForm.get("dateEndContract") != null
			&& !editGrantContractForm.get("dateEndContract").equals(""))
		{
			infoGrantContract.setDateEndContract(
				sdf.parse((String) editGrantContractForm.get("dateEndContract")));
			responsibleTeacher.setEndDate(
				sdf.parse((String) editGrantContractForm.get("dateEndContract")));
			orientationTeacher.setEndDate(
				sdf.parse((String) editGrantContractForm.get("dateEndContract")));
		}

		//set other dates
		if (editGrantContractForm.get("dateSendDispatchCC") != null
			&& !editGrantContractForm.get("dateSendDispatchCC").equals(""))
			infoGrantContract.setDateSendDispatchCC(
				sdf.parse((String) editGrantContractForm.get("dateSendDispatchCC")));
		if (editGrantContractForm.get("dateDispatchCC") != null
			&& !editGrantContractForm.get("dateDispatchCC").equals(""))
			infoGrantContract.setDateDispatchCC(
				sdf.parse((String) editGrantContractForm.get("dateDispatchCC")));
		if (editGrantContractForm.get("dateSendDispatchCD") != null
			&& !editGrantContractForm.get("dateSendDispatchCD").equals(""))
			infoGrantContract.setDateSendDispatchCD(
				sdf.parse((String) editGrantContractForm.get("dateSendDispatchCD")));
		if (editGrantContractForm.get("dateDispatchCD") != null
			&& !editGrantContractForm.get("dateDispatchCD").equals(""))
			infoGrantContract.setDateDispatchCD(
				sdf.parse((String) editGrantContractForm.get("dateDispatchCD")));
		if (editGrantContractForm.get("dateAcceptTerm") != null
			&& !editGrantContractForm.get("dateAcceptTerm").equals(""))
			infoGrantContract.setDateAcceptTerm(
				sdf.parse((String) editGrantContractForm.get("dateAcceptTerm")));

		if (editGrantContractForm.get("idGrantContract") != null)
			infoGrantContract.setIdInternal((Integer) editGrantContractForm.get("idGrantContract"));
		if (editGrantContractForm.get("contractNumber") != null
			&& !editGrantContractForm.get("contractNumber").equals(""))
			infoGrantContract.setContractNumber(
				new Integer((String) editGrantContractForm.get("contractNumber")));
		infoGrantOwner.setIdInternal((Integer) editGrantContractForm.get("idInternal"));
		infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

		infoGrantContract.setEndContractMotive((String) editGrantContractForm.get("endContractMotive"));
		grantType.setSigla((String) editGrantContractForm.get("grantType"));
		infoGrantContract.setGrantTypeInfo(grantType);

		orientationInfoTeacher.setTeacherNumber(
			new Integer((String) editGrantContractForm.get("grantOrientationTeacher")));
		orientationTeacher.setOrientationTeacherInfo(orientationInfoTeacher);
		orientationTeacher.setIdInternal(
			(Integer) editGrantContractForm.get("grantOrientationTeacherIdInternal"));
		infoGrantContract.setGrantOrientationTeacherInfo(orientationTeacher);

		responsibleInfoTeacher.setTeacherNumber(
			new Integer((String) editGrantContractForm.get("grantResponsibleTeacher")));
		responsibleTeacher.setResponsibleTeacherInfo(responsibleInfoTeacher);
		responsibleTeacher.setIdInternal(
			(Integer) editGrantContractForm.get("grantResponsibleTeacherIdInternal"));
		infoGrantContract.setGrantResponsibleTeacherInfo(responsibleTeacher);

		return infoGrantContract;
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
