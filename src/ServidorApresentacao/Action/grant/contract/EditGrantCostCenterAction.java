/*
 * Created on 20/Jan/2004
 *  
 */

package ServidorApresentacao.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantCostCenter;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantCostCenterAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantCostCenterForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer idGrantCostCenter = null;
		if (request.getParameter("idGrantCostCenter") != null)
			idGrantCostCenter = new Integer(request.getParameter("idGrantCostCenter"));

		if (idGrantCostCenter != null) //Edit
		{
			try
			{
				DynaValidatorForm grantCostCenterForm = (DynaValidatorForm) form;
				IUserView userView = SessionUtils.getUserView(request);

				//Read the grant type
				Object[] args = { idGrantCostCenter };
				InfoGrantCostCenter infoGrantCostCenter =
					(InfoGrantCostCenter) ServiceUtils.executeService(
						userView,
						"ReadGrantPaymentEntity",
						args);

				//Populate the form
				setFormGrantCostCenter(grantCostCenterForm, infoGrantCostCenter);
			}
			catch (FenixServiceException e)
			{
				return setError(request, mapping, "errors.grant.costcenter.read", null, null);
			}
		}
		return mapping.findForward("edit-grant-costcenter");
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm editGrantCostCenterForm = (DynaValidatorForm) form;
		IUserView userView = SessionUtils.getUserView(request);
		InfoGrantCostCenter infoGrantCostCenter = null;

		try
		{
			infoGrantCostCenter = populateInfoFromForm(editGrantCostCenterForm);

			//Check if teacher exists
//			Object[] args = { infoGrantCostCenter.getInfoResponsibleTeacher().getTeacherNumber()};
//			InfoTeacher infoTeacher =
//				(InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber", args);
//			if (infoTeacher == null)
//				return setError(
//					request,
//					mapping,
//					"errors.grant.paymententity.unknownTeacher",
//					null,
//					editGrantCostCenterForm.get("responsibleTeacherNumber"));
//            infoGrantCostCenter.setInfoResponsibleTeacher(infoTeacher);

            //Edit/Create the payment entity
			Object[] args2 = { infoGrantCostCenter };
			ServiceUtils.executeService(userView, "EditGrantPaymentEntity", args2);
		}
		catch (ExistingServiceException e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.costcenter.duplicateEntry",
				null,
				infoGrantCostCenter.getNumber());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.costcenter.bd.create", null, null);
		}

		return mapping.findForward("manage-grant-costcenter");
	}

	/*
	 * Populates form from InfoCostCenter
	 */
	private void setFormGrantCostCenter(DynaValidatorForm form, InfoGrantCostCenter infoGrantCostCenter)
		throws Exception
	{
		BeanUtils.copyProperties(form, infoGrantCostCenter);
		if(infoGrantCostCenter.getInfoResponsibleTeacher() != null)
			form.set("responsibleTeacherNumber",infoGrantCostCenter.getInfoResponsibleTeacher().getTeacherNumber().toString());
	}

	private InfoGrantCostCenter populateInfoFromForm(DynaValidatorForm editGrantCostCenterForm)
		throws Exception
	{
		InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
		BeanUtils.copyProperties(infoGrantCostCenter, editGrantCostCenterForm);
		
        infoGrantCostCenter.setOjbConcreteClass("Dominio.grant.contract.GrantCostCenter");
        
        //Build the teacher Number
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(
			new Integer((String) editGrantCostCenterForm.get("responsibleTeacherNumber")));
		infoGrantCostCenter.setInfoResponsibleTeacher(infoTeacher);
		
        return infoGrantCostCenter;
	}
}
