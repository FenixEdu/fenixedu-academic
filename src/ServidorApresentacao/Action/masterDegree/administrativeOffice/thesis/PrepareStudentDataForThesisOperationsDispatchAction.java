package ServidorApresentacao.Action.masterDegree.administrativeOffice.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
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

public class PrepareStudentDataForThesisOperationsDispatchAction extends DispatchAction {

	public ActionForward getStudentAndDegreeTypeForThesisOperations(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		
		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeType = null;
		Integer studentNumber = null;
		String forward = "success";

		try {
			degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
			studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));
		} catch (Exception e) {
			//case when the user cancels the current form, and goes back to the intial page of master degree thesis operations (index page)
			degreeType = (Integer) getStudentByNumberAndDegreeTypeForm.get("degreeType");
			studentNumber = (Integer) getStudentByNumberAndDegreeTypeForm.get("studentNumber");
			forward = "cancel";
		}

		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		InfoStudent infoStudent = null;
		InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

		/* * * get student * * */
		Object argsStudent[] = { studentNumber,new TipoCurso(degreeType) };
		try {
			infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "ReadStudentByNumberAndDegreeType", argsStudent);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoStudent == null) {
			throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent", mapping.findForward("error"));

		}

		request.setAttribute(SessionConstants.STUDENT, infoStudent);

		/* * * get student curricular plan * * */
		Object argsStudentCurricularPlan[] = { studentNumber, new TipoCurso(degreeType)};
		try {
			infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) ServiceUtils.executeService(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsStudentCurricularPlan);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoStudentCurricularPlan == null) {
			throw new NonExistingActionException("error.exception.masterDegree.nonExistentActiveStudentCurricularPlan", mapping.findForward("error"));

		}

		/* * * get master degree thesis data * * */
		Object argsMasterDegreeThesisDataVersion[] = { infoStudentCurricularPlan };
		try {
			infoMasterDegreeThesisDataVersion =
				(InfoMasterDegreeThesisDataVersion) ServiceUtils.executeService(
					userView,
					"ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
					argsMasterDegreeThesisDataVersion);
		} catch (NonExistingServiceException e) {
			// no active master degree thesis
			if (forward.equals("cancel"))
				return mapping.findForward("createThesisCancel");
			else
				return mapping.findForward("createThesis");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward(forward);

	}

}