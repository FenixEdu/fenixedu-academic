package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.GuiderAlreadyChosenServiceException;
import ServidorAplicacao.Servico.exceptions.RequiredGuidersServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.GuiderAlreadyChosenActionException;
import ServidorApresentacao.Action.exceptions.RequiredGuidersActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class ChangeMasterDegreeThesisLookupDispatchAction extends LookupDispatchAction
{

	public ActionForward addGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);

		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addAssistentGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeGuiders(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] teachersNumbersList = (Integer[]) createMasterDegreeForm.get("guidersNumbers");
		Integer[] removedGuiders = (Integer[]) createMasterDegreeForm.get("removedGuidersNumbers");

		createMasterDegreeForm.set("guidersNumbers", subtractArray(teachersNumbersList, removedGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward externalAssitentGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		//	to display the external persons search form
		request.setAttribute(SessionConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS, new Boolean(true));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward externalGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		//	to display the external persons search form
		request.setAttribute(SessionConstants.SEARCH_EXTERNAL_GUIDERS, new Boolean(true));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);

		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward searchExternalAssitentGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByName(
				form,
				request,
				"externalAssistentGuiderName",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward searchExternalGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByName(
				form,
				request,
				"externalGuiderName",
				SessionConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS,
				actionErrors);

		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addExternalAssistentGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addExternalGuider(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeAssistentGuiders(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] teachersNumbersList =
			(Integer[]) createMasterDegreeForm.get("assistentGuidersNumbers");
		Integer[] removedAssistentGuiders =
			(Integer[]) createMasterDegreeForm.get("removedAssistentGuidersNumbers");

		createMasterDegreeForm.set(
			"assistentGuidersNumbers",
			subtractArray(teachersNumbersList, removedAssistentGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeExternalAssistentGuiders(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] externalPersonsIDsList =
			(Integer[]) createMasterDegreeForm.get("externalAssistentGuidersIDs");
		Integer[] removedExternalAssistentGuiders =
			(Integer[]) createMasterDegreeForm.get("removedExternalAssistentGuidersIDs");

		createMasterDegreeForm.set(
			"externalAssistentGuidersIDs",
			subtractArray(externalPersonsIDsList, removedExternalAssistentGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeExternalGuiders(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] externalPersonsIDsList = (Integer[]) createMasterDegreeForm.get("externalGuidersIDs");
		Integer[] removedExternalGuiders =
			(Integer[]) createMasterDegreeForm.get("removedExternalGuidersIDs");

		createMasterDegreeForm.set(
			"externalGuidersIDs",
			subtractArray(externalPersonsIDsList, removedExternalGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getTeachersByNumbers(
				form,
				request,
				"guidersNumbers",
				SessionConstants.GUIDERS_LIST,
				actionErrors);
			operations.getTeachersByNumbers(
				form,
				request,
				"assistentGuidersNumbers",
				SessionConstants.ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalAssistentGuidersIDs",
				SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
				actionErrors);
			operations.getExternalPersonsByIDs(
				form,
				request,
				"externalGuidersIDs",
				SessionConstants.EXTERNAL_GUIDERS_LIST,
				actionErrors);

		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward changeMasterDegreeThesis(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeType = (Integer) createMasterDegreeForm.get("degreeType");
		Integer studentNumber = (Integer) createMasterDegreeForm.get("studentNumber");
		String dissertationTitle = (String) createMasterDegreeForm.get("dissertationTitle");
		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		ArrayList infoTeacherGuiders = null;
		ArrayList infoTeacherAssistentGuiders = null;
		ArrayList infoExternalPersonExternalGuiders = null;
		ArrayList infoExternalPersonExternalAssistentGuiders = null;

		Object args[] = { studentNumber, new TipoCurso(degreeType)};
		try
		{
			infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) ServiceUtils.executeService(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					args);
		} catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try
		{
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			infoTeacherGuiders =
				operations.getTeachersByNumbers(
					form,
					request,
					"guidersNumbers",
					SessionConstants.GUIDERS_LIST,
					actionErrors);
			infoTeacherAssistentGuiders =
				operations.getTeachersByNumbers(
					form,
					request,
					"assistentGuidersNumbers",
					SessionConstants.ASSISTENT_GUIDERS_LIST,
					actionErrors);
			infoExternalPersonExternalAssistentGuiders =
				operations.getExternalPersonsByIDs(
					form,
					request,
					"externalAssistentGuidersIDs",
					SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST,
					actionErrors);
			infoExternalPersonExternalGuiders =
				operations.getExternalPersonsByIDs(
					form,
					request,
					"externalGuidersIDs",
					SessionConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);
		} catch (Exception e1)
		{
			throw new FenixActionException(e1);
		} finally
		{
			if (infoTeacherGuiders.isEmpty())
				actionErrors.add(
					"error.exception.masterDegree.noGuidersSelected",
					new ActionError("error.exception.masterDegree.noGuidersSelected"));

			saveErrors(request, actionErrors);

			if (actionErrors.isEmpty() == false)
				return mapping.findForward("start");

		}

		Object args2[] =
			{
				userView,
				infoStudentCurricularPlan,
				dissertationTitle,
				infoTeacherGuiders,
				infoTeacherAssistentGuiders,
				infoExternalPersonExternalGuiders,
				infoExternalPersonExternalAssistentGuiders };

		try
		{
			ServiceUtils.executeService(userView, "ChangeMasterDegreeThesisData", args2);
		} catch (RequiredGuidersServiceException e)
		{
			throw new RequiredGuidersActionException(e.getMessage(), mapping.findForward("start"));
		} catch (GuiderAlreadyChosenServiceException e)
		{
			throw new GuiderAlreadyChosenActionException(e.getMessage(), mapping.findForward("start"));
		} catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
		} catch (FenixServiceException e)
		{
			throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
		}

		return mapping.findForward("success");

	}

	public ActionForward cancelChangeMasterDegreeThesis(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		PrepareStudentDataForThesisOperationsDispatchAction prepareStudentDataForThesisOperations =
			new PrepareStudentDataForThesisOperationsDispatchAction();
		return prepareStudentDataForThesisOperations.getStudentAndDegreeTypeForThesisOperations(
			mapping,
			form,
			request,
			response);

	}

	private Integer[] subtractArray(Integer[] originalArray, Integer[] arrayToSubtract)
	{
		List tmp = new ArrayList();

		for (int i = 0; i < originalArray.length; i++)
			tmp.add(originalArray[i]);

		for (int i = 0; i < arrayToSubtract.length; i++)
			tmp.remove(arrayToSubtract[i]);

		originalArray = (Integer[]) tmp.toArray(new Integer[] {
		});
		return originalArray;
	}

	private String getFromRequest(String parameter, HttpServletRequest request)
	{
		String parameterString = request.getParameter(parameter);
		if (parameterString == null)
		{
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
	 */
	protected Map getKeyMethodMap()
	{

		Map map = new HashMap();
		map.put("button.submit.masterDegree.thesis.addGuider", "addGuider");
		map.put("button.submit.masterDegree.thesis.removeGuiders", "removeGuiders");
		map.put("button.submit.masterDegree.thesis.externalAssitentGuider", "externalAssitentGuider");
		map.put(
			"button.submit.masterDegree.thesis.searchExternalAssitentGuider",
			"searchExternalAssitentGuider");
		map.put(
			"button.submit.masterDegree.thesis.addExternalAssistentGuider",
			"addExternalAssistentGuider");
		map.put("button.submit.masterDegree.thesis.externalGuider", "externalGuider");
		map.put("button.submit.masterDegree.thesis.searchExternalGuider", "searchExternalGuider");
		map.put("button.submit.masterDegree.thesis.addExternalGuider", "addExternalGuider");
		map.put("button.submit.masterDegree.thesis.removeExternalGuiders", "removeExternalGuiders");
		map.put("button.submit.masterDegree.thesis.addAssistentGuider", "addAssistentGuider");
		map.put("button.submit.masterDegree.thesis.removeAssistentGuiders", "removeAssistentGuiders");
		map.put(
			"button.submit.masterDegree.thesis.removeExternalAssistentGuiders",
			"removeExternalAssistentGuiders");
		map.put("button.submit.masterDegree.thesis.changeThesis", "changeMasterDegreeThesis");
		map.put("button.cancel", "cancelChangeMasterDegreeThesis");
		return map;
	}

}