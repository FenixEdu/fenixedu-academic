/*
 * Created on 29/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.grant.contract.InfoGrantProject;
import DataBeans.grant.contract.InfoGrantSubsidy;
import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantProject;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditGrantPartAction extends FenixDispatchAction
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
		Integer loaddb = null;

		if (request.getParameter("loaddb") != null)
			loaddb = new Integer(request.getParameter("loaddb"));
		if (request.getParameter("idGrantPart") != null)
			idGrantPart = new Integer(request.getParameter("idGrantPart"));

		if (loaddb != null && loaddb.equals(new Integer(1))) //load contents from database
		{
			if (idGrantPart != null)
			{
				try
				{
					//Read the grant part
					Object[] args = { idGrantPart };
					InfoGrantPart infoGrantPart =
						(InfoGrantPart) ServiceUtils.executeService(userView, "ReadGrantPart", args);

					//Populate the form
					setFormGrantPart(grantPartForm, infoGrantPart);
					request.setAttribute(
						"idSubsidy",
						infoGrantPart.getInfoGrantSubsidy().getIdInternal());
				}
				catch (FenixServiceException e)
				{
					return setError(
						request,
						mapping,
						"errors.grant.part.read",
						"manage-grant-part",
						null);
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
			else //New grant part
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
		}
		else
			request.setAttribute("idSubsidy", request.getParameter("grantSubsidyId"));

		ActionForward result = loadPaymentEntities(request, mapping, userView);
		if (result != null)
			return result;

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
		IUserView userView = SessionUtils.getUserView(request);
		try
		{
			//Verificar se foi escolhida UMA E SO UMA entidade pagadora
			if (editGrantPartForm.get("project").equals("0")
				&& editGrantPartForm.get("costCenter").equals("0"))
				return setError(request, mapping, "errors.grant.part.invalidPaymentEntity", null, null);
			if (!editGrantPartForm.get("project").equals("0")
				&& !editGrantPartForm.get("costCenter").equals("0"))
				return setError(
					request,
					mapping,
					"errors.grant.part.mustBeOnePaymentEntity",
					null,
					null);

			infoGrantPart = populateInfoFromForm(editGrantPartForm);

			if (infoGrantPart.getInfoResponsibleTeacher() == null)
			{
				//NO part responsible teacher set YET.
				//The part responsbile teacher will be set to be the payment entity responsible teacher
				Object[] args = { infoGrantPart.getInfoGrantPaymentEntity().getIdInternal()};
				InfoGrantPaymentEntity infoGrantPaymentEntity =
					(InfoGrantPaymentEntity) ServiceUtils.executeService(
						userView,
						"ReadGrantPaymentEntity",
						args);
				infoGrantPart.setInfoResponsibleTeacher(
					infoGrantPaymentEntity.getInfoResponsibleTeacher());
			}

			Object[] args = { infoGrantPart };
			ServiceUtils.executeService(userView, "EditGrantPart", args);

			request.setAttribute("idSubsidy", editGrantPartForm.get("grantSubsidyId"));
		}
		catch (InvalidPartResponsibleTeacherException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.part.invalidPartTeacher",
				null,
				infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber());
		}
		catch (ExistingServiceException e)
		{
			return setError(request, mapping, "errors.grant.part.duplicateEntry", null, null);
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

		if (infoGrantPart.getInfoResponsibleTeacher() != null
			&& infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber() != null)
			form.set(
				"responsibleTeacherNumber",
				infoGrantPart.getInfoResponsibleTeacher().getTeacherNumber().toString());

		if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter)
			form.set("costCenter", infoGrantPart.getInfoGrantPaymentEntity().getIdInternal().toString());
		else if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantProject)
			form.set("project", infoGrantPart.getInfoGrantPaymentEntity().getIdInternal().toString());
	}

	private InfoGrantPart populateInfoFromForm(DynaValidatorForm editGrantPartForm) throws Exception
	{
        InfoGrantPart infoGrantPart = new InfoGrantPart();
        
        //Percentage
        BeanUtils.copyProperties(infoGrantPart, editGrantPartForm);

		if(verifyStringParameterInForm(editGrantPartForm,"idInternal"))
			infoGrantPart.setIdInternal((Integer) editGrantPartForm.get("idInternal"));

		InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
		infoGrantSubsidy.setIdInternal((Integer) editGrantPartForm.get("grantSubsidyId"));
		infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);

		//The part responsible teacher is only set HERE if the user has chosen one in the form
		//Otherwise, the part responsible teacher will be the payment entity responsible teacher
        if(verifyStringParameterInForm(editGrantPartForm,"responsibleTeacherNumber"))
		{
			InfoTeacher infoTeacher = new InfoTeacher();
			infoTeacher.setTeacherNumber(
				new Integer((String) editGrantPartForm.get("responsibleTeacherNumber")));
			infoGrantPart.setInfoResponsibleTeacher(infoTeacher);
		}

		//Set the Payment Entity
		InfoGrantPaymentEntity infoPaymentEntity = null;
		Integer paymentEntityID = null;
		if (!editGrantPartForm.get("project").equals("0"))
		{
			infoPaymentEntity = new InfoGrantProject();
			paymentEntityID = new Integer((String) editGrantPartForm.get("project"));
		}
		else if (!editGrantPartForm.get("costCenter").equals("0"))
		{
			infoPaymentEntity = new InfoGrantCostCenter();
			paymentEntityID = new Integer((String) editGrantPartForm.get("costCenter"));
		}

		infoPaymentEntity.setIdInternal(paymentEntityID);
		infoGrantPart.setInfoGrantPaymentEntity(infoPaymentEntity);
        
		return infoGrantPart;
	}

	/*
	 * Loads information about projects and cost centers to the form
	 */
	private ActionForward loadPaymentEntities(
		HttpServletRequest request,
		ActionMapping mapping,
		IUserView userView)
	{
		try
		{
			String projectClass = "Dominio.grant.contract.GrantProject";
			Object[] args = { projectClass };
			List projectsList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadAllGrantPaymentEntitiesByClassName",
					args);
			//Adding a select project line to the list (presentation reasons)
			GrantProject selectProject = new GrantProject();
			selectProject.setIdInternal(new Integer(0));
			selectProject.setNumber("[Escolha um projecto]");
			projectsList.add(0, selectProject);
			request.setAttribute("projectsList", projectsList);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.part.loadingProjects", null, null);
		}

		try
		{
			String costCenterClass = "Dominio.grant.contract.GrantCostCenter";
			Object[] args2 = { costCenterClass };
			List costCenterList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadAllGrantPaymentEntitiesByClassName",
					args2);
			//Adding a select costCenter line to the list (presentation reasons)
			GrantCostCenter selectCostCenter = new GrantCostCenter();
			selectCostCenter.setIdInternal(new Integer(0));
			selectCostCenter.setDesignation("[Escolha um centro custo]");
			costCenterList.add(0, selectCostCenter);
			request.setAttribute("costCenterList", costCenterList);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.part.loadingCostCenters", null, null);
		}
		return null;
	}
}