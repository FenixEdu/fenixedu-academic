/*
 * Created on 20/Dec/2003
 */

package ServidorApresentacao.Action.grant.qualification;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import DataBeans.person.InfoQualification;
import Dominio.Country;
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

public class EditGrantQualificationAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantQualificationForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer idQualification = null;
		if (request.getParameter("idQualification") != null
			&& !request.getParameter("idQualification").equals(""))
			idQualification = new Integer(request.getParameter("idQualification"));

		DynaValidatorForm grantQualificationForm = (DynaValidatorForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		if (idQualification != null && request.getParameter("load") != null) //Edit
		{
			try
			{
				//Read the contract
				Object[] args = { idQualification };
				InfoQualification infoGrantQualification =
					(InfoQualification) ServiceUtils.executeService(userView, "ReadQualification", args);

				//Populate the form
				setFormGrantQualification(grantQualificationForm, infoGrantQualification);

				request.setAttribute("idPerson", infoGrantQualification.getInfoPerson().getIdInternal());
				request.setAttribute("username", infoGrantQualification.getInfoPerson().getUsername());
				request.setAttribute("idInternal", request.getParameter("idInternal"));
			}
			catch (FenixServiceException e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.qualification.read",
					"manage-grant-qualification",
					null);
			}
		}
		else //New
			{
			try
			{
				Integer idPerson = null;
				if (!request.getParameter("idPerson").equals(""))
					idPerson = new Integer(request.getParameter("idPerson"));
				grantQualificationForm.set("idPerson", idPerson);
				request.setAttribute("idPerson", idPerson);
				request.setAttribute("idInternal", request.getParameter("idInternal"));
				request.setAttribute("username", request.getParameter("username"));
			}
			catch (Exception e)
			{
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-qualification",
					null);
			}
		}

		List countryList = null;
		try
		{
			countryList =
				(List) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadAllCountries",
					null);
		}
		catch (Exception e)
		{
			return setError(
				request,
				mapping,
				"errors.grant.unrecoverable",
				"manage-grant-qualification",
				null);
		}

		//Adding a select country line to the list (presentation reasons)
		Country selectCountry = new Country();
		selectCountry.setIdInternal(null);
		selectCountry.setName("[Escolha um país]");
		countryList.add(0, selectCountry);
		request.setAttribute("countryList", countryList);

		return mapping.findForward("edit-grant-qualification");
	}

	public ActionForward doEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			DynaValidatorForm editGrantQualificationForm = (DynaValidatorForm) form;
			InfoQualification infoGrantQualification = populateInfoFromForm(editGrantQualificationForm);

			request.setAttribute("idInternal", editGrantQualificationForm.get("idInternal"));
			request.setAttribute("idPerson", editGrantQualificationForm.get("idPerson"));
			request.setAttribute("username", editGrantQualificationForm.get("username"));

			Object[] args = { infoGrantQualification.getIdInternal(), infoGrantQualification };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "EditQualification", args);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.qualification.bd.create", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}

		return mapping.findForward("manage-grant-qualification");
	}

	public ActionForward doDelete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			Integer idQualification = new Integer(request.getParameter("idQualification"));
			request.setAttribute("idInternal", request.getParameter("idInternal"));
			request.setAttribute("idPerson", request.getParameter("idPerson"));
			request.setAttribute("username", request.getParameter("username"));

			Object[] args = { idQualification };
			IUserView userView = SessionUtils.getUserView(request);
			ServiceUtils.executeService(userView, "DeleteQualification", args);
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.qualification.bd.delete", null, null);
		}
		catch (Exception e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", null, null);
		}
		return mapping.findForward("manage-grant-qualification");
	}

	/*
	 * Populates form from InfoContract
	 */
	private void setFormGrantQualification(
		DynaValidatorForm form,
		InfoQualification infoGrantQualification)
		throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		form.set("mark", infoGrantQualification.getMark());
		form.set("school", infoGrantQualification.getSchool());
		form.set("title", infoGrantQualification.getTitle());
		form.set("degree", infoGrantQualification.getDegree());
		form.set("qualificationDate", sdf.format(infoGrantQualification.getDate()));
		form.set("branch", infoGrantQualification.getBranch());
		form.set("specializationArea", infoGrantQualification.getSpecializationArea());
		form.set("degreeRecognition", infoGrantQualification.getDegreeRecognition());
		if (infoGrantQualification.getEquivalenceDate() != null)
			form.set("equivalenceDate", sdf.format(infoGrantQualification.getEquivalenceDate()));
		form.set("equivalenceSchool", infoGrantQualification.getEquivalenceSchool());
		form.set("idPerson", infoGrantQualification.getInfoPerson().getIdInternal());
		form.set("idQualification", infoGrantQualification.getIdInternal());
		form.set("username", infoGrantQualification.getInfoPerson().getUsername());
		if (infoGrantQualification.getInfoCountry() != null)
			form.set("country", infoGrantQualification.getInfoCountry().getIdInternal());
	}

	private InfoQualification populateInfoFromForm(DynaValidatorForm editGrantQualificationForm)
		throws Exception
	{
		InfoQualification infoQualification = new InfoQualification();
		InfoPerson infoPerson = new InfoPerson();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		infoQualification.setDate(
			sdf.parse((String) editGrantQualificationForm.get("qualificationDate")));
		infoQualification.setSchool((String) editGrantQualificationForm.get("school"));
		infoQualification.setDegree((String) editGrantQualificationForm.get("degree"));
		infoPerson.setIdInternal((Integer) editGrantQualificationForm.get("idPerson"));

		if (verifyStringParameterInForm(editGrantQualificationForm, "equivalenceDate"))
			infoQualification.setEquivalenceDate(
				sdf.parse((String) editGrantQualificationForm.get("equivalenceDate")));
		if (verifyStringParameterInForm(editGrantQualificationForm, "idQualification"))
			infoQualification.setIdInternal((Integer) editGrantQualificationForm.get("idQualification"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "mark"))
			infoQualification.setMark((String) editGrantQualificationForm.get("mark"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "title"))
			infoQualification.setTitle((String) editGrantQualificationForm.get("title"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "branch"))
			infoQualification.setBranch((String) editGrantQualificationForm.get("branch"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "specializationArea"))
			infoQualification.setSpecializationArea(
				(String) editGrantQualificationForm.get("specializationArea"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "degreeRecognition"))
			infoQualification.setDegreeRecognition(
				(String) editGrantQualificationForm.get("degreeRecognition"));
		if (verifyStringParameterInForm(editGrantQualificationForm, "equivalenceSchool"))
			infoQualification.setEquivalenceSchool(
				(String) editGrantQualificationForm.get("equivalenceSchool"));
		
		InfoCountry infoCountry = new InfoCountry();
		if (((Integer) editGrantQualificationForm.get("country")).equals(new Integer(0)))
			infoCountry.setIdInternal(null);
		else
			infoCountry.setIdInternal((Integer) editGrantQualificationForm.get("country"));
        
        infoQualification.setInfoPerson(infoPerson);
		infoQualification.setInfoCountry(infoCountry);
		return infoQualification;
	}
}
