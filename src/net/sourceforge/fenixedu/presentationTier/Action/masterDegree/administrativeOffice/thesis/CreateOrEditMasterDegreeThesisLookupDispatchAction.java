package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

public class CreateOrEditMasterDegreeThesisLookupDispatchAction extends LookupDispatchAction {

	public ActionForward addGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addAssistentGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeGuiders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] teachersNumbersList = (Integer[]) createMasterDegreeForm.get("guidersNumbers");
		Integer[] removedGuiders = (Integer[]) createMasterDegreeForm.get("removedGuidersNumbers");

		createMasterDegreeForm.set("guidersNumbers", subtractArray(teachersNumbersList, removedGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward externalAssitentGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		// to display the external persons search form
		request.setAttribute(PresentationConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS, new Boolean(true));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward externalGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		// to display the external persons search form
		request.setAttribute(PresentationConstants.SEARCH_EXTERNAL_GUIDERS, new Boolean(true));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward searchExternalAssitentGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByName(form, request, "externalAssistentGuiderName",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);
		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward searchExternalGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByName(form, request, "externalGuiderName",
					PresentationConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS, actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addExternalAssistentGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);
		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward addExternalGuider(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeAssistentGuiders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] teachersNumbersList = (Integer[]) createMasterDegreeForm.get("assistentGuidersNumbers");
		Integer[] removedAssistentGuiders = (Integer[]) createMasterDegreeForm.get("removedAssistentGuidersNumbers");

		createMasterDegreeForm.set("assistentGuidersNumbers", subtractArray(teachersNumbersList, removedAssistentGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeExternalAssistentGuiders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] externalPersonsIDsList = (Integer[]) createMasterDegreeForm.get("externalAssistentGuidersIDs");
		Integer[] removedExternalAssistentGuiders = (Integer[]) createMasterDegreeForm.get("removedExternalAssistentGuidersIDs");

		createMasterDegreeForm.set("externalAssistentGuidersIDs",
				subtractArray(externalPersonsIDsList, removedExternalAssistentGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeExternalGuiders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {

		DynaActionForm createMasterDegreeForm = (DynaActionForm) form;

		Integer[] externalPersonsIDsList = (Integer[]) createMasterDegreeForm.get("externalGuidersIDs");
		Integer[] removedExternalGuiders = (Integer[]) createMasterDegreeForm.get("removedExternalGuidersIDs");

		createMasterDegreeForm.set("externalGuidersIDs", subtractArray(externalPersonsIDsList, removedExternalGuiders));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getTeachersByNumbers(form, request, "guidersNumbers", PresentationConstants.GUIDERS_LIST, actionErrors);
			operations.getTeachersByNumbers(form, request, "assistentGuidersNumbers",
					PresentationConstants.ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalAssistentGuidersIDs",
					PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST, actionErrors);
			operations.getExternalPersonsByIDs(form, request, "externalGuidersIDs", PresentationConstants.EXTERNAL_GUIDERS_LIST,
					actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	private Integer[] subtractArray(Integer[] originalArray, Integer[] arrayToSubtract) {
		List tmp = new ArrayList();

		for (Integer element : originalArray) {
			tmp.add(element);
		}

		for (Integer element : arrayToSubtract) {
			tmp.remove(element);
		}

		originalArray = (Integer[]) tmp.toArray(new Integer[] {});
		return originalArray;
	}

	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.submit.masterDegree.thesis.addGuider", "addGuider");
		map.put("button.submit.masterDegree.thesis.removeGuiders", "removeGuiders");
		map.put("button.submit.masterDegree.thesis.externalAssitentGuider", "externalAssitentGuider");
		map.put("button.submit.masterDegree.thesis.searchExternalAssitentGuider", "searchExternalAssitentGuider");
		map.put("button.submit.masterDegree.thesis.addExternalAssistentGuider", "addExternalAssistentGuider");
		map.put("button.submit.masterDegree.thesis.externalGuider", "externalGuider");
		map.put("button.submit.masterDegree.thesis.searchExternalGuider", "searchExternalGuider");
		map.put("button.submit.masterDegree.thesis.addExternalGuider", "addExternalGuider");
		map.put("button.submit.masterDegree.thesis.removeExternalGuiders", "removeExternalGuiders");
		map.put("button.submit.masterDegree.thesis.addAssistentGuider", "addAssistentGuider");
		map.put("button.submit.masterDegree.thesis.removeAssistentGuiders", "removeAssistentGuiders");
		map.put("button.submit.masterDegree.thesis.removeExternalAssistentGuiders", "removeExternalAssistentGuiders");
		map.put("button.submit.masterDegree.thesis.createThesis", "createMasterDegreeThesis");
		map.put("button.cancel", "cancelCreateMasterDegreeThesis");
		return map;
	}

	public ActionForward cancelMasterDegreeThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrepareStudentDataForThesisOperationsDispatchAction prepareStudentDataForThesisOperations =
				new PrepareStudentDataForThesisOperationsDispatchAction();
		return prepareStudentDataForThesisOperations.getStudentAndDegreeTypeForThesisOperations(mapping, form, request, response);

	}
}