package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.RequiredJuriesServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.RequiredJuriesActionException;
import ServidorApresentacao.Action.exceptions.ScholarshipNotFinishedActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.MasterDegreeClassification;
import Util.TipoCurso;

/**
 * 
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */

public class ChangeMasterDegreeProofLookupDispatchAction extends LookupDispatchAction {

	public ActionForward addJury(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		transportData(form, request);

		try {
			operations.getTeachersByNumbers(form, request, "juriesNumbers", SessionConstants.JURIES_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward removeJuries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;

		Integer[] teachersNumbersList = (Integer[]) changeMasterDegreeProofForm.get("juriesNumbers");
		Integer[] removedJuries = (Integer[]) changeMasterDegreeProofForm.get("removedJuriesNumbers");

		changeMasterDegreeProofForm.set("juriesNumbers", subtractArray(teachersNumbersList, removedJuries));

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		transportData(form, request);

		try {
			operations.getTeachersByNumbers(form, request, "juriesNumbers", SessionConstants.JURIES_LIST, actionErrors);
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			saveErrors(request, actionErrors);
		}

		return mapping.findForward("start");

	}

	public ActionForward changeMasterDegreeProof(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm changeMasterDegreeProofForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeType = (Integer) changeMasterDegreeProofForm.get("degreeType");
		Integer studentNumber = (Integer) changeMasterDegreeProofForm.get("studentNumber");
		MasterDegreeClassification finalResult =
			MasterDegreeClassification.getEnum(((Integer) changeMasterDegreeProofForm.get("finalResult")).intValue());
		Integer attachedCopiesNumber = (Integer) changeMasterDegreeProofForm.get("attachedCopiesNumber");
		Integer proofDateDay = (Integer) changeMasterDegreeProofForm.get("proofDateDay");
		Integer proofDateMonth = (Integer) changeMasterDegreeProofForm.get("proofDateMonth");
		Integer proofDateYear = (Integer) changeMasterDegreeProofForm.get("proofDateYear");
		Integer thesisDeliveryDateDay = (Integer) changeMasterDegreeProofForm.get("thesisDeliveryDateDay");
		Integer thesisDeliveryDateMonth = (Integer) changeMasterDegreeProofForm.get("thesisDeliveryDateMonth");
		Integer thesisDeliveryDateYear = (Integer) changeMasterDegreeProofForm.get("thesisDeliveryDateYear");

		Calendar proofDateCalendar = new GregorianCalendar(proofDateYear.intValue(), proofDateMonth.intValue(), proofDateDay.intValue());
		Date proofDate = proofDateCalendar.getTime();
		Calendar thesisDeliveryDateCalendar =
			new GregorianCalendar(thesisDeliveryDateYear.intValue(), thesisDeliveryDateMonth.intValue(), thesisDeliveryDateDay.intValue());
		Date thesisDeliveryDate = thesisDeliveryDateCalendar.getTime();

		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		ArrayList infoTeacherJuries = null;

		// get student curricular plan
		Object args[] = { studentNumber, new TipoCurso(degreeType)};
		try {
			infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) ServiceUtils.executeService(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
		ActionErrors actionErrors = new ActionErrors();

		try {
			operations.getStudentByNumberAndDegreeType(form, request, actionErrors);
			infoTeacherJuries = operations.getTeachersByNumbers(form, request, "juriesNumbers", SessionConstants.JURIES_LIST, actionErrors);

		} catch (Exception e1) {
			throw new FenixActionException(e1);
		} finally {
			if (infoTeacherJuries.isEmpty())
				actionErrors.add("error.exception.masterDegree.noJuriesSelected", new ActionError("error.exception.masterDegree.noJuriesSelected"));

			saveErrors(request, actionErrors);

			if (actionErrors.isEmpty() == false){
				transportData(form, request);
				return mapping.findForward("start");
			}
				

		}

		//
		Object args2[] = { userView, infoStudentCurricularPlan, proofDate, thesisDeliveryDate, finalResult, attachedCopiesNumber, infoTeacherJuries };

		try {
			ServiceUtils.executeService(userView, "ChangeMasterDegreeProof", args2);
		} catch (RequiredJuriesServiceException e) {
			throw new RequiredJuriesActionException(e.getMessage(), mapping.findForward("start"));
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e.getMessage(), mapping.findForward("start"));
		} catch (ScholarshipNotFinishedServiceException e) {
			throw new ScholarshipNotFinishedActionException(e.getMessage(), mapping.findForward("start"));
		} catch (FenixServiceException e) {
			throw new ExistingActionException(e.getMessage(), mapping.findForward("start"));
		}

		return mapping.findForward("success");

	}

	public ActionForward cancelChangeMasterDegreeProof(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		PrepareStudentDataForThesisOperationsDispatchAction prepareStudentDataForThesisOperations =
			new PrepareStudentDataForThesisOperationsDispatchAction();
		return prepareStudentDataForThesisOperations.getStudentAndDegreeTypeForThesisOperations(mapping, form, request, response);

	}

	public void transportData(ActionForm form, HttpServletRequest request) throws FenixActionException {

		// dissertation title
		DynaActionForm masterDegreeProofForm = (DynaActionForm) form;
		String dissertationTitle = (String) masterDegreeProofForm.get("dissertationTitle");
		request.setAttribute(SessionConstants.DISSERTATION_TITLE, dissertationTitle);

		// final result options
		List finalResult = MasterDegreeClassification.toArrayList();
		request.setAttribute(SessionConstants.CLASSIFICATION, finalResult);

	}

	private Integer[] subtractArray(Integer[] originalArray, Integer[] arrayToSubtract) {
		List tmp = new ArrayList();

		for (int i = 0; i < originalArray.length; i++)
			tmp.add((Integer) originalArray[i]);

		for (int i = 0; i < arrayToSubtract.length; i++)
			tmp.remove((Integer) arrayToSubtract[i]);

		originalArray = (Integer[]) tmp.toArray(new Integer[] {
		});
		return originalArray;
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
	 */
	protected Map getKeyMethodMap() {

		Map map = new HashMap();
		map.put("button.submit.masterDegree.thesis.addJury", "addJury");
		map.put("button.submit.masterDegree.thesis.removeJuries", "removeJuries");
		map.put("button.submit.masterDegree.thesis.changeProof", "changeMasterDegreeProof");
		map.put("button.cancel", "cancelChangeMasterDegreeProof");
		return map;
	}

}